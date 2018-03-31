package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;

public class PhotoController {
    @FXML
    Button logoutButton;
    @FXML
    Button backButton;
    @FXML
    Button addButton;
    @FXML
    Button recaptionButton;
    @FXML
    Button openButton;
    @FXML
    Button deleteButton;
    @FXML
    ListView<Photo> photoList;
    private Stage primaryStage;
    private Album album;
    private Scene albumScene;

    public void start(Stage primaryStage, Album album) {
        this.primaryStage = primaryStage;
        this.album = album;
        photoList.setItems(album.getPhotoList());

        if (album.getPhotoList().size() > 0)
            photoList.getSelectionModel().select(0);
    }

    public void logout(ActionEvent e) throws IOException {
        new GeneralMethods().logout(primaryStage);
    }

    public void add(ActionEvent e) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(primaryStage);
        if (file != null && !album.addPhoto(file.getAbsolutePath()))
            GeneralMethods.popAlert("Invalid or duplicate photo.");
    }

    public void recaption(ActionEvent e) {
        Photo photo = photoList.getSelectionModel().getSelectedItem();
        if (photo == null)
            return;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Caption:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
            photo.setCaption((result.get().toLowerCase()));
    }

    public void delete(ActionEvent e) {
        Photo photo = photoList.getSelectionModel().getSelectedItem();
        if (photo == null || !GeneralMethods.popConfirm("Delete this photo?"))
            return;
        if (!album.deletePhoto(photo))
            GeneralMethods.popAlert("Cannot delete.");
    }
    
    public void back(ActionEvent e) throws IOException {
        FXMLLoader albumLoader = new FXMLLoader(getClass().getResource("/view/Album.fxml"));
        Pane albumPane = albumLoader.load();
        AlbumController albumController = albumLoader.getController();
        albumController.start(primaryStage, album.getUser());
        primaryStage.setScene(new Scene(albumPane, 450, 300));
    }

    public void open(ActionEvent e) {

    }

}
