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

public class LoginController {
    @FXML
    TextField username;
    @FXML
    Button loginButton;
    private Stage primaryStage;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void login(ActionEvent e) throws IOException {
        if (username.getText().trim().equalsIgnoreCase("admin")) {
            FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
            Pane adminPane = adminLoader.load();
            AdminController adminController = adminLoader.getController();
            adminController.start(primaryStage);
            primaryStage.setScene(new Scene(adminPane, 450, 300));
        }
        System.out.println(username.getText());
    }

}
