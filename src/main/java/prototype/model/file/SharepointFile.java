package prototype.model.file;

import prototype.model.membership.SharepointUser;
import prototype.model.metadata.SharepointMetadata;

public class SharepointFile {
    protected String name;
    protected String comment;
    protected String createdDate;
    protected String lastModifiedDate;
    protected String relativeUrl;
    protected int length;
    protected int majorVersion;
    protected int minorVersion;
    protected SharepointMetadata metadata;
    protected SharepointUser author;

    public SharepointFile(String name, String comment, String createdDate,
                          String lastModifiedDate, String relativeUrl, int length,
                          int majorVersion, int minorVersion, SharepointMetadata metadata,
                          SharepointUser author) {
        this.name = name;
        this.comment = comment;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.relativeUrl = relativeUrl;
        this.length = length;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.metadata = metadata;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public int getLength() {
        return length;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public SharepointMetadata getMetadata() {
        return metadata;
    }

    public SharepointUser getAuthor() {
        return author;
    }
}
