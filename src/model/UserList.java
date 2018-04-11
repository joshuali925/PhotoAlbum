package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class UserList implements Serializable {
    private transient ObservableList<User> userList = null;
    private ArrayList<User> userListData = new ArrayList<User>();
    private static UserList instance = null;

    private static final String storeDir = "./data";
    private static final String storeFile = "users.dat";

    private UserList() {
        // TODO: stock account
        User stock = new User("stock");
        userListData.add(stock);
        stock.addAlbum("abc");
        setInstance();
    }

    /**
     * Serialize
     */
    public static void writeApp() {
        try {
            File directory = new File(storeDir);
            if (!directory.exists())
                directory.mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(storeDir + File.separator + storeFile));
            oos.writeObject(getInstance());
            oos.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("IOException");
        }
    }

    /**
     * @return Deserialized object
     */
    public static UserList readApp() {
        UserList data = null;
        try {
            FileInputStream is = new FileInputStream(storeDir + File.separator + storeFile);
            ObjectInputStream ois = new ObjectInputStream(is);
            data = (UserList) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            // System.err.println("First time user (file not found)");
            data = getInstance();
        } catch (IOException e) {
            System.err.println("IOException");
            // e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found");
        }
        instance = data;
        return data;
    }

    /**
     * @return The instance (same instance if already created, singleton)
     */
    public static UserList getInstance() {
        if (instance == null)
            instance = new UserList();
        return instance;
    }

    /**
     * Set instance
     */
    public void setInstance() {
        if (instance == null)
            instance = this;
    }

    /**
     * @return The observable list of users
     */
    public ObservableList<User> getUserList() {
        if (userList == null)
            userList = FXCollections.observableArrayList(userListData);
        return userList;
    }

    /**
     * Try to find the user with given name
     * @param name
     * @return User if found, null if not found
     */
    public User findUser(String name) {
        for (User user : userListData)
            if (user.getName().equals(name))
                return user;
        return null;
    }

    /**
     * Add a new user
     * @param name
     * @return True if not duplicate
     */
    public boolean addUser(String name) {
        if (userList == null)
            userList = FXCollections.observableArrayList(userListData);
        if (name.length() == 0 || findUser(name) != null)
            return false;
        User user = new User(name);
        return userList.add(user) && userListData.add(user);
    }

    /**
     * Delete a user
     * @param user
     * @return True if deleted
     */
    public boolean deleteUser(User user) {
        if (userList == null)
            userList = FXCollections.observableArrayList(userListData);
        return userList.remove(user) && userListData.remove(user);
    }

}
