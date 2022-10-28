package prototype.model.metadata;

public class SharepointMetadata {
    protected String id;
    protected String uri;
    protected String type;

    public SharepointMetadata(String id, String uri, String type) {
        this.id = id;
        this.uri = uri;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return type;
    }
}
