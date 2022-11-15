package prototype.builder;

import org.json.JSONException;
import org.json.JSONObject;
import prototype.client.SharepointClient;
import prototype.config.SharepointConfig;
import prototype.filter.SharepointContentFilter;
import prototype.model.file.SharepointFile;
import prototype.model.folder.SharepointFolder;
import prototype.model.list.SharepointList;
import prototype.model.list.SharepointListItem;
import prototype.model.membership.SharepointGroup;
import prototype.model.membership.SharepointUser;
import prototype.model.metadata.SharepointMetadata;
import prototype.model.recyclebin.SharepointRecycledItem;
import prototype.model.site.SharepointSite;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class SharepointObjectBuilder {
    
    protected SharepointClient sharepointClient;
    protected SharepointFileContentBuilder sharepointFileContentBuilder;
    protected URI siteURI;
    protected String currentSiteTitle;
    protected String objectToSkip;
    protected SharepointConfig sharepointConfig;
    protected SharepointContentFilter sharepointContentFilter;
    
    public SharepointObjectBuilder(SharepointClient sharepointClient,
                                   URI siteURI,
                                   SharepointConfig sharepointConfig) throws IOException {
        this.sharepointClient = sharepointClient;
        this.sharepointFileContentBuilder = new SharepointFileContentBuilder();
        this.siteURI = siteURI;
        this.sharepointConfig = sharepointConfig;
        this.sharepointContentFilter = new SharepointContentFilter(sharepointConfig);
    }
    public SharepointSite buildSharepointSite(JSONObject data) {
        System.out.println("Building site..." );

        String url = data.getString("Url");
        String id = data.getString("Id");
        String createdDate = data.getString("Created");
        String description = data.getString("Description");
        String title = data.getString("Title");

        /* for the SharepointObjectBuilder to reference */
        currentSiteTitle = title;

        List<SharepointList> lists = null;
        if (!sharepointConfig.isSkipLists()) {
            try {
                lists = sharepointClient.determineSiteLists(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<SharepointFolder> folders = null;
        if (!sharepointConfig.isSkipFolders()) {
            try {
                folders = sharepointClient.determineSiteFolders(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SharepointMetadata metadata = buildSharepointMetadata(data.getJSONObject("__metadata"));

        List<SharepointRecycledItem> recycledItems = null;
        if (!sharepointConfig.isSkipRecycledItems()) {
            try {
                recycledItems = sharepointClient.determineRecycledItems(data.getJSONObject("RecycleBin"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<SharepointGroup> sharepointGroups = null;
        if (!sharepointConfig.isSkipGroups()) {
            try {
                sharepointGroups = sharepointClient.determineGroups(data.getJSONObject("SiteGroups"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new SharepointSite(url, description, id, createdDate,
                title, lists, folders, metadata, recycledItems, sharepointGroups);
    }

    public SharepointList buildSharepointList(JSONObject data) throws IOException {
        System.out.println("Building list..." );

        String id = data.getString("Id");
        String title = data.getString("Title");
        String createdDate = data.getString("Created");
        String lastItemModifiedDate = data.getString("LastItemModifiedDate");
        String lastItemDeletedDate = data.getString("LastItemDeletedDate");
        List<SharepointListItem> items = sharepointClient.determineListItems(data.getJSONObject("Items"));
        boolean hidden = data.getBoolean("Hidden");
        int itemCount = data.getInt("ItemCount");

        SharepointMetadata metadata = buildSharepointMetadata(data.getJSONObject("__metadata"));

        return new SharepointList(id, title, createdDate, lastItemModifiedDate, lastItemDeletedDate,
                items, hidden, itemCount, metadata);
    }

    public SharepointListItem buildSharepointListItem(JSONObject data) {
        System.out.println("Building items..." );

        String title = null;

        try {
            title = data.get("Title").toString();
        } catch (JSONException ignored) {
            /* in some cases "Title" is null */
        }

        /* determine if the list item has any attachments */
        boolean hasAttachments = false;
        try {
             hasAttachments = data.getBoolean("Attachments");
        } catch (JSONException ignored) {

        }
        List<String> attachments = null;
        if (hasAttachments) {
            try {
                attachments = sharepointClient.discoverListItemAttachments(data.getJSONObject("AttachmentFiles"), currentSiteTitle);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SharepointMetadata metadata = buildSharepointMetadata(data.getJSONObject("__metadata"));

        return new SharepointListItem(title, data, attachments, metadata);
    }

    public SharepointFolder buildSharepointFolder(JSONObject data) {
        System.out.println("Building folder..." );

        String name = data.getString("Name");
        String relativeUrl = data.getString("ServerRelativeUrl");
        int itemCount = data.getInt("ItemCount");

        List<SharepointFile> files = null;
        if (!sharepointConfig.isSkipFiles()) {
            try {
                files = (itemCount == 0) ? null : sharepointClient.determineFiles(data.getJSONObject("Files"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<SharepointFolder> subFolders = null;
        if (!sharepointConfig.isSkipSubFolders()) {
            try {
                subFolders = sharepointClient.determineSubFolders(data.getJSONObject("Folders"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SharepointMetadata metadata = buildSharepointMetadata(data.getJSONObject("__metadata"));

        return new SharepointFolder(name, relativeUrl, itemCount, files, subFolders, metadata);
    }

    public SharepointFile buildSharepointFile(JSONObject data) throws IOException {
        System.out.println("Building file..." );

        String name = data.getString("Name");
        String comment = data.getString("CheckInComment");
        String createdDate = data.getString("TimeCreated");
        String lastModifiedDate = data.getString("TimeLastModified");
        String relativeUrl = data.getString("ServerRelativeUrl");
        int length = Integer.parseInt(data.getString("Length"));
        int majorVersion = data.getInt("MajorVersion");
        int minorVersion = data.getInt("MinorVersion");

        /* we'll download the file content in separate thread to avoid hanging up the API calls */
        if (!sharepointConfig.getNoDownload()) {
            new Thread(() ->
                    sharepointClient.downloadFileContent(
                            data,
                            currentSiteTitle,
                            name))
                    .start();
        }

        SharepointMetadata metadata = buildSharepointMetadata(data.getJSONObject("__metadata"));

        SharepointUser author = buildSharepointUser(data.getJSONObject("Author"));

        return new SharepointFile(name, comment, createdDate, lastModifiedDate, relativeUrl,
                length, majorVersion, minorVersion, metadata, author);
    }

    public SharepointMetadata buildSharepointMetadata(JSONObject metadataObj) {
        System.out.println("Building metadata..." );

        String id = metadataObj.getString("id");
        String uri = metadataObj.getString("uri");
        String type = metadataObj.getString("type");

        return new SharepointMetadata(id, uri, type);
    }

    public SharepointGroup buildSharepointGroup(JSONObject data) {
        System.out.println("Building group..." );

        String loginName = data.getString("LoginName");
        String title = data.getString("Title");
        String description = null;
        try {
            description = data.getString("Description");
        } catch (JSONException ignored) {

        }
        String ownerTitle = data.getString("OwnerTitle");
        boolean allowMembersEditMembership = data.getBoolean("AllowMembersEditMembership");
        boolean allowRequestToJoinLeave = data.getBoolean("AllowRequestToJoinLeave");
        boolean autoAcceptRequestToJoinLeave = data.getBoolean("AutoAcceptRequestToJoinLeave");
        boolean onlyAllowMembersViewMembership = data.getBoolean("OnlyAllowMembersViewMembership");

        List<SharepointUser> sharepointUsers = null;
        try {
            sharepointUsers = sharepointClient.determineGroupUsers(data.getJSONObject("Users"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SharepointGroup(loginName, title, description, ownerTitle, allowMembersEditMembership,
                allowRequestToJoinLeave, autoAcceptRequestToJoinLeave, onlyAllowMembersViewMembership, sharepointUsers);
    }

    public SharepointUser buildSharepointUser(JSONObject data) {
        System.out.println("Building user..." );

        String loginName = data.getString("LoginName");
        String title = data.getString("Title");
        String email = data.getString("Email");
        boolean isSiteAdmin = data.getBoolean("IsSiteAdmin");
        String nameIdIssuer = null;
        try {
            nameIdIssuer = data.getJSONObject("UserId").getString("NameIdIssuer");
        } catch (JSONException ignored) {
            /* in some cases "UserId" is null */
        }

        return new SharepointUser(loginName, title, email, isSiteAdmin, nameIdIssuer);
    }

    public SharepointRecycledItem buildSharepointRecycledItem(JSONObject data)  {
        System.out.println("Building recycled item..." );

        String id = data.getString("Id");
        String deletedDate = data.getString("DeletedDate");
        String title = data.getString("Title");
        String directory = data.getString("DirName");
        int size = data.getInt("Size");

        JSONObject deletedByDetails = null;
        try {
            deletedByDetails = sharepointClient.getDeletedBy(data.getJSONObject("DeletedBy"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SharepointRecycledItem(id, deletedDate, title, directory, deletedByDetails, size);
    }
}
