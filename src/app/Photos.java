package app;

import java.io.IOException;
import java.io.Serializable;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.UserList;

public class Photos extends Application implements Serializable {

    @Override
    public void start(Stage primaryStage) throws IOException {
        UserList userList = UserList.readApp();

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Pane loginPane = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.start(primaryStage);

        primaryStage.setScene(new Scene(loginPane, 450, 300));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
