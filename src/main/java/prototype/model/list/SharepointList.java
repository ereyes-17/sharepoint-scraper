package prototype.model.list;

import prototype.model.metadata.SharepointMetadata;

import java.util.List;

public class SharepointList {
    protected String id;
    protected String title;
    protected String createdDate;
    protected String lastItemModifiedDate;
    protected String lastItemDeletedDate;
    protected List<String> fields;
    protected List<SharepointListItem> items;
    protected boolean hidden;
    protected int itemCount;
    protected SharepointMetadata metadata;

    public SharepointList(String id, String title, String createdDate,
                          String lastItemModifiedDate, String lastItemDeletedDate,
                          List<String> fields, List<SharepointListItem> items,
                          boolean hidden, int itemCount, SharepointMetadata metadata) {
        this.id = id;
        this.title = title;
        this.createdDate = createdDate;
        this.lastItemModifiedDate = lastItemModifiedDate;
        this.lastItemDeletedDate = lastItemDeletedDate;
        this.fields = fields;
        this.items = items;
        this.hidden = hidden;
        this.itemCount = itemCount;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastItemModifiedDate() {
        return lastItemModifiedDate;
    }

    public String getLastItemDeletedDate() {
        return lastItemDeletedDate;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<SharepointListItem> getItems() {
        return items;
    }

    public boolean isHidden() {
        return hidden;
    }

    public SharepointMetadata getMetadata() {
        return metadata;
    }

    public int getItemCount() { return itemCount; }
}
