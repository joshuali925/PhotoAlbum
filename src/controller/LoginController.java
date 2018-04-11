package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;
import model.UserList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class LoginController {
    @FXML
    TextField username;
    @FXML
    Button loginButton;
    private Stage primaryStage;
    private UserList userList;

    /**
     * Initialize
     * 
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userList = UserList.getInstance();
    }

    /**
     * Try to login with given username
     * 
     * @param e
     * @throws IOException
     */
    public void login(ActionEvent e) throws IOException {
        if (username.getText().length() == 0)
            return;
        String input = username.getText().trim().toLowerCase();
        if (input.equals("admin")) {
            FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
            Pane adminPane = adminLoader.load();
            AdminController adminController = adminLoader.getController();
            adminController.start(primaryStage);
            primaryStage.setScene(new Scene(adminPane, 450, 300));
            return;
        }
        User user = userList.findUser(input);
        if (user == null) {
            GeneralMethods.popAlert("User does not exist.");
            return;
        }
        FXMLLoader albumLoader = new FXMLLoader(getClass().getResource("/view/Album.fxml"));
        Pane albumPane = albumLoader.load();
        AlbumController albumController = albumLoader.getController();
        albumController.start(primaryStage, user);
        primaryStage.setScene(new Scene(albumPane, 450, 300));
    }

}
