package app;

import java.io.IOException;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

public class Photos extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Pane loginPane = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.start(primaryStage);

        primaryStage.setScene(new Scene(loginPane, 450, 300));
        primaryStage.setResizable(false);
        primaryStage.show();
        
        User.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
