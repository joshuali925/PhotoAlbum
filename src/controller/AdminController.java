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
    private UserList user;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        user = UserList.getInstance();
        userList.setItems(user.getUserList());
        if (user.getUserList().size() > 0)
            userList.getSelectionModel().select(0);
    }

    public void logout(ActionEvent e) throws IOException {
        new GeneralMethods().logout(primaryStage);
    }

    public void create(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !user.addUser(result.get().toLowerCase()))
            GeneralMethods.popAlert("Invalid or duplicate name.");
    }

    public void delete(ActionEvent e) {
        if (!GeneralMethods.popConfirm("Delete this user?"))
            return;
        if (!user.deleteUser(userList.getSelectionModel().getSelectedItem()))
            GeneralMethods.popAlert("Cannot delete.");
    }
}
