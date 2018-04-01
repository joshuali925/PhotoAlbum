package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Photo {
    public String name;
    public String path;
    public String caption;
    public ObservableList<Tag> tags;
    public long date;
    public Image image;
    public ImageView thumbnail;
    public SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy HH:mm:ss");

    public Photo(String path, long date) throws FileNotFoundException {
        int index = Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/")) + 1;
        name = path.substring(index);
        this.path = path;
        this.caption = "";
        this.date = date;
        tags = FXCollections.observableArrayList();
        FileInputStream input = new FileInputStream(path);
        image = new Image(input);
        thumbnail = new ImageView(image);
        thumbnail.setFitHeight(40);
        thumbnail.setFitWidth(40);
        thumbnail.setPreserveRatio(true);
    }

    public ObservableList<Tag> getTags() {
        return tags;
    }

    public boolean addTag(String key, String value) {
        if (key.length() == 0 || value.length() == 0)
            return false;
        for (Tag tag : tags)
            if (tag.getKey().equals(key) && tag.getValue().equals(value))
                return false;
        return tags.add(new Tag(key, value));
    }

    public boolean removeTag(Tag tag) {
        return tags.remove(tag);
    }

    public Image getImage() {
        return image;
    }
    
    public ImageView getThumbnail() {
        return thumbnail;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
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
