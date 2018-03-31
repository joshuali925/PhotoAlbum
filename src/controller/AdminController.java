package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.User;

public class AdminController {
    @FXML
    Button logoutButton;
    @FXML
    Button createButton;
    @FXML
    Button deleteButton;
    @FXML
    ListView<User> userList;
    private Stage primaryStage;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void logout(ActionEvent e) throws IOException {
         new GeneralMethods().logout(primaryStage);
    }

    public void create(ActionEvent e) {

    }

    public void delete(ActionEvent e) {

    }
}
