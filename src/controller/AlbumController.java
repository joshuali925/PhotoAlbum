package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Album;
import model.User;

public class AlbumController {
    @FXML
    TableView<Album> albumTable;
    @FXML
    TableColumn<Album, String> nameCol;
    @FXML
    TableColumn<Album, String> photosCol;
    @FXML
    TableColumn<Album, String> dateCol;
    @FXML
    Button openButton;
    @FXML
    Button createButton;
    @FXML
    Button renameButton;
    @FXML
    Button deleteButton;
    @FXML
    Button logoutButton;
    private Stage primaryStage;
    private User user;
    private ObservableList<Album> albumList;

    public void start(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        albumList = user.getAlbumList();
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        photosCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhotoNumber()));
        albumTable.setItems(albumList);
        if (albumList.size() > 0)
            albumTable.getSelectionModel().select(0);
    }

    public void logout(ActionEvent e) throws IOException {
        new GeneralMethods().logout(primaryStage);
    }

    public void create(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Album name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !user.addAlbum(result.get().toLowerCase()))
            GeneralMethods.popAlert("Invalid or duplicate name.");
    }

    public void rename(ActionEvent e) {
        Album album = albumTable.getSelectionModel().getSelectedItem();
        if (album == null)
            return;
        TextInputDialog dialog = new TextInputDialog(album.getName());
        dialog.setHeaderText(null);
        dialog.setContentText("New name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !user.renameAlbum(album, result.get().toLowerCase())) {
            GeneralMethods.popAlert("Invalid or duplicate name.");
        }
        albumTable.refresh();
    }

    public void delete(ActionEvent e) {
        Album album = albumTable.getSelectionModel().getSelectedItem();
        if (album == null || !GeneralMethods.popConfirm("Delete this album?"))
            return;
        if (!user.deleteAlbum(album))
            GeneralMethods.popAlert("Cannot delete.");
    }

    public void open(ActionEvent e) throws IOException {
        Album album = albumTable.getSelectionModel().getSelectedItem();
        if (album == null)
            return;
        FXMLLoader photoLoader = new FXMLLoader(getClass().getResource("/view/Photo.fxml"));
        Pane albumPane = photoLoader.load();
        PhotoController photoController = photoLoader.getController();
        photoController.start(primaryStage, album);
        primaryStage.setScene(new Scene(albumPane, 450, 300));
    }
}
