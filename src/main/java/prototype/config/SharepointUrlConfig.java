package prototype.config;

public class SharepointUrlConfig {
    public static String SHAREPOINT_API_PATH = "/_api/Web";
    public static String SHAREPOINT_WEBS_PATH = "/Webs";
    public static String SHAREPOINT_LISTS_PATH = "/lists";
    public static String SHAREPOINT_FOLDERS_PATH = "/folders";
    public static String SHAREPOINT_LIST_DATE_BEFORE_FILTER = "?$filter=LastItemModifiedDate%20lt%20datetime'<beforeDate>Z'";
    public static String SHAREPOINT_LIST_DATE_AFTER_FILTER = "?$filter=LastItemModifiedDate%20ge%20datetime'<afterDate>Z'";
    public static String SHAREPOINT_LIST_DATERANGE_FILTER = "?$filter=LastItemModifiedDate%20lt%20datetime'<beforeDate>Z'%20and%20LastItemModifiedDate%20ge%20datetime'<afterDate>Z'";
    public static String SHAREPOINT_FOLDER_DATE_BEFORE_FILTER = "?$filter=TimeLastModified%20lt%20datetime'<beforeDate>Z'";
    public static String SHAREPOINT_FOLDER_DATE_AFTER_FILTER = "?$filter=TimeLastModified%20ge%20datetime'<afterDate>Z'";
    public static String SHAREPOINT_FOLDER_DATERANGE_FILTER ="?$filter=TimeLastModified%20lt%20datetime'<beforeDate>Z'%20and%20TimeLastModified%20ge%20datetime'<afterDate>Z'";
    public static String SHAREPOINT_FILE_DATE_BEFORE_FILTER = "?$filter=TimeLastModified%20lt%20datetime'<beforeDate>Z'";
    public static String SHAREPOINT_FILE_DATE_AFTER_FILTER = "?$filter=TimeLastModified%20ge%20datetime'<afterDate>Z'";
    public static String SHAREPOINT_FILE_DATERANGE_FILTER ="?$filter=TimeLastModified%20lt%20datetime'<beforeDate>Z'%20and%20TimeLastModified%20ge%20datetime'<afterDate>Z'";
    public static String SHAREPOINT_ORDER_BY_FILTER = "?$orderby=";

}
