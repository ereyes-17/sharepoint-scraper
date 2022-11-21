package prototype.model.site;

import prototype.model.folder.SharepointFolder;
import prototype.model.list.SharepointList;
import prototype.model.membership.SharepointGroup;
import prototype.model.membership.SharepointUser;
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
    protected SharepointUser author;
    protected List<SharepointGroup> sharepointGroups;
    protected List<SharepointSite> subsites;

    public SharepointSite(String url,
                          String description,
                          String id,
                          String createdDate,
                          String title,
                          List<SharepointList> lists,
                          List<SharepointFolder> folders,
                          SharepointMetadata metadata,
                          List<SharepointRecycledItem> recycledItems,
                          SharepointUser author,
                          List<SharepointGroup> sharepointGroups,
                          List<SharepointSite> subsites) {
        this.url = url;
        this.parentUrl = this.determineParentUrl(url);
        this.description = description;
        this.id = id;
        this.createdDate = createdDate;
        this.title = title.replace("/", "_").replace("+", "-");
        this.lists = lists;
        this.folders = folders;
        this.metadata = metadata;
        this.recycledItems = recycledItems;
        this.author = author;
        this.sharepointGroups = sharepointGroups;
        this.subsites = subsites;
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

    public SharepointUser getAuthor() {
        return author;
    }

    public List<SharepointGroup> getSharepointGroups() {
        return sharepointGroups;
    }

    public List<SharepointSite> getSubsites() {
        return subsites;
    }
}
