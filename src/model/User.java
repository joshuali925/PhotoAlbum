package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    public String name;
    public ObservableList<Album> albumList = FXCollections.observableArrayList();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Album findAlbum(String name) {
        for (Album album : albumList)
            if (album.getName().equals(name))
                return album;
        return null;
    }

    public boolean addAlbum(String name) {
        if (name.length() == 0 || findAlbum(name) != null)
            return false;
        Album album = new Album(name);
        album.setUser(this);
        return albumList.add(album);
    }

    public boolean deleteAlbum(Album album) {
        return albumList.remove(album);
    }

    public boolean renameAlbum(Album target, String name) {
        for (Album album : albumList) {
            if (album == target)
                continue;
            if (album.name.equals(name))
                return false;
        }
        target.setName(name);
        return true;
    }

    public ObservableList<Album> getAlbumList() {
        return albumList;
    }

    @Override
    public String toString() {
        return name;
    }
}
