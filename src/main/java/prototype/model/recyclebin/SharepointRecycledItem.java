package prototype.model.recyclebin;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.util.Map;

public class SharepointRecycledItem {
    protected String id;
    protected String deletedDate;
    protected String title;
    protected String directory;
    protected Map deletedByDetails;
    protected int size;

    public SharepointRecycledItem(String id, String deletedDate, String title,
                                  String directory, JSONObject deletedByDetails, int size) {
        this.id = id;
        this.deletedDate = deletedDate;
        this.title = title;
        this.directory = directory;
        this.deletedByDetails = new Gson().fromJson(String.valueOf(deletedByDetails), Map.class);
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDirectory() {
        return directory;
    }

    public int getSize() {
        return size;
    }

    public Map getDeletedByDetails() {
        return deletedByDetails;
    }
}
