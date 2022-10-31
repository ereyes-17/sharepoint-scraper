package prototype;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import prototype.client.SharepointClient;
import prototype.config.SharepointConfig;
import prototype.model.site.SharepointSite;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Main {

    public static SharepointConfig sharepointConfig;
    public static List<SharepointSite> sites;
    private static List<String> fileNames;
    private static boolean outputInformation = false;

    public static void main(String[] args) throws URISyntaxException, IOException, NoSuchAlgorithmException,
            KeyStoreException, KeyManagementException {

        /* Obtain required arguments */
        String rootSite = args[0];
        /* Let's quickly check if the user needs help! */
        if (rootSite.equalsIgnoreCase("-h") || rootSite.equalsIgnoreCase("--help")) {
            help();
        }
        final String username = args[1];
        final String password = args[2];
        final String domain = args[3];

        /* optional arguments */
        String[] optionalArguments = Arrays.copyOfRange(args, 3, args.length);
        boolean skipSubObject = false;
        String skipArgs = null;
        boolean noDownload = false;
        String pathToFilterConfig = null;

        /* determine which optional arguments were passed in */
        for (String argument : optionalArguments) {
            if (argument.contains("--output=")) {
                outputInformation = argument.split("=")[1].equalsIgnoreCase("true");
            }
            if (argument.contains("--skip=")) {
                skipSubObject = true;
                skipArgs = argument.split("=")[1];
            }
            if (argument.equalsIgnoreCase("--no-download")) {
                noDownload = true;
            }
            if (argument.contains("--filter-conf=")) {
                pathToFilterConfig = argument.split("=")[1];
            }
        }

        /* Initialize the configuration class */
        sharepointConfig = new SharepointConfig(rootSite, username, password, domain, noDownload);
        /* Before initializing the client, let's check if any configuration files are needed */
        if (pathToFilterConfig != null) {
            /* Update sharepointConfig */
            updateConfigForDateFilter(pathToFilterConfig);
        }
        /* Let's also update the skip objects to the config */
        if (skipSubObject) {
            updateConfigWithSkipArgs(skipArgs);
        }

        SharepointClient sharepointClient = new SharepointClient(sharepointConfig);

        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Arguments provided: ");
        System.out.println("    ROOT SITE: " + rootSite);
        System.out.println("     USERNAME: " + username);
        System.out.println("     PASSWORD: " + password);
        System.out.println("       DOMAIN: " + domain);
        System.out.println("       OUTPUT: " + outputInformation);
        System.out.println("  SKIP OBJECT: " + skipSubObject + " --> " + ((skipSubObject) ? skipArgs : null));
        System.out.println("FILE DOWNLOAD: " + !noDownload);
        System.out.println("-----------------------------------------------------------------------------");

        /* call the client to collect data */
        sites = sharepointClient.collectSiteData();

        assert sites != null;

        fileNames = new ArrayList<>();

        /* spit out the data */
        sites.forEach(Main::generateOutputFiles);

        if (!fileNames.isEmpty()) {
            fileNames.forEach(fileName -> {
                System.out.println("Output File: " + fileName);
            });
        }

        /* display threads running at the end of the program */
        Set<Thread> currentThreads = Thread.getAllStackTraces().keySet();
        if (currentThreads.size() > 4) {
            System.out.println("Caution when viewing results, some of the following threads may still be downloading files...");
            System.out.printf("%-15s \t %-15s \t %-15s \t %s\n", "NAME", "STATE", "PRIORITY", "IS_DAEMON");
            for (Thread t : currentThreads) {
                System.out.printf("%-15s \t %-15s \t %-15d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isDaemon());
            }
        }
    }

    private static void updateConfigWithSkipArgs(String skipArgs) {
        if (skipArgs.contains(",")) {
            String[] sharepointObjects = skipArgs.split(",");
            for (String sharepointObj : sharepointObjects) {
                switch (sharepointObj.trim()) {
                    case "subsites":
                        sharepointConfig.setSkipSubsites(true);
                        break;
                    case "lists":
                        sharepointConfig.setSkipLists(true);
                        break;
                    case "folders":
                        sharepointConfig.setSkipFolders(true);
                        break;
                    case "files":
                        sharepointConfig.setSkipFiles(true);
                        break;
                    default:
                        System.out.println(sharepointObj + " is NOT a Sharepoint object.");
                        System.exit(1);
                }
            }
        } else {
            if (skipArgs.trim().equalsIgnoreCase("subsites")) {
                sharepointConfig.setSkipSubsites(true);
            } else if (skipArgs.trim().equalsIgnoreCase("lists")) {
                sharepointConfig.setSkipLists(true);
            }
            else if (skipArgs.trim().equalsIgnoreCase("folders")) {
                sharepointConfig.setSkipFolders(true);
            }
            else if (skipArgs.trim().equalsIgnoreCase("files")) {
                sharepointConfig.setSkipFiles(true);
            } else {
                System.out.println(skipArgs + " is NOT a Sharepoint object.");
                System.exit(1);
            }
        }
    }


    private static void generateOutputFiles(SharepointSite site) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String siteObjectAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(site);
            System.out.println(siteObjectAsString);
            if (outputInformation) {
                String outputFilePath = System.getProperty("user.dir")
                        + File.separator
                        + site.getTitle().replace(" ", "")
                        + ".json";
                File outputFile = new File(outputFilePath);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                byte[] siteObjectAsBytes = siteObjectAsString.getBytes();
                fileOutputStream.write(siteObjectAsBytes);
                fileOutputStream.close();
                fileNames.add(outputFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateConfigForDateFilter(String pathToFilterConfig) {
        try {
            String fileContentAsString = new String(Files.readAllBytes(Paths.get(pathToFilterConfig)));

            JSONObject jsonObject = new JSONObject(fileContentAsString);
            JSONObject filterObj = jsonObject.getJSONObject("filter");

            JSONObject listsObj = filterObj.getJSONObject("lists");
            try {
                sharepointConfig.setBeforeDate_listFilter(listsObj.getString("BeforeDate_itemModified"));
            } catch (JSONException ignored) {
                sharepointConfig.setBeforeDate_listFilter(null);
            }
            try {
                sharepointConfig.setAfterDate_listFilter(listsObj.getString("AfterDate_itemModified"));
            } catch (JSONException ignored) {
                sharepointConfig.setAfterDate_listFilter(null);
            }

            JSONObject foldersObj = filterObj.getJSONObject("folders");
            try {
                sharepointConfig.setBeforeDate_folderFilter(foldersObj.getString("BeforeDate_Modified"));
            } catch (JSONException ignored) {
                sharepointConfig.setBeforeDate_folderFilter(null);
            }
            try {
                sharepointConfig.setAfterDate_folderFilter(foldersObj.getString("AfterDate_Modified"));
            } catch (JSONException ignored) {
                sharepointConfig.setAfterDate_folderFilter(null);
            }

            JSONObject filesObj = filterObj.getJSONObject("files");
            try {
                sharepointConfig.setBeforeDate_fileFilter(filesObj.getString("BeforeDate_Modified"));
            } catch (JSONException ignored) {
                sharepointConfig.setBeforeDate_fileFilter(null);
            }
            try {
                sharepointConfig.setAfterDate_fileFilter(filesObj.getString("AfterDate_Modified"));
            } catch (JSONException ignored) {
                sharepointConfig.setAfterDate_fileFilter(null);
            }

            JSONObject orderByObject = jsonObject.getJSONObject("orderBy");

            try {
                JSONArray listOrderElements = orderByObject.getJSONArray("lists");
                List<String> elements = convertJSONArrayToList(listOrderElements);
                if (!elements.isEmpty()) {
                    sharepointConfig.setOrderBy_listCriteria(elements);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                sharepointConfig.setOrderBy_listCriteria(null);
            }

            try {
                JSONArray folderOrderElements = orderByObject.getJSONArray("folders");
                List<String> elements = convertJSONArrayToList(folderOrderElements);
                if (!elements.isEmpty()) {
                    sharepointConfig.setOrderBy_folderCriteria(elements);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                sharepointConfig.setOrderBy_folderCriteria(null);
            }

            try {
                JSONArray fileOrderElements = orderByObject.getJSONArray("files");
                List<String> elements = convertJSONArrayToList(fileOrderElements);
                if (!elements.isEmpty()) {
                    sharepointConfig.setOrderBy_fileCriteria(elements);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                sharepointConfig.setOrderBy_fileCriteria(null);
            }

            System.out.println("Filter settings applied to the configuration...");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<String> convertJSONArrayToList(JSONArray jsonArray) {
        List<String> elements = new ArrayList<>();
        for (Object o : jsonArray) {
            System.out.println(o.toString());
            try {
                elements.add(o.toString());
            } catch (NullPointerException ignored) {
                /* Could be null, so just ignore */
            }
        }
        return elements;
    }

    private static void help() {
        System.out.println("Usage:");
        System.out.println("  Required arguments (positional):");
        System.out.println("    site    : Sharepoint site url");
        System.out.println("    username: Sharepoint username");
        System.out.println("    password: Sharepoint password");
        System.out.println("    domain  : Sharepoint domain");
        System.out.println("  Optional arguments (any order):");
        System.out.println("    --skip=       : Skip a Sharepoint object. Options include 'subsites','lists','folders','files'");
        System.out.println("                  : You may skip multiple objects. Look at example 3.");
        System.out.println("    --output=     : Do you want output files? Options include 'true', 'false' (default is false)");
        System.out.println("    --no-download : Do you want to fetch discovered files? Default is false, add argument to make true");
        System.out.println("    --filter-conf=: Include a filter (date-range): Path to the .json file is required");
        System.out.println("EXAMPLES: ");
        System.out.println("  1. Collect all site data (includes file download)");
        System.out.println("    java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain");
        System.out.println("  2. Skip subsites, don't download files");
        System.out.println("    java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=subsites --no-download");
        System.out.println("  3. Skip subsites and folders. (As a result, no files get downloaded because we're skipping folders)");
        System.out.println("    java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=subsites,folders");
        System.out.println("  4. Skip lists, apply AfterDate_Modified filter to files (.json config included after command)");
        System.out.println("    java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=lists --filter-conf=myFilter.json");
        System.out.println("    myFilter.json content: ");
        System.out.println("    {\n" +
                "  \"filter\": {\n" +
                "    \"lists\": {\n" +
                "      \"BeforeDate_itemModified\" : null,\n" +
                "      \"AfterDate_itemModified\": null\n" +
                "    },\n" +
                "    \"folders\": {\n" +
                "      \"BeforeDate_Modified\" : null,\n" +
                "      \"AfterDate_Modified\": null\n" +
                "    },\n" +
                "    \"files\": {\n" +
                "      \"BeforeDate_Modified\" : null,\n" +
                "      \"AfterDate_Modified\": \"2022-10-24T23:59:59\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        System.out.println("  5. Collect all data, apply date range filter to lists (.json config included after command)");
        System.out.println("    java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --filter-conf=myFilter.json");
        System.out.println("    myFilter.json content: ");
        System.out.println("    {\n" +
                "  \"filter\": {\n" +
                "    \"lists\": {\n" +
                "      \"BeforeDate_itemModified\" : \"2022-10-24T23:59:59\",\n" +
                "      \"AfterDate_itemModified\": \"2022-10-22T23:59:59\"\n" +
                "    },\n" +
                "    \"folders\": {\n" +
                "      \"BeforeDate_Modified\" : null,\n" +
                "      \"AfterDate_Modified\": null\n" +
                "    },\n" +
                "    \"files\": {\n" +
                "      \"BeforeDate_Modified\" : null,\n" +
                "      \"AfterDate_Modified\": null\n" +
                "    }\n" +
                "  }\n" +
                "}");
        System.out.println("Note that you can apply multiple filters, just specify the values in the json config.");
        System.out.println("For any further questions, please contact sir Elijah Reyes (elijah.reyes@hitachivantarafederal.com)");
        System.exit(0);
    }
}
