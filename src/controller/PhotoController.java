package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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
    Label title;
    @FXML
    ListView<Photo> photoList;
    private Stage primaryStage;
    private Album album;

    public void start(Stage primaryStage, Album album) {
        this.primaryStage = primaryStage;
        this.album = album;
        photoList.setItems(album.getPhotoList());
        title.setText("Photos in " + album);

        photoList.setCellFactory(cell -> new ListCell<Photo>() {
            @Override
            public void updateItem(Photo name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (name.getCaption().length() > 0)
                        setText(name.getName() + " (" + name.getCaption() + ")");
                    else
                        setText(name.getName());
                    setGraphic(name.getThumbnail());
                }
            }
        });

        if (album.getPhotoList().size() > 0)
            photoList.getSelectionModel().select(0);
    }

    public void logout(ActionEvent e) throws IOException {
        new GeneralMethods().logout(primaryStage);
    }

    public void add(ActionEvent e) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        List<File> files = chooser.showOpenMultipleDialog(primaryStage);
        if (files == null)
            return;
        if (files.size() > 10) {
            GeneralMethods.popAlert("Please choose less than 10 photos at once.");
            return;
        }
        int counter = 0;
        for (File file : files)
            if (file != null && album.addPhoto(file.getAbsolutePath(), file.lastModified()))
                counter++;
        GeneralMethods.popInfo("Imported " + counter + " photo" + (counter > 1 ? "s." : "."));
    }

    public void recaption(ActionEvent e) {
        Photo photo = photoList.getSelectionModel().getSelectedItem();
        if (photo == null)
            return;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Caption:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        photo.setCaption((result.get().toLowerCase()));
        photoList.refresh();
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
        albumController.select(album);
    }

    public void select(Photo photo) {
        photoList.getSelectionModel().select(photo);
    }

    public void open(ActionEvent e) throws IOException {
        Photo photo = photoList.getSelectionModel().getSelectedItem();
        if (photo == null)
            return;
        FXMLLoader slideLoader = new FXMLLoader(getClass().getResource("/view/Slide.fxml"));
        Pane slidePane = slideLoader.load();
        SlideController slideController = slideLoader.getController();
        slideController.start(primaryStage, album, photo);
        primaryStage.setScene(new Scene(slidePane, 450, 300));
    }

}
