package prototype.config;

import org.json.JSONArray;

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
    protected boolean skipSubFolders = false;
    protected List<String> listFilterOptions;
    protected List<String> folderFilterOptions;
    protected List<String> fileFilterOptions;
    protected List<String> listOrderFields;
    protected List<String> folderOrderFields;
    protected List<String> fileOrderFields;
    protected int skipListValue;
    protected int skipFolderValue;
    protected int skipFileValue;
    protected List<String> listSelectOptions;
    protected List<String> folderSelectOptions;
    protected List<String> fileSelectOptions;

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

    public boolean isSkipSubFolders() {
        return skipSubFolders;
    }

    public void setSkipSubFolders(boolean skipSubFolders) {
        this.skipSubFolders = skipSubFolders;
    }

    public String getODATA_PARAM() {
        return ODATA_PARAM;
    }

    public void setListFilterOptions(List<String> elements) {
        this.listFilterOptions = elements;
    }

    public List<String> getListFilterOptions() {
        return listFilterOptions;
    }

    public List<String> getFolderFilterOptions() {
        return folderFilterOptions;
    }

    public void setFolderFilterOptions(List<String> folderFilterOptions) {
        this.folderFilterOptions = folderFilterOptions;
    }

    public List<String> getFileFilterOptions() {
        return fileFilterOptions;
    }

    public void setFileFilterOptions(List<String> fileFilterOptions) {
        this.fileFilterOptions = fileFilterOptions;
    }

    public void setListOrderFields(List<String> elements) {
        this.listOrderFields = elements;
    }

    public void setRootSite(String rootSite) {
        this.rootSite = rootSite;
    }

    public List<String> getListOrderFields() {
        return listOrderFields;
    }

    public List<String> getFolderOrderFields() {
        return folderOrderFields;
    }

    public void setFolderOrderFields(List<String> folderOrderFields) {
        this.folderOrderFields = folderOrderFields;
    }

    public List<String> getFileOrderFields() {
        return fileOrderFields;
    }

    public void setFileOrderFields(List<String> fileOrderFields) {
        this.fileOrderFields = fileOrderFields;
    }

    public int getSkipListValue() {
        return skipListValue;
    }

    public void setSkipListValue(int skipListValue) {
        this.skipListValue = skipListValue;
    }

    public int getSkipFolderValue() {
        return skipFolderValue;
    }

    public void setSkipFolderValue(int skipFolderValue) {
        this.skipFolderValue = skipFolderValue;
    }

    public int getSkipFileValue() {
        return skipFileValue;
    }

    public void setSkipFileValue(int skipFileValue) {
        this.skipFileValue = skipFileValue;
    }

    public List<String> getListSelectOptions() {
        return listSelectOptions;
    }

    public void setListSelectOptions(List<String> listSelectOptions) {
        this.listSelectOptions = listSelectOptions;
    }

    public List<String> getFolderSelectOptions() {
        return folderSelectOptions;
    }

    public void setFolderSelectOptions(List<String> folderSelectOptions) {
        this.folderSelectOptions = folderSelectOptions;
    }

    public List<String> getFileSelectOptions() {
        return fileSelectOptions;
    }

    public void setFileSelectOptions(List<String> fileSelectOptions) {
        this.fileSelectOptions = fileSelectOptions;
    }
}
