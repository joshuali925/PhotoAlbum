package app;

import java.io.IOException;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Photos extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Pane loginPane = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.start(primaryStage);

        Scene loginScene = new Scene(loginPane, 450, 300);
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
