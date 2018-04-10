package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserList;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class SlideController {

    @FXML
    TableView<Tag> tagTable;
    @FXML
    TableColumn<Tag, String> keyCol;
    @FXML
    TableColumn<Tag, String> valueCol;
    @FXML
    Button addButton;
    @FXML
    Button removeButton;
    @FXML
    Button backButton;
    @FXML
    Button copyButton;
    @FXML
    Button moveButton;
    @FXML
    Button prevButton;
    @FXML
    Button nextButton;
    @FXML
    Label nameLabel;
    @FXML
    Label dateLabel;
    @FXML
    Label captionLabel;
    @FXML
    ImageView imageView;
    private Stage primaryStage;
    private Photo photo;
    private Album album;
    private User user;

    /**
     * Initialize
     * 
     * @param primaryStage
     * @param album
     * @param photo
     */
    public void start(Stage primaryStage, Album album, Photo photo) {
        this.primaryStage = primaryStage;
        this.photo = photo;
        this.album = album;
        this.user = album.getUser();
        updateInfo(photo);
        keyCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
        valueCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue()));
    }

    /**
     * Move the image to center
     */
    public void centerImage() {
        Image image = imageView.getImage();
        if (image == null)
            return;
        double x = imageView.getFitWidth() / image.getWidth();
        double y = imageView.getFitHeight() / image.getHeight();
        double adjust = x >= y ? y : x;
        imageView.setX((imageView.getFitWidth() - image.getWidth() * adjust) / 2);
        imageView.setY((imageView.getFitHeight() - image.getHeight() * adjust) / 2);
    }

    /**
     * Add a new tag
     * 
     * @param e
     */
    public void addTag(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
        dialog.setHeaderText(null);
        dialog.setContentText("Tag key:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        String key = result.get().trim();
        dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
        dialog.setHeaderText(null);
        dialog.setContentText("Tag value:");
        result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        String value = result.get().trim();
        if (!photo.addTag(key, value)) {
            GeneralMethods.popAlert("Invalid or duplicate tag.");
            return;
        }
        UserList.writeApp();
    }

    /**
     * Remove selected tag
     * 
     * @param e
     */
    public void removeTag(ActionEvent e) {
        Tag tag = tagTable.getSelectionModel().getSelectedItem();
        if (tag == null || !GeneralMethods.popConfirm("Remove this tag?"))
            return;
        if (!photo.removeTag(tag)) {
            GeneralMethods.popAlert("Cannot remove.");
            return;
        }
        UserList.writeApp();
    }

    /**
     * Go back to photos
     * 
     * @param e
     * @throws IOException
     */
    public void back(ActionEvent e) throws IOException {
        FXMLLoader photoLoader = new FXMLLoader(getClass().getResource("/view/Photo.fxml"));
        Pane albumPane = photoLoader.load();
        PhotoController photoController = photoLoader.getController();
        photoController.start(primaryStage, album);
        primaryStage.setScene(new Scene(albumPane, 450, 300));
        photoController.select(photo);
    }

    /**
     * Copy photo to an album
     * 
     * @param e
     * @throws FileNotFoundException
     */
    public void copy(ActionEvent e) throws FileNotFoundException {
        List<Album> choices = user.getAlbumList();
        ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(choices.get(0), choices);
        dialog.initOwner(primaryStage);
        dialog.setHeaderText(null);
        dialog.setContentText("Copy to album:");
        Optional<Album> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() == album)
            return;
        Album targetAlbum = result.get();
        if (!targetAlbum.addPhoto(photo)) {
            GeneralMethods.popAlert("Photo already exists in " + targetAlbum.getName() + ".");
            return;
        }
        UserList.writeApp();
    }

    /**
     * Move photo to an album
     * 
     * @param e
     * @throws IOException
     */
    public void move(ActionEvent e) throws IOException {
        List<Album> choices = user.getAlbumList();
        ChoiceDialog<Album> dialog = new ChoiceDialog<Album>(choices.get(0), choices);
        dialog.initOwner(primaryStage);
        dialog.setHeaderText(null);
        dialog.setContentText("Move to album:");
        Optional<Album> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() == album)
            return;
        Album targetAlbum = result.get();
        if (!targetAlbum.addPhoto(photo)) {
            GeneralMethods.popAlert("Photo already exists in " + targetAlbum.getName() + ".");
            return;
        }
        ObservableList<Photo> photoList = album.getPhotoList();
        int index = photoList.indexOf(photo);
        album.deletePhoto(photo);
        if (photoList.size() == 0) {
            GeneralMethods.popInfo("No more left.");
            back(e);
            return;
        }
        if (index == photoList.size())
            index--;
        updateInfo(photoList.get(index));
        UserList.writeApp();
    }

    /**
     * Refresh with new photo
     * 
     * @param photo
     */
    public void updateInfo(Photo photo) {
        this.photo = photo;
        imageView.setImage(photo.getImage());
        centerImage();
        nameLabel.setText(photo.getName());
        dateLabel.setText(photo.getDate());
        captionLabel.setText(photo.getCaption());
        tagTable.setItems(photo.getTags());
    }

    /**
     * Go to previous photo
     * 
     * @param e
     * @throws IOException
     */
    public void prev(ActionEvent e) throws IOException {
        ObservableList<Photo> photoList = album.getPhotoList();
        int index = photoList.indexOf(photo) - 1;
        if (index < 0) {
            GeneralMethods.popAlert("No more photos.");
            return;
        }
        updateInfo(photoList.get(index));
    }

    /**
     * Go to next photo
     * 
     * @param e
     * @throws IOException
     */
    public void next(ActionEvent e) throws IOException {
        ObservableList<Photo> photoList = album.getPhotoList();
        int index = photoList.indexOf(photo) + 1;
        if (index >= photoList.size()) {
            GeneralMethods.popAlert("No more photos.");
            return;
        }
        updateInfo(photoList.get(index));
    }

}
