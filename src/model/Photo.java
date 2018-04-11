package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class Photo implements Serializable {
    private String name;
    private String path;
    private String caption;
    private long date;
    private transient ObservableList<Tag> tags = null;
    private ArrayList<Tag> tagsData = new ArrayList<Tag>();
    // private transient Image image;
    // private transient ImageView thumbnail;

    public Photo(String path, long date) {
        int index = Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/")) + 1;
        name = path.substring(index);
        this.path = path;
        caption = "";
        this.date = date;
    }

    /**
     * @return The observable list of tags
     */
    public ObservableList<Tag> getTags() {
        if (tags == null)
            tags = FXCollections.observableArrayList(tagsData);
        return tags;
    }

    /**
     * Add a new tag
     * 
     * @param key
     * @param value
     * @return True if not duplicate
     */
    public boolean addTag(String key, String value) {
        if (tags == null)
            tags = FXCollections.observableArrayList(tagsData);
        if (key.length() == 0 || value.length() == 0)
            return false;
        for (Tag tag : tags)
            if (tag.getKey().equals(key) && tag.getValue().equals(value))
                return false;
        Tag tag = new Tag(key, value);
        return tags.add(tag) && tagsData.add(tag);
    }

    /**
     * Remove a tag
     * 
     * @param tag
     * @return True if removed
     */
    public boolean removeTag(Tag tag) {
        if (tags == null)
            tags = FXCollections.observableArrayList(tagsData);
        return tags.remove(tag) && tagsData.remove(tag);
    }

    /**
     * Create a new image object on every call
     * 
     * @return Created new image
     */
    public Image getImage() {
        // if (image == null) {
        // try {
        // FileInputStream input = new FileInputStream(path);
        // image = new Image(input);
        // } catch (FileNotFoundException e) {
        // System.err.println("Image file not found.");
        // }
        // }
        // return image;

        try {
            FileInputStream input = new FileInputStream(path);
            return new Image(input);
        } catch (FileNotFoundException e) {
            System.err.println("Image file not found.");
        }
        return null;
    }

    /**
     * Create a new image view on every call
     * 
     * @return Created scaled image view
     */
    public ImageView getThumbnail() {
        // if (thumbnail == null) {
        // thumbnail = new ImageView(getImage());
        // thumbnail.setFitHeight(40);
        // thumbnail.setFitWidth(40);
        // thumbnail.setPreserveRatio(true);
        // }
        // return thumbnail;

        ImageView ret = new ImageView(getImage());
        ret.setFitHeight(40);
        ret.setFitWidth(40);
        ret.setPreserveRatio(true);
        return ret;
    }

    /**
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return date
     */
    public String getDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy HH:mm:ss");
        return dateFormatter.format(date);
    }

    /**
     * @return timestamp in milliseconds
     */
    public long getTimestamp() {
        return date;
    }

    /**
     * @return caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Set caption
     * 
     * @param caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return name;
    }

}
