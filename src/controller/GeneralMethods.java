package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class GeneralMethods {
    private static Stage primaryStage;

    private GeneralMethods() {

    }

    /**
     * @param stage
     * Set primaryStage
     */
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * @throws IOException
     * Logout
     */
    public static void logout() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(GeneralMethods.class.getResource("/view/Login.fxml"));
        Pane loginPane = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginController.start(primaryStage);
        primaryStage.setScene(new Scene(loginPane, 450, 300));
    }

    /**
     * @param message
     * Pop a new dialog
     */
    public static void popAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Alert");
        alert.initOwner(primaryStage);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * @param message
     * Pop a new dialog
     */
    public static void popInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.initOwner(primaryStage);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * @param message
     * @return Ok or cancel
     * Pop a confirmation dialog
     */
    public static boolean popConfirm(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.initOwner(primaryStage);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
