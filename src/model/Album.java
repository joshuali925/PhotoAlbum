package model;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Album {
    public String name;
    public User user;
    public ObservableList<Photo> photoList = FXCollections.observableArrayList();
    public Set<String> existingPaths = new HashSet<String>();

    public Album(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRange() {
        if (photoList.size() == 0)
            return "";
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
        for (Photo photo : photoList) {
            long curr = photo.getTimestamp();
            if (curr > max)
                max = curr;
            if (curr < min)
                min = curr;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("M/dd/yyyy");
        return dateFormatter.format(min) + " - " + dateFormatter.format(max);
    }

    public String getPhotoNumber() {
        return photoList.size() + "";
    }

    public ObservableList<Photo> getPhotoList() {
        return photoList;
    }

    public boolean addPhoto(String path, long date) throws FileNotFoundException {
        return existingPaths.add(path) && photoList.add(new Photo(path, date));
    }

    public boolean deletePhoto(Photo photo) {
        return existingPaths.remove(photo.getPath()) && photoList.remove(photo);
    }

    @Override
    public String toString() {
        return name;
    }
}
