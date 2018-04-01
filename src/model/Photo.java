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

public class Photo implements Serializable {
    private String name;
    private String path;
    private String caption;
    private transient ObservableList<Tag> tags = null;
    private ArrayList<Tag> tagsData = new ArrayList<Tag>();
    private long date;
    private transient Image image;
    private transient ImageView thumbnail;

    public Photo(String path, long date) {
        int index = Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/")) + 1;
        name = path.substring(index);
        this.path = path;
        this.caption = "";
        this.date = date;
        try {
            FileInputStream input = new FileInputStream(path);
            image = new Image(input);
            thumbnail = new ImageView(image);
            thumbnail.setFitHeight(40);
            thumbnail.setFitWidth(40);
            thumbnail.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            System.err.println("Photo not found.");
        }
    }

    public ObservableList<Tag> getTags() {
        if (tags == null)
            tags = FXCollections.observableArrayList(tagsData);
        return tags;
    }

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

    public boolean removeTag(Tag tag) {
        if (tags == null)
            tags = FXCollections.observableArrayList(tagsData);
        return tags.remove(tag) && tagsData.remove(tag);
    }

    public Image getImage() {
        if (image == null) {
            try {
                FileInputStream input = new FileInputStream(path);
                image = new Image(input);
            } catch (FileNotFoundException e) {
                System.err.println("Image file not found.");
            }
        }
        return image;
    }

    public ImageView getThumbnail() {
        if (thumbnail == null) {
            thumbnail = new ImageView(getImage());
            thumbnail.setFitHeight(40);
            thumbnail.setFitWidth(40);
            thumbnail.setPreserveRatio(true);
        }
        return thumbnail;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy HH:mm:ss");
        return dateFormatter.format(date);
    }

    public long getTimestamp() {
        return date;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return name;
    }

}
