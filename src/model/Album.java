package model;

import java.io.FileNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Album {
    public String name;
    public String range;
    public User user;
    public ObservableList<Photo> photoList = FXCollections.observableArrayList();

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

    public String getPhotoNumber() {
        return photoList.size() + "";
    }

    public ObservableList<Photo> getPhotoList() {
        return photoList;
    }

    public boolean addPhoto(String path, long date) throws FileNotFoundException {
        for (Photo photo : photoList)
            if (photo.getPath().equals(path))
                return false;
        return photoList.add(new Photo(path,date));
    }
    
    public boolean deletePhoto(Photo photo) {
        return photoList.remove(photo);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
