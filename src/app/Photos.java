package app;

import java.io.IOException;

import controller.GeneralMethods;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.UserList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class Photos extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        UserList.readApp();
        GeneralMethods.setStage(primaryStage);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Pane loginPane = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.start(primaryStage);

        primaryStage.setScene(new Scene(loginPane, 450, 300));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args
     * Driver program
     */
    public static void main(String[] args) {
        launch(args);
    }
}
