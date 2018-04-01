package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User implements Serializable {
    private String name;
    private transient ObservableList<Album> albumList = null;
    private ArrayList<Album> albumListData = new ArrayList<Album>();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Album findAlbum(String name) {
        for (Album album : albumListData)
            if (album.getName().equals(name))
                return album;
        return null;
    }

    public Album addAlbum(String name) {
        if (name.length() == 0 || findAlbum(name) != null)
            return null;
        if (albumList == null)
            albumList = FXCollections.observableArrayList(albumListData);
        Album album = new Album(name);

        // TODO: only for testing
        // try {
        // album.addPhoto("D:/Photos/Lightroom/b1.jpg", 1522641600000L);
        // album.addPhoto("D:/Photos/Lightroom/b2.jpg", 102412532);
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // }

        album.setUser(this);
        albumList.add(album);
        albumListData.add(album);
        return album;
    }

    public boolean deleteAlbum(Album album) {
        if (albumList == null)
            albumList = FXCollections.observableArrayList(albumListData);
        return albumList.remove(album) && albumListData.remove(album);
    }

    public boolean renameAlbum(Album target, String name) {
        for (Album album : albumListData) {
            if (album == target)
                continue;
            if (album.getName().equals(name))
                return false;
        }
        target.setName(name);
        return true;
    }

    public ObservableList<Album> getAlbumList() {
        if (albumList == null)
            albumList = FXCollections.observableArrayList(albumListData);
        return albumList;
    }

    @Override
    public String toString() {
        return name;
    }
}
