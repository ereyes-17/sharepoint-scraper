package prototype.model.list;

import com.google.gson.Gson;
import org.json.JSONObject;
import prototype.model.metadata.SharepointMetadata;

import java.util.List;
import java.util.Map;

public class SharepointListItem {
    protected String title;
    protected Map data;
    protected List<String> attachments;
    protected SharepointMetadata metadata;

    public SharepointListItem(String title, JSONObject data, List<String> attachments, SharepointMetadata metadata) {
        this.title = title;
        this.data = new Gson().fromJson(String.valueOf(data), Map.class);
        this.attachments = attachments;
        this.metadata = metadata;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public SharepointMetadata getMetadata() {
        return metadata;
    }
}