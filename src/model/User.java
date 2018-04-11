package model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class User implements Serializable {
    private String name;
    private transient ObservableList<Album> albumList = null;
    private ArrayList<Album> albumListData = new ArrayList<Album>();

    public User(String name) {
        this.name = name;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Try to find album of given name
     * 
     * @param name
     * @return Album if found, null if not found
     */
    public Album findAlbum(String name) {
        for (Album album : albumListData)
            if (album.getName().equals(name))
                return album;
        return null;
    }

    /**
     * Add a new album
     * @param name
     * @return The album added
     */
    public Album addAlbum(String name) {
        if (name.length() == 0 || findAlbum(name) != null)
            return null;
        if (albumList == null)
            albumList = FXCollections.observableArrayList(albumListData);
        Album album = new Album(name, this);
        albumList.add(album);
        albumListData.add(album);
        return album;
    }

    /**
     * Delete an album
     * @param album
     * @return True if deleted
     */
    public boolean deleteAlbum(Album album) {
        if (albumList == null)
            albumList = FXCollections.observableArrayList(albumListData);
        return albumList.remove(album) && albumListData.remove(album);
    }

    /**
     * Rename an album
     * @param target
     * @param name
     * @return True if not duplicate
     */
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

    /**
     * @return The observable list of albums
     */
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
