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
import javafx.scene.control.Label;
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
    @FXML
    Button searchButton;
    @FXML
    Label title;
    private Stage primaryStage;
    private User user;

    public void start(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        title.setText(user + "'s album");
        ObservableList<Album> albumList = user.getAlbumList();
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        photosCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhotoNumber()));
        dateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRange()));
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
        if (result.isPresent() && user.addAlbum(result.get().trim().toLowerCase()) == null)
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

    public void select(Album album) {
        albumTable.getSelectionModel().select(album);
    }

    public void open(ActionEvent e) throws IOException {
        Album album = albumTable.getSelectionModel().getSelectedItem();
        if (album == null)
            return;
        FXMLLoader photoLoader = new FXMLLoader(getClass().getResource("/view/Photo.fxml"));
        Pane photoPane = photoLoader.load();
        PhotoController photoController = photoLoader.getController();
        photoController.start(primaryStage, album);
        primaryStage.setScene(new Scene(photoPane, 450, 300));
    }

    public void search(ActionEvent e) throws IOException {
        FXMLLoader searchLoader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
        Pane searchPane = searchLoader.load();
        SearchController searchController = searchLoader.getController();
        searchController.start(primaryStage, user);
        primaryStage.setScene(new Scene(searchPane, 450, 300));
    }
}
