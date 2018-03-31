package model;

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

    @Override
    public String toString() {
        return name;
    }
}
