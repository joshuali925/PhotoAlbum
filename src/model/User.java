package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    public String name;
    public static ObservableList<User> list = FXCollections.observableArrayList();

    public User(String name) {
        this.name = name;
    }

    public static void initialize() {
        list.add(new User("admin"));
        User stock = new User("stock");
        list.add(stock);
    }

    public String getName() {
        return name;
    }

    public static ObservableList<User> getList() {
        return list;
    }

    public static User find(String name) {
        for (User user : list)
            if (user.name.equals(name))
                return user;
        return null;
    }

    public static boolean add(String name) {
        if (name.length() == 0 || find(name) != null)
            return false;
        list.add(new User(name));
        return true;
    }

    public static boolean delete(User user) {
        return !user.name.equals("admin") && !user.name.equals("stock") && list.remove(user);
    }

    @Override
    public String toString() {
        return name;
    }
}
