package model;

import java.io.FileNotFoundException;

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

    public Album addAlbum(String name) {
        if (name.length() == 0 || findAlbum(name) != null)
            return null;
        Album album = new Album(name);

        // TODO: only for testing
        try {
            album.addPhoto("D:/Photos/Lightroom/b1.jpg", 1522641600000L);
            album.addPhoto("D:/Photos/Lightroom/b2.jpg", 102412532);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        album.setUser(this);
        albumList.add(album);
        return album;
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
