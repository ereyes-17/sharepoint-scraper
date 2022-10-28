package prototype.config;

import java.util.List;

public class SharepointConfig {
    protected String rootSite;
    protected String username;
    protected String password;
    protected String domain;
    protected final String SKIP_SUBSITES = "--skip=subsites";
    protected final String SKIP_LISTS = "--skip=lists";
    protected final String SKIP_FOLDERS = "--skip=folders";
    protected final String SKIP_FILES = "--skip=files";
    protected final String ODATA_PARAM = "application/json;odata=verbose";
    protected boolean noDownload;
    protected String beforeDate_listFilter;
    protected String afterDate_listFilter;
    protected String beforeDate_folderFilter;
    protected String afterDate_folderFilter;
    protected String beforeDate_fileFilter;
    protected String afterDate_fileFilter;
    protected List<String> orderBy_listCriteria;
    protected List<String> orderBy_folderCriteria;
    protected List<String> orderBy_fileCriteria;

    public SharepointConfig(String rootSite, String username, String password,
                            String domain, boolean noDownload) {
        this.rootSite = rootSite;
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.noDownload = noDownload;
    }

    public String getRootSite() {
        return rootSite;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDomain() {
        return domain;
    }

    public String getSKIP_SUBSITES() {
        return SKIP_SUBSITES;
    }

    public String getSKIP_LISTS() {
        return SKIP_LISTS;
    }

    public String getSKIP_FOLDERS() {
        return SKIP_FOLDERS;
    }

    public String getSKIP_FILES() {
        return SKIP_FILES;
    }
    public boolean getNoDownload() {
        return noDownload;
    }

    public String getODATA_PARAM() {
        return ODATA_PARAM;
    }

    public String getBeforeDate_listFilter() {
        return beforeDate_listFilter;
    }

    public void setBeforeDate_listFilter(String beforeDate_listFilter) {
        this.beforeDate_listFilter = beforeDate_listFilter;
    }

    public String getAfterDate_listFilter() {
        return afterDate_listFilter;
    }

    public void setAfterDate_listFilter(String afterDate_listFilter) {
        this.afterDate_listFilter = afterDate_listFilter;
    }

    public String getBeforeDate_folderFilter() {
        return beforeDate_folderFilter;
    }

    public void setBeforeDate_folderFilter(String beforeDate_folderFilter) {
        this.beforeDate_folderFilter = beforeDate_folderFilter;
    }

    public String getAfterDate_folderFilter() {
        return afterDate_folderFilter;
    }

    public void setAfterDate_folderFilter(String afterDate_folderFilter) {
        this.afterDate_folderFilter = afterDate_folderFilter;
    }

    public String getBeforeDate_fileFilter() {
        return beforeDate_fileFilter;
    }

    public void setBeforeDate_fileFilter(String beforeDate_fileFilter) {
        this.beforeDate_fileFilter = beforeDate_fileFilter;
    }

    public String getAfterDate_fileFilter() {
        return afterDate_fileFilter;
    }

    public void setAfterDate_fileFilter(String afterDate_fileFilter) {
        this.afterDate_fileFilter = afterDate_fileFilter;
    }

    public List<String> getOrderBy_listCriteria() {
        return orderBy_listCriteria;
    }

    public void setOrderBy_listCriteria(List<String> orderyBy_listCriteria) {
        this.orderBy_listCriteria = orderyBy_listCriteria;
    }

    public List<String> getOrderBy_folderCriteria() {
        return orderBy_folderCriteria;
    }

    public void setOrderBy_folderCriteria(List<String> orderBy_folderCriteria) {
        this.orderBy_folderCriteria = orderBy_folderCriteria;
    }

    public List<String> getOrderBy_fileCriteria() {
        return orderBy_fileCriteria;
    }

    public void setOrderBy_fileCriteria(List<String> orderBy_fileCriteria) {
        this.orderBy_fileCriteria = orderBy_fileCriteria;
    }
}
