package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserList {
    public ObservableList<User> userList = FXCollections.observableArrayList();
    public static UserList instance = null;

    public UserList() {
        User stock = new User("stock");
        userList.add(stock);
        stock.addAlbum("abc");
        setInstance();
    }

    public static UserList getInstance() {
        return instance;
    }

    public void setInstance() {
        if (instance == null)
            instance = this;
    }

    public ObservableList<User> getUserList() {
        return userList;
    }

    public User findUser(String name) {
        for (User user : userList)
            if (user.name.equals(name))
                return user;
        return null;
    }

    public boolean addUser(String name) {
        if (name.length() == 0 || findUser(name) != null)
            return false;
        return userList.add(new User(name));
    }

    public boolean deleteUser(User user) {
        return !user.name.equals("admin") && !user.name.equals("stock") && userList.remove(user);
    }

}
