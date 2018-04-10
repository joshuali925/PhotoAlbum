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
import model.UserList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
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

    /**
     * Initialize
     * 
     * @param primaryStage
     * @param album
     */
    public void start(Stage primaryStage, Album album) {
        this.primaryStage = primaryStage;
        this.album = album;
        title.setText("Photos in " + album);

        photoList.setItems(album.getPhotoList());
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
     * Try add photo
     * 
     * @param e
     * @throws FileNotFoundException
     */
    public void add(ActionEvent e) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        List<File> files = chooser.showOpenMultipleDialog(primaryStage);
        if (files == null)
            return;
        // if (files.size() > 100) {
        // GeneralMethods.popAlert("Please choose less than 100 photos at once.");
        // return;
        // }
        int counter = 0;
        for (File file : files)
            if (file != null && album.addPhoto(file.getAbsolutePath(), file.lastModified()))
                counter++;
        String response = "Imported " + counter + " photo" + (counter > 1 ? "s." : ".");
        if (files.size() - counter > 0)
            response += "\n" + (files.size() - counter) + " already in " + album + ".";
        GeneralMethods.popInfo(response);
        if (counter > 0)
            UserList.writeApp();
    }

    /**
     * Add caption or recaption
     * 
     * @param e
     */
    public void recaption(ActionEvent e) {
        Photo photo = photoList.getSelectionModel().getSelectedItem();
        if (photo == null)
            return;
        String caption = photo.getCaption();
        TextInputDialog dialog = new TextInputDialog(caption);
        dialog.initOwner(primaryStage);
        dialog.setHeaderText(null);
        dialog.setContentText("Caption:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().equals(caption))
            return;
        photo.setCaption((result.get()));
        // photoList.setItems(null);
        // photoList.setItems(album.getPhotoList());
        // photoList.getSelectionModel().select(photo);
        photoList.refresh();
        UserList.writeApp();
    }

    /**
     * Delete photo
     * 
     * @param e
     */
    public void delete(ActionEvent e) {
        Photo photo = photoList.getSelectionModel().getSelectedItem();
        if (photo == null || !GeneralMethods.popConfirm("Delete this photo?"))
            return;
        if (!album.deletePhoto(photo)) {
            GeneralMethods.popAlert("Cannot delete.");
            return;
        }
        UserList.writeApp();
    }

    /**
     * Go back to albums
     * 
     * @param e
     * @throws IOException
     */
    public void back(ActionEvent e) throws IOException {
        FXMLLoader albumLoader = new FXMLLoader(getClass().getResource("/view/Album.fxml"));
        Pane albumPane = albumLoader.load();
        AlbumController albumController = albumLoader.getController();
        albumController.start(primaryStage, album.getUser());
        primaryStage.setScene(new Scene(albumPane, 450, 300));
        albumController.select(album);
    }

    /**
     * Select current photo
     * 
     * @param photo
     */
    public void select(Photo photo) {
        photoList.getSelectionModel().select(photo);
    }

    /**
     * Display photo
     * 
     * @param e
     * @throws IOException
     */
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
