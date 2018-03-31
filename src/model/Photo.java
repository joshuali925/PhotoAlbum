package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Photo {
    public String name;
    public String path;
    public String caption;
    public Map<String, List<String>> tag;
    public Calendar date;
    public Image thumbnail;
    public ImageView thumbView;

    public Photo(String path) throws FileNotFoundException {
        name = path;
        this.path = path;
        tag = new HashMap<String, List<String>>();
        FileInputStream input = new FileInputStream(path);
        thumbnail = new Image(input, 100, 0, false, false);
        thumbView = new ImageView(thumbnail);
    }

    public ImageView getThumbView() {
        return thumbView;
    }
    
    public String getPath() {
        return path;
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
