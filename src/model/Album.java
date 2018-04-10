package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class Album implements Serializable {
    private String name;
    private User user;
    private transient ObservableList<Photo> photoList = null;
    private ArrayList<Photo> photoListData = new ArrayList<Photo>();
    private HashSet<String> existingPaths = new HashSet<String>();

    public Album(String name, User user) {
        this.name = name;
        this.user = user;
    }

    /**
     * @return Album name
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return User of the album
     */
    public User getUser() {
        return user;
    }

    /**
     * @return Data range of the album
     */
    public String getRange() {
        if (photoListData.size() == 0)
            return "";
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
        for (Photo photo : photoListData) {
            long curr = photo.getTimestamp();
            if (curr > max)
                max = curr;
            if (curr < min)
                min = curr;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("M/d/yyyy");
        return dateFormatter.format(min) + " - " + dateFormatter.format(max);
    }

    /**
     * @return Number of photos in the album
     */
    public String getPhotoNumber() {
        return photoListData.size() + "";
    }

    /**
     * @return The observable list of photos
     */
    public ObservableList<Photo> getPhotoList() {
        if (photoList == null)
            photoList = FXCollections.observableArrayList(photoListData);
        return photoList;
    }

    /**
     * Add photo
     * 
     * @param path
     * @param date
     * @return True if not duplicate
     */
    public boolean addPhoto(String path, long date) {
        if (photoList == null)
            photoList = FXCollections.observableArrayList(photoListData);
        Photo photo = new Photo(path, date);
        return existingPaths.add(path) && photoList.add(photo) && photoListData.add(photo);
    }

    /**
     * Add photo
     * 
     * @param photo
     * @return True if not duplicate
     */
    public boolean addPhoto(Photo photo) {
        if (photoList == null)
            photoList = FXCollections.observableArrayList(photoListData);
        return existingPaths.add(photo.getPath()) && photoList.add(photo) && photoListData.add(photo);
    }

    /**
     * Delete photo
     * 
     * @param photo
     * @return True if deleted
     */
    public boolean deletePhoto(Photo photo) {
        if (photoList == null)
            photoList = FXCollections.observableArrayList(photoListData);
        return existingPaths.remove(photo.getPath()) && photoList.remove(photo) && photoListData.remove(photo);
    }

    @Override
    public String toString() {
        return name;
    }
}
