package prototype.model.site;

import prototype.model.folder.SharepointFolder;
import prototype.model.list.SharepointList;
import prototype.model.membership.SharepointGroup;
import prototype.model.metadata.SharepointMetadata;
import prototype.model.recyclebin.SharepointRecycledItem;

import java.util.List;

public class SharepointSite {

    protected String url;
    protected String parentUrl;
    protected String description;
    protected String id;

    protected String createdDate;

    protected String title;
    protected List<SharepointList> lists;
    protected List<SharepointFolder> folders;
    protected SharepointMetadata metadata;
    protected List<SharepointRecycledItem> recycledItems;
    protected boolean isHomeSite;
    protected List<SharepointGroup> sharepointGroups;

    public SharepointSite(String url,
                          String description,
                          String id,
                          String createdDate,
                          String title,
                          List<SharepointList> lists,
                          List<SharepointFolder> folders,
                          SharepointMetadata metadata,
                          List<SharepointRecycledItem> recycledItems,
                          List<SharepointGroup> sharepointGroups) {
        this.url = url;
        this.parentUrl = this.determineParentUrl(url);
        this.description = description;
        this.id = id;
        this.createdDate = createdDate;
        this.title = title;
        this.lists = lists;
        this.folders = folders;
        this.metadata = metadata;
        this.recycledItems = recycledItems;
        this.isHomeSite = false;
        this.sharepointGroups = sharepointGroups;
    }

    private String determineParentUrl(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.reverse();

        String parentUrl = new String(stringBuilder);
        parentUrl = parentUrl.substring(parentUrl.indexOf("/"));

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(parentUrl);
        stringBuilder2.reverse();

        return new String(stringBuilder2);
    }

    public String getUrl() {
        return url;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getTitle() {
        return title;
    }

    public List<SharepointList> getLists() {
        return lists;
    }

    public List<SharepointFolder> getFolders() {
        return folders;
    }

    public SharepointMetadata getMetadata() {
        return metadata;
    }

    public List<SharepointRecycledItem> getRecycledItems() {
        return recycledItems;
    }

    public boolean isHomeSite() {
        return isHomeSite;
    }

    public void setIsHomeSite(boolean isHomeSite) {
        this.isHomeSite = isHomeSite;
    }

    public List<SharepointGroup> getSharepointGroups() {
        return sharepointGroups;
    }
}
