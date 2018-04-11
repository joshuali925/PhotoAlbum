package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.User;
import model.UserList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
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
    private UserList list;

    /**
     * Initialize program and user list
     * 
     * @param primaryStage
     *            The only window
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        list = UserList.getInstance();
        userList.setItems(list.getUserList());
        if (list.getUserList().size() > 0)
            userList.getSelectionModel().select(0);
    }

    /**
     * Logout
     * 
     * @param e
     * @throws IOException
     */
    public void logout(ActionEvent e) throws IOException {
        GeneralMethods.logout();
    }

    /**
     * Add new user
     * 
     * @param e
     */
    public void create(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
        dialog.setHeaderText(null);
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        if (!list.addUser(result.get().trim().toLowerCase())) {
            GeneralMethods.popAlert("Invalid or duplicate name.");
            return;
        }
        UserList.writeApp();
    }

    /**
     * Delete user
     * 
     * @param e
     */
    public void delete(ActionEvent e) {
        User user = userList.getSelectionModel().getSelectedItem();
        if (user == null || !GeneralMethods.popConfirm("Delete this user?"))
            return;
        if (!this.list.deleteUser(user)) {
            GeneralMethods.popAlert("Cannot delete.");
            return;
        }
        UserList.writeApp();
    }
}
