package prototype.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import prototype.builder.SharepointFileContentBuilder;
import prototype.builder.SharepointObjectBuilder;
import prototype.config.SharepointConfig;
import org.apache.http.client.CredentialsProvider;
import prototype.config.SharepointUrlConfig;
import prototype.filter.SharepointContentFilter;
import prototype.model.file.SharepointFile;
import prototype.model.folder.SharepointFolder;
import prototype.model.list.SharepointList;
import prototype.model.list.SharepointListItem;
import prototype.model.membership.SharepointGroup;
import prototype.model.membership.SharepointUser;
import prototype.model.recyclebin.SharepointRecycledItem;
import prototype.model.site.SharepointSite;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SharepointClient {
    public List<SharepointSite> sites = new ArrayList<>();
    protected SharepointConfig sharepointConfig;
    protected HttpClient httpClient;
    protected SharepointRequest sharepointRequest;
    protected SharepointObjectBuilder sharepointObjectBuilder;
    protected URI siteURI;
    protected SharepointFileContentBuilder sharepointFileContentBuilder;
    protected SharepointContentFilter sharepointContentFilter;
    private SharepointSite currentSite;
    private final SharepointResponseExceptionHandler sharepointResponseExceptionHandler;

    public SharepointClient(SharepointConfig sharepointConfig) throws URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        this.sharepointConfig = sharepointConfig;
        this.siteURI = new URI(sharepointConfig.getRootSite());
        this.httpClient = createHttpClient();
        this.sharepointRequest = new SharepointRequest(sharepointConfig);
        this.sharepointObjectBuilder = new SharepointObjectBuilder(this, siteURI, sharepointConfig);
        this.sharepointFileContentBuilder = new SharepointFileContentBuilder();
        this.sharepointContentFilter = new SharepointContentFilter(sharepointConfig);
        this.sharepointResponseExceptionHandler = new SharepointResponseExceptionHandler();
    }

    private HttpClient createHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        AuthScope authScope = new AuthScope(siteURI.getHost(), siteURI.getPort(), AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);

        NTCredentials ntCredentials = new NTCredentials(
                sharepointConfig.getUsername(),
                sharepointConfig.getPassword(),
                siteURI.getHost(),
                sharepointConfig.getDomain()
        );

        credentialsProvider.setCredentials(authScope, ntCredentials);

        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory> create()
                        .register("https", sslConnectionSocketFactory)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        return HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setConnectionManager(connectionManager)
                .build();
    }

    public List<SharepointSite> collectSiteData() throws IOException {
        String targetApiPath = SharepointUrlConfig.SHAREPOINT_API_PATH;
        /* We expand the Author field to get the user who created the site */
        HttpGet httpGet = sharepointRequest.createGetRequestObject(sharepointConfig.getRootSite() + targetApiPath + "?$expand=Author");

        /* Execute the initial request - we will get data we need for further searching */
        System.out.println("Executing initial GET request to " + sharepointConfig.getRootSite() + targetApiPath + "...");
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode(), response);
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* Let's add the root site to the sites list - our initial response body */
        JSONObject d = (JSONObject) responseBody.get("d");
        currentSite = sharepointObjectBuilder.buildSharepointSite(d);
        sites.add(currentSite);

        /* Determine the sub sites */
        if (!sharepointConfig.isSkipSubsites()) {
            String websPath = d.getJSONObject("Webs").getJSONObject("__deferred").getString("uri");
            determineSubSites(websPath);
        }

        /* With our content filter we can expect nulls to be added to the sites list, so remove them before returning */
        return sites.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void determineSubSites(String websPath) throws IOException {
        System.out.println("Searching for subsites");

        /* We expand the Author field to get the user who created the site */
        HttpGet httpGet = sharepointRequest.createGetRequestObject(websPath + "?$expand=Author");
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");
        /* Iterate the results */
        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                currentSite = sharepointObjectBuilder.buildSharepointSite(resultObj);
                /* add the site */
                sites.add(currentSite);
                /* recursively search for subsites - we can build the webs api path for each site here
                 *  this will keep us from making too many requests to the sharepoint site
                 */
                if (currentSite != null) {
                    String currentSiteUrl = currentSite.getUrl();
                    String newWebsPath = currentSiteUrl + SharepointUrlConfig.SHAREPOINT_API_PATH + SharepointUrlConfig.SHAREPOINT_WEBS_PATH;
                    try {
                        determineSubSites(newWebsPath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Site references or contains unwanted data...");
                }
            });
        }
    }

    public List<SharepointList> determineSiteLists(String siteUrl) throws IOException {
        System.out.println("Searching for lists");

        String sitePathUrl = siteUrl + SharepointUrlConfig.SHAREPOINT_API_PATH + SharepointUrlConfig.SHAREPOINT_LISTS_PATH;
        /* might need modification to the site list path if filters are applied */
        sitePathUrl = sharepointContentFilter.applyFilterToListPath(sitePathUrl);
        sitePathUrl = sharepointContentFilter.applyOrderToListPath(sitePathUrl);
        sitePathUrl = sharepointContentFilter.applySkipToListPath(sitePathUrl);
        sitePathUrl = sharepointContentFilter.applySelectToListPath(sitePathUrl);

        HttpGet httpGet = sharepointRequest.createGetRequestObject(sitePathUrl);
        HttpResponse response = getSharePointData(httpGet);

        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointList> lists = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                try {
                    lists.add(sharepointObjectBuilder.buildSharepointList(resultObj));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return lists;
    }

    public  List<SharepointListItem> determineListItems(JSONObject itemsObj) throws IOException {
        System.out.println("Searching for list items");

        JSONObject deferredObj = itemsObj.getJSONObject("__deferred");
        String itemsPath = deferredObj.getString("uri");

        HttpGet httpGet = sharepointRequest.createGetRequestObject(itemsPath);
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointListItem> items = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                items.add(sharepointObjectBuilder.buildSharepointListItem(resultObj));
            });
        }

        return items;
    }

    public List<String> discoverListItemAttachments(JSONObject attachmentsObj, String currentSiteTitle) throws IOException {
        System.out.println("Discovering list item attachments");

        JSONObject deferredObj = attachmentsObj.getJSONObject("__deferred");
        String attachmentsPath = deferredObj.getString("uri");

        HttpGet httpGet = sharepointRequest.createGetRequestObject(attachmentsPath);
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<String> attachments = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                String fileName = resultObj.getString("FileName");
                attachments.add(fileName);
                if (!sharepointConfig.getNoDownload()) {
                    new Thread(() -> downloadFileContent(resultObj, currentSiteTitle, fileName)).start();
                }
            });
        }

        return attachments;
    }

    public List<SharepointFolder> determineSiteFolders(String siteUrl) throws IOException {
        System.out.println("Searching for folders");

        String sitePathUrl = siteUrl + SharepointUrlConfig.SHAREPOINT_API_PATH + SharepointUrlConfig.SHAREPOINT_FOLDERS_PATH;
        /* might need modification to the site list path if filters are applied */
        sitePathUrl = sharepointContentFilter.applyFilterToFolderPath(sitePathUrl);
        sitePathUrl = sharepointContentFilter.applyOrderToFolderPath(sitePathUrl);
        sitePathUrl = sharepointContentFilter.applySelectToFolderPath(sitePathUrl);
        sitePathUrl = sharepointContentFilter.applySkipToFolderPath(sitePathUrl);

        HttpGet httpGet = sharepointRequest.createGetRequestObject(sitePathUrl);
        HttpResponse response = getSharePointData(httpGet);

        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointFolder> folders = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                folders.add(sharepointObjectBuilder.buildSharepointFolder(resultObj));
            });
        }

        return folders;
    }

    public List<SharepointFolder> determineSubFolders(JSONObject foldersObj) throws IOException {
        System.out.println("Searching for sub folders");

        JSONObject deferredObj = foldersObj.getJSONObject("__deferred");
        String foldersPath = deferredObj.getString("uri");

        HttpGet httpGet = sharepointRequest.createGetRequestObject(foldersPath);
        HttpResponse response = getSharePointData(httpGet);

        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode(), response);
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointFolder> subFolders = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                subFolders.add(sharepointObjectBuilder.buildSharepointFolder(resultObj));
            });
        }

        return subFolders;
    }

    public  List<SharepointFile> determineFiles(JSONObject folderObj) throws IOException {
        System.out.println("Searching for files");

        JSONObject deferredObj = folderObj.getJSONObject("__deferred");
        String filesPath = deferredObj.getString("uri");
        /* might need modification to the site list path if filters are applied */
        filesPath = sharepointContentFilter.applyFilterToFilePath(filesPath);
        filesPath = sharepointContentFilter.applyOrderToFilePath(filesPath);
        filesPath = sharepointContentFilter.applySkipToFilePath(filesPath);
        filesPath = sharepointContentFilter.applySelectToFilePath(filesPath);

        /* we always want to expand the Author property */
        if (filesPath.contains("$filter") || filesPath.contains("$order") ||
                filesPath.contains("$skip") || filesPath.contains("$select")) {
            filesPath = filesPath + "&?$expand=Author";
        } else {
            filesPath = filesPath + "?$expand=Author";
        }

        HttpGet httpGet = sharepointRequest.createGetRequestObject(filesPath);
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointFile> files = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                try {
                    files.add(sharepointObjectBuilder.buildSharepointFile(resultObj));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return files;
    }

    public  List<SharepointRecycledItem> determineRecycledItems(JSONObject recycleObj) throws IOException {
        System.out.println("Searching for recycled items");

        JSONObject deferredObj = recycleObj.getJSONObject("__deferred");
        String itemsPath = deferredObj.getString("uri");

        HttpGet httpGet = sharepointRequest.createGetRequestObject(itemsPath);
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointRecycledItem> recycledItems = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                recycledItems.add(sharepointObjectBuilder.buildSharepointRecycledItem(resultObj));
            });
        }

        return recycledItems;
    }

    public  List<SharepointGroup> determineGroups(JSONObject groupObj) throws IOException {
        System.out.println("Searching for groups");

        JSONObject deferredObj = groupObj.getJSONObject("__deferred");
        String itemsPath = deferredObj.getString("uri");

        HttpGet httpGet = sharepointRequest.createGetRequestObject(itemsPath);
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointGroup> groups = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                groups.add(sharepointObjectBuilder.buildSharepointGroup(resultObj));
            });
        }

        return groups;
    }

    public  List<SharepointUser> determineGroupUsers(JSONObject usersObj) throws IOException {
        System.out.println("Searching for group users");

        JSONObject deferredObj = usersObj.getJSONObject("__deferred");
        String itemsPath = deferredObj.getString("uri");

        HttpGet httpGet = sharepointRequest.createGetRequestObject(itemsPath);
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        /* view the results array from the response */
        JSONObject d = (JSONObject) responseBody.get("d");
        JSONArray resultsArray = d.getJSONArray("results");

        List<SharepointUser> users = new ArrayList<>();

        if (resultsArray.length() > 0) {
            resultsArray.forEach(result -> {
                JSONObject resultObj = (JSONObject) result;
                users.add(sharepointObjectBuilder.buildSharepointUser(resultObj));
            });
        }

        return users;
    }

    public JSONObject getDeletedBy(JSONObject deletedByObj) throws IOException {
        System.out.println("Searching for deleted by");

        JSONObject deferredObj = deletedByObj.getJSONObject("__deferred");
        String deletedByPath = deferredObj.getString("uri");

        HttpGet httpGet = sharepointRequest.createGetRequestObject(deletedByPath);
        HttpResponse response = getSharePointData(httpGet);
        sharepointResponseExceptionHandler.handleResponseFromStatusCode(response.getStatusLine().getStatusCode());
        JSONObject responseBody = new JSONObject(EntityUtils.toString(response.getEntity()));
        EntityUtils.consumeQuietly(response.getEntity());

        return responseBody;
    }

    public void downloadFileContent(JSONObject fileData, String currentSiteTitle, String fileName)  {
        System.out.println("Downloading content for " + fileName + " in separate thread...");

        /* we need to build a url from scratch here since the file urls don't follow the traditional api uris */
        String fileUrl = siteURI.getScheme()
                + "://" + siteURI.getHost()
                + "/" + fileData.getString("ServerRelativeUrl");

        fileUrl = fileUrl.replace(" ", "%20");

        HttpGet httpGet = new HttpGet(fileUrl);
        try {
            HttpEntity entity = httpClient.execute(httpGet).getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024 * 1024);

            IOUtils.copy(inputStream, buffer);

            byte[] fileBytes = buffer.toByteArray();

            sharepointFileContentBuilder.saveFile(fileBytes, currentSiteTitle, fileName);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpResponse getSharePointData(HttpGet httpGet) {
        try {
            return httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
