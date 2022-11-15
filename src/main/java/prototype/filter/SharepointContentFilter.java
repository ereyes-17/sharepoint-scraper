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

    public String applySkipToListPath(String sitePathUrl) {
        int skipVal = sharepointConfig.getSkipListValue();
        if (skipVal > 0) {
            if (sitePathUrl.contains("$filter") || sitePathUrl.contains("$orderby")) {
                sitePathUrl = sitePathUrl + "&?$skip=" + skipVal;
            } else {
                sitePathUrl = sitePathUrl + "?$skip=" + skipVal;
            }
            return sitePathUrl;
        }
        return sitePathUrl;
    }

    public String applySkipToFolderPath(String sitePathUrl) {
        int skipVal = sharepointConfig.getSkipFolderValue();
        if (skipVal > 0) {
            if (sitePathUrl.contains("$filter") || sitePathUrl.contains("$orderby")) {
                sitePathUrl = sitePathUrl + "&?$skip=" + skipVal;
            } else {
                sitePathUrl = sitePathUrl + "?$skip=" + skipVal;
            }
            return sitePathUrl;
        }
        return sitePathUrl;
    }

    public String applySkipToFilePath(String sitePathUrl) {
        int skipVal = sharepointConfig.getSkipFileValue();
        if (skipVal > 0) {
            if (sitePathUrl.contains("$filter") || sitePathUrl.contains("$orderby")) {
                sitePathUrl = sitePathUrl + "&?$skip=" + skipVal;
            } else {
                sitePathUrl = sitePathUrl + "?$skip=" + skipVal;
            }
            return sitePathUrl;
        }
        return sitePathUrl;
    }

    public String applySelectToListPath(String sitePathUrl) {
        List<String> listOrderCriteria = sharepointConfig.getListSelectOptions();
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
            if (sitePathUrl.contains("$")) {
                sitePathUrl = sitePathUrl + "&?$select=" + orderString;
            } else {
                sitePathUrl = sitePathUrl + "?$select=" + orderString;
            }
            return sitePathUrl;
        }

        return sitePathUrl;
    }

    public String applySelectToFolderPath(String sitePathUrl) {
        List<String> folderSelectCriteria = sharepointConfig.getFolderSelectOptions();
        StringBuilder stringBuilder = new StringBuilder();

        if (folderSelectCriteria != null) {
            for (String element : folderSelectCriteria) {
                if (folderSelectCriteria.indexOf(element) != folderSelectCriteria.size() - 1) {
                    stringBuilder.append(element).append(",");
                } else {
                    stringBuilder.append(element);
                }
            }
            String orderString = new String(stringBuilder);
            if (sitePathUrl.contains("$")) {
                sitePathUrl = sitePathUrl + "&?$select=" + orderString;
            } else {
                sitePathUrl = sitePathUrl + "?$select=" + orderString;
            }
            return sitePathUrl;
        }
        return sitePathUrl;
    }

    public String applySelectToFilePath(String sitePathUrl) {
        List<String> fileSelectCriteria = sharepointConfig.getFileSelectOptions();
        StringBuilder stringBuilder = new StringBuilder();

        if (fileSelectCriteria != null) {
            for (String element : fileSelectCriteria) {
                if (fileSelectCriteria.indexOf(element) != fileSelectCriteria.size() - 1) {
                    stringBuilder.append(element).append(",");
                } else {
                    stringBuilder.append(element);
                }
            }
            String orderString = new String(stringBuilder);
            if (sitePathUrl.contains("$")) {
                sitePathUrl = sitePathUrl + "&?$select=" + orderString;
            } else {
                sitePathUrl = sitePathUrl + "?$select=" + orderString;
            }
            return sitePathUrl;
        }
        return sitePathUrl;
    }

}
