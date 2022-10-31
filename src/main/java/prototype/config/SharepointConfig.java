package prototype.config;

import java.util.List;

public class SharepointConfig {
    protected String rootSite;
    protected String username;
    protected String password;
    protected String domain;
    protected final String ODATA_PARAM = "application/json;odata=verbose";
    protected boolean noDownload;
    protected boolean skipSubsites = false;
    protected boolean skipLists = false;
    protected boolean skipFolders = false;
    protected boolean skipFiles = false;
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

    public boolean getNoDownload() {
        return noDownload;
    }

    public boolean isSkipSubsites() {
        return skipSubsites;
    }

    public void setSkipSubsites(boolean skipSubsites) {
        this.skipSubsites = skipSubsites;
    }

    public boolean isSkipLists() {
        return skipLists;
    }

    public void setSkipLists(boolean skipLists) {
        this.skipLists = skipLists;
    }

    public boolean isSkipFolders() {
        return skipFolders;
    }

    public void setSkipFolders(boolean skipFolders) {
        this.skipFolders = skipFolders;
    }

    public boolean isSkipFiles() {
        return skipFiles;
    }

    public void setSkipFiles(boolean skipFiles) {
        this.skipFiles = skipFiles;
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
