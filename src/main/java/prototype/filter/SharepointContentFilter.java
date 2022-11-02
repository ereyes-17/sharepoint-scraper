package prototype.filter;

import org.json.JSONObject;
import prototype.config.SharepointConfig;
import prototype.config.SharepointUrlConfig;

import java.util.Arrays;
import java.util.List;

public class SharepointContentFilter {
    protected SharepointConfig sharepointConfig;
    protected final List<String> IGNORED_DATA = Arrays.asList("Nintex", "Workflow", "What's");

    public SharepointContentFilter(SharepointConfig sharepointConfig) {
        this.sharepointConfig = sharepointConfig;
    }

    public boolean unwantedInTitle(JSONObject data) {
        boolean detected = false;

        String title = data.getString("Title").toLowerCase();
        for (String ignoredItem : IGNORED_DATA) {
            if (title.contains(ignoredItem.toLowerCase())) {
                detected = true;
                break;
            }
        }
        return detected;
    }

    public boolean unwantedInName(JSONObject data) {
        boolean detected = false;

        String name = data.getString("Name").toLowerCase();
        for (String ignoredItem : IGNORED_DATA) {
            if (name.contains(ignoredItem.toLowerCase())) {
                detected = true;
                break;
            }
        }
        return detected;
    }

    public boolean unwantedInUrl(JSONObject data) {
        boolean detected = false;

        String url = data.getString("Url").toLowerCase();
        for (String ignoredItem : IGNORED_DATA) {
            if (url.contains(ignoredItem.toLowerCase())) {
                detected = true;
                break;
            }
        }
        return detected;
    }

    public boolean unwantedInMetadata(JSONObject data) {
        boolean detected = false;

        JSONObject metadata = data.getJSONObject("__metadata");
        String uri = metadata.getString("uri").toLowerCase();
        String id = metadata.getString("id");

        for (String ignoredItem : IGNORED_DATA) {
            if (uri.contains(ignoredItem.toLowerCase()) || id.contains(ignoredItem.toLowerCase())) {
                detected = true;
                break;
            }
        }

        return detected;
    }

    public boolean unwantedInServerRelativeUrl(JSONObject data) {
        boolean detected = false;

        String url = data.getString("ServerRelativeUrl").toLowerCase();
        for (String ignoredItem : IGNORED_DATA) {
            if (url.contains(ignoredItem.toLowerCase())) {
                detected = true;
                break;
            }
        }
        return detected;
    }

    public String applyFilterToListPath(String sitePathUrl) {
        List<String> listFilterOptions = sharepointConfig.getListFilterOptions();
        if (listFilterOptions != null) {
            StringBuilder sitePathUrlBuilder = new StringBuilder("?$filter=");
            for (String element : listFilterOptions) {
                if (listFilterOptions.indexOf(element) != listFilterOptions.size() - 1) {
                    sitePathUrlBuilder
                            .append(element)
                            .append(" ")
                            .append("and")
                            .append(" ");
                } else {
                    sitePathUrlBuilder.append(element);
                }
            }
            /* Replace any spaces with %20 */
            final String urlFilterString = sitePathUrlBuilder.toString().replace(" ", "%20");
            sitePathUrl = sitePathUrl + urlFilterString;
        }
        return sitePathUrl;
    }

    public String applyFilterToFolderPath(String sitePathUrl) {
        List<String> folderFilterOptions = sharepointConfig.getFolderFilterOptions();
        if (folderFilterOptions != null) {
            StringBuilder sitePathUrlBuilder = new StringBuilder("?$filter=");
            for (String element : folderFilterOptions) {
                if (folderFilterOptions.indexOf(element) != folderFilterOptions.size() - 1) {
                    sitePathUrlBuilder
                            .append(element)
                            .append(" ")
                            .append("and")
                            .append(" ");
                } else {
                    sitePathUrlBuilder.append(element);
                }
            }
            /* Replace any spaces with %20 */
            final String urlFilterString = sitePathUrlBuilder.toString().replace(" ", "%20");
            sitePathUrl = sitePathUrl + urlFilterString;
        }
        return sitePathUrl;
    }

    public String applyFilterToFilePath(String sitePathUrl) {
        List<String> fileFilterOptions = sharepointConfig.getFileFilterOptions();
        if (fileFilterOptions != null) {
            StringBuilder sitePathUrlBuilder = new StringBuilder("?$filter=");
            for (String element : fileFilterOptions) {
                if (fileFilterOptions.indexOf(element) != fileFilterOptions.size() - 1) {
                    sitePathUrlBuilder
                            .append(element)
                            .append(" ")
                            .append("and")
                            .append(" ");
                } else {
                    sitePathUrlBuilder.append(element);
                }
            }
            /* Replace any spaces with %20 */
            final String urlFilterString = sitePathUrlBuilder.toString().replace(" ", "%20");
            sitePathUrl = sitePathUrl + urlFilterString;
        }
        return sitePathUrl;
    }

    public String applyOrderToListPath(String sitePathUrl) {
        /* order odata query will be checked AFTER the filter query */
        List<String> listOrderCriteria = sharepointConfig.getListOrderFields();
        StringBuilder stringBuilder = new StringBuilder();

        if (listOrderCriteria != null) {
            for (String element : listOrderCriteria) {
                if (listOrderCriteria.indexOf(element) != listOrderCriteria.size() - 1) {
                    stringBuilder.append(element).append(",");
                } else {
                    stringBuilder.append(element);
                }
            }
            String orderString = new String(stringBuilder);
            if (sitePathUrl.contains("$filter")) {
                sitePathUrl = sitePathUrl + "&?$orderby=" + orderString;
            } else {
                sitePathUrl = sitePathUrl + "?$orderby=" + orderString;
            }
            return sitePathUrl;
        }

        return sitePathUrl;
    }

    public String applyOrderToFolderPath(String sitePathUrl) {
        List<String> folderOrderCriteria = sharepointConfig.getFolderOrderFields();
        StringBuilder stringBuilder = new StringBuilder();

        if (folderOrderCriteria != null) {
            for (String element : folderOrderCriteria) {
                if (folderOrderCriteria.indexOf(element) != folderOrderCriteria.size() - 1) {
                    stringBuilder.append(element).append(",");
                } else {
                    stringBuilder.append(element);
                }
            }
            String orderString = new String(stringBuilder);
            if (sitePathUrl.contains("$filter")) {
                sitePathUrl = sitePathUrl + "&?$orderby=" + orderString;
            } else {
                sitePathUrl = sitePathUrl + "?$orderby=" + orderString;
            }
            return sitePathUrl;
        }
        return sitePathUrl;
    }

    public String applyOrderToFilePath(String sitePathUrl) {
        List<String> fileOrderCriteria = sharepointConfig.getFileOrderFields();
        StringBuilder stringBuilder = new StringBuilder();

        if (fileOrderCriteria != null) {
            for (String element : fileOrderCriteria) {
                if (fileOrderCriteria.indexOf(element) != fileOrderCriteria.size() - 1) {
                    stringBuilder.append(element).append(",");
                } else {
                    stringBuilder.append(element);
                }
            }
            String orderString = new String(stringBuilder);
            if (sitePathUrl.contains("$filter")) {
                sitePathUrl = sitePathUrl + "&?$orderby=" + orderString;
            } else {
                sitePathUrl = sitePathUrl + "?$orderby=" + orderString;
            }
            return sitePathUrl;
        }
        return sitePathUrl;
    }
}
