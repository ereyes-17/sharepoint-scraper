package prototype.model.folder;

import prototype.model.file.SharepointFile;
import prototype.model.metadata.SharepointMetadata;

import java.util.List;

public class SharepointFolder {
    protected String name;
    protected String relativeUrl;
    protected int itemCount;
    protected List<SharepointFile> files;
    protected List<SharepointFolder> subFolders;
    protected SharepointMetadata metadata;

    public SharepointFolder(String name, String relativeUrl, int itemCount,
                            List<SharepointFile> files, List<SharepointFolder> subFolders, SharepointMetadata metadata) {
        this.name = name;
        this.relativeUrl = relativeUrl;
        this.itemCount = itemCount;
        this.files = files;
        this.subFolders = subFolders;
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public int getItemCount() {
        return itemCount;
    }

    public List<SharepointFile> getFiles() {
        return files;
    }

    public List<SharepointFolder> getSubFolders() {
        return subFolders;
    }

    public SharepointMetadata getMetadata() {
        return metadata;
    }
}
