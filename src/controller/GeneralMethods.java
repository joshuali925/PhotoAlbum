package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GeneralMethods {
    public void logout(Stage primaryStage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Pane loginPane = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.start(primaryStage);

        Scene loginScene = new Scene(loginPane, 450, 300);
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
