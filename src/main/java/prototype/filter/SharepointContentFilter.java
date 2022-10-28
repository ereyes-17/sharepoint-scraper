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
        String beforeDate = sharepointConfig.getBeforeDate_listFilter();
        String afterDate = sharepointConfig.getAfterDate_listFilter();

        if (beforeDate != null && afterDate == null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_LIST_DATE_BEFORE_FILTER
                    .replace("<beforeDate>", beforeDate);
        } else if (beforeDate == null && afterDate != null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_LIST_DATE_AFTER_FILTER
                    .replace("<afterDate>", afterDate);
        } else if (beforeDate != null && afterDate != null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_LIST_DATERANGE_FILTER
                    .replace("<beforeDate>", beforeDate)
                    .replace("<afterDate>", afterDate);
        }
        return sitePathUrl;
    }

    public String applyFilterToFolderPath(String sitePathUrl) {
        String beforeDate = sharepointConfig.getBeforeDate_folderFilter();
        String afterDate = sharepointConfig.getAfterDate_folderFilter();

        if (beforeDate != null && afterDate == null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_FOLDER_DATE_BEFORE_FILTER
                    .replace("<beforeDate>", beforeDate);
        } else if (beforeDate == null && afterDate != null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_FOLDER_DATE_AFTER_FILTER
                    .replace("<afterDate>", afterDate);
        } else if (beforeDate != null && afterDate != null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_FOLDER_DATERANGE_FILTER
                    .replace("<beforeDate>", beforeDate)
                    .replace("<afterDate>", afterDate);
        }
        return sitePathUrl;
    }

    public String applyFilterToFilePath(String sitePathUrl) {
        String beforeDate = sharepointConfig.getBeforeDate_fileFilter();
        String afterDate = sharepointConfig.getAfterDate_fileFilter();

        if (beforeDate != null && afterDate == null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_FILE_DATE_BEFORE_FILTER
                    .replace("<beforeDate>", beforeDate);
        } else if (beforeDate == null && afterDate != null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_FILE_DATE_AFTER_FILTER
                    .replace("<afterDate>", afterDate);
        } else if (beforeDate != null && afterDate != null) {
            return sitePathUrl + SharepointUrlConfig.SHAREPOINT_FILE_DATERANGE_FILTER
                    .replace("<beforeDate>", beforeDate)
                    .replace("<afterDate>", afterDate);
        }
        return sitePathUrl;
    }

    public String applyOrderToListPath(String sitePathUrl) {
        /* order odata query will be checked AFTER the filter query */
        List<String> listOrderCriteria = sharepointConfig.getOrderBy_listCriteria();
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
                return sitePathUrl + "&" + SharepointUrlConfig.SHAREPOINT_ORDER_BY_FILTER + orderString;
            } else {
                return sitePathUrl + SharepointUrlConfig.SHAREPOINT_ORDER_BY_FILTER + orderString;
            }
        }

        return sitePathUrl;
    }

    public String applyOrderToFolderPath(String sitePathUrl) {
        List<String> folderOrderCriteria = sharepointConfig.getOrderBy_folderCriteria();
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
            System.out.println("ORDER STRING: " + orderString);
            if (sitePathUrl.contains("$filter")) {
                return sitePathUrl + "&" + SharepointUrlConfig.SHAREPOINT_ORDER_BY_FILTER + orderString;
            } else {
                return sitePathUrl + SharepointUrlConfig.SHAREPOINT_ORDER_BY_FILTER + orderString;
            }
        }
        return sitePathUrl;
    }

    public String applyOrderToFilePath(String sitePathUrl) {
        List<String> fileOrderCriteria = sharepointConfig.getOrderBy_fileCriteria();
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
                return sitePathUrl + "&" + SharepointUrlConfig.SHAREPOINT_ORDER_BY_FILTER + orderString;
            } else {
                return sitePathUrl + SharepointUrlConfig.SHAREPOINT_ORDER_BY_FILTER + orderString;
            }
        }
        return sitePathUrl;
    }
}
