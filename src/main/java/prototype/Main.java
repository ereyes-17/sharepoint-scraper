package prototype;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import prototype.client.SharepointClient;
import prototype.config.SharepointConfig;
import prototype.model.site.SharepointSite;

import java.io.*;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Main {

    public static SharepointConfig sharepointConfig;
    public static SharepointSite site;
    private static String fileName;
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
        String timezoneOffset = "Z"; // default timezone is UTC

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
            updateConfigWithInputProperties(pathToFilterConfig);
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
        site = sharepointClient.collectSiteData();

        assert site != null;

        /* spit out the data */
        generateOutput(site);

        /* show where the output file is, if requested */
        if (fileName != null) {
            System.out.println("Output File: " + fileName);
        }

        /* display threads running at the end of the program */
        Set<Thread> currentThreads = Thread.getAllStackTraces().keySet();
        if (currentThreads.stream().anyMatch(thread -> thread.getName().contains("Thread-"))) {
            System.out.println("Caution when viewing results, some of the following threads may still be downloading files...");
            System.out.printf("%-15s \t %-15s \t %-15s \t %s\n", "NAME", "STATE", "PRIORITY", "IS_DAEMON");
            for (Thread t : currentThreads) {
                if (t.getName().contains("Thread-")) {
                    System.out.printf("%-15s \t %-15s \t %-15d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isDaemon());
                }
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
                    case "subfolders":
                        sharepointConfig.setSkipSubFolders(true);
                        break;
                    case "recycled":
                        sharepointConfig.setSkipRecycledItems(true);
                        break;
                    case "groups":
                        sharepointConfig.setSkipGroups(true);
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
            } else if (skipArgs.trim().equalsIgnoreCase("subfolders")) {
                sharepointConfig.setSkipSubFolders(true);
            } else if (skipArgs.trim().equalsIgnoreCase("recycled")) {
                sharepointConfig.setSkipRecycledItems(true);
            } else if (skipArgs.trim().equalsIgnoreCase("groups")) {
                sharepointConfig.setSkipGroups(true);
            } else {
                System.out.println(skipArgs + " is NOT a Sharepoint object.");
                System.exit(1);
            }
        }
    }


    private static void generateOutput(SharepointSite site) {
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
                fileName = outputFilePath;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateConfigWithInputProperties(String pathToFilterConfig) {
        try {
            /* Read the config file */
            String fileContentAsString = new String(Files.readAllBytes(Paths.get(pathToFilterConfig)));

            /* Parse file content into a json object */
            JSONObject jsonObject = new JSONObject(fileContentAsString);

            /* Retrieve the filters property */
            JSONObject filterObj = null;
            try {
                filterObj = jsonObject.getJSONObject("filter");
            } catch (JSONException ignored) {
                /* We don't really care here */
            }
            /* If we received a filter prop, let's see what the user wants to filter */
            if (filterObj != null) {
                try {
                    /* Check to see if there's a 'lists' prop */
                    JSONArray listFilterOptions = filterObj.getJSONArray("lists");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(listFilterOptions);
                    /* Update sharepointConfig */
                    sharepointConfig.setListFilterOptions(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
                /* Repeat the process for folders and files */
                try {
                    /* Check to see if there's a 'folders' prop */
                    JSONArray folderFilterOptions = filterObj.getJSONArray("folders");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(folderFilterOptions);
                    /* Update sharepointConfig */
                    sharepointConfig.setFolderFilterOptions(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
                try {
                    /* Check to see if there's a 'files' prop */
                    JSONArray fileFilterOptions = filterObj.getJSONArray("files");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(fileFilterOptions);
                    /* Update sharepointConfig */
                    sharepointConfig.setFileFilterOptions(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
            }

            /* Retrieve the order property */
            JSONObject orderObj = null;
            try {
                orderObj = jsonObject.getJSONObject("order");
            } catch (JSONException ignored) {
                /* We don't really care here */
            }
            /* If we received an order prop, let's see what order the user wants */
            if (orderObj != null) {
                try {
                    /* Check to see if there's a 'lists' prop */
                    JSONObject listOrderObj = orderObj.getJSONObject("lists");
                    /* Get the fields list */
                    JSONArray listOrderFields = listOrderObj.getJSONArray("fields");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(listOrderFields);
                    /* Update sharepointConfig */
                    sharepointConfig.setListOrderFields(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
                /* Repeat the process for folders and files */
                try {
                    /* Check to see if there's a 'lists' prop */
                    JSONObject folderOrderObj = orderObj.getJSONObject("folders");
                    /* Get the fields list */
                    JSONArray folderOrderFields = folderOrderObj.getJSONArray("fields");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(folderOrderFields);
                    /* Update sharepointConfig */
                    sharepointConfig.setFolderOrderFields(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
                try {
                    /* Check to see if there's a 'files' prop */
                    JSONObject filesOrderObj = orderObj.getJSONObject("files");
                    /* Get the fields list */
                    JSONArray filesOrderFields = filesOrderObj.getJSONArray("fields");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(filesOrderFields);
                    /* Update sharepointConfig */
                    sharepointConfig.setFileOrderFields(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
            }

            /* Retrieve the skip property */
            JSONObject skipObj = null;
            try {
                skipObj = jsonObject.getJSONObject("skip");
            } catch (JSONException ignored) {

            }

            /* If we received a skip prop, let's see what values are desired by the user */
            if (skipObj != null) {
                try {
                    /* Determine for lists */
                    int listSkip = skipObj.getInt("lists");
                    sharepointConfig.setSkipListValue(listSkip);
                } catch (JSONException ignored) {
                    sharepointConfig.setSkipListValue(-1);
                }
                try {
                    /* Determine for folders */
                    int foldersSkip = skipObj.getInt("folders");
                    sharepointConfig.setSkipFolderValue(foldersSkip);
                } catch (JSONException ignored) {
                    sharepointConfig.setSkipFolderValue(-1);
                }
                try {
                    /* Determine for files */
                    int fileSkip = skipObj.getInt("files");
                    sharepointConfig.setSkipFileValue(fileSkip);
                } catch (JSONException ignored) {
                    sharepointConfig.setSkipFileValue(-1);
                }
            }

            /* Retrieve the select property */
            JSONObject selectObj = null;
            try {
                selectObj = jsonObject.getJSONObject("select");
            } catch (JSONException ignored) {

            }

            /* If we received a select prop, let's see what fields the user desires */
            if (selectObj != null) {
                try {
                    JSONArray listSelectFields = selectObj.getJSONArray("lists");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(listSelectFields);
                    /* Update sharepointConfig */
                    sharepointConfig.setListSelectOptions(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
                /* Repeat the process for folders and files */
                try {
                    JSONArray folderSelectFields = selectObj.getJSONArray("folders");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(folderSelectFields);
                    /* Update sharepointConfig */
                    sharepointConfig.setFolderSelectOptions(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
                try {
                    JSONArray filesSelectFields = selectObj.getJSONArray("files");
                    /* Convert the json array to a List */
                    List<String> elements = convertJSONArrayToList(filesSelectFields);
                    /* Update sharepointConfig */
                    sharepointConfig.setFileSelectOptions(elements);
                } catch (JSONException ignored) {
                    /* We don't really care here */
                }
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
        System.out.println("    --skip=       : Skip a Sharepoint object. Options include 'subsites','lists','folders','files','subfolders'");
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
        System.out.println("  4. Skip lists, apply a configuration");
        System.out.println("    java -jar target/SharePointPrototype-0.1.9-jar-with-dependencies.jar http://my-sharepoint-site.com/sites/MySite sharepointUser sharepointPassword sharepointDomain --skip=lists --filter-conf=myFilter.json");
        System.out.println("    * Examples of json configs are in the config-examples folder.");
        System.out.println("    * You can apply multiple filters, just specify the values in the json config.");
        System.out.println("For any further questions, please contact sir Elijah Reyes (elijah.reyes@hitachivantarafederal.com) or please visit https://www.odata.org/documentation/odata-version-2-0/uri-conventions/#OrderBySystemQueryOption to review odata query options");
        System.exit(0);
    }
}
