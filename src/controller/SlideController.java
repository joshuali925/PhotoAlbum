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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

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

    public void start(Stage primaryStage, Album album, Photo photo) {
        this.primaryStage = primaryStage;
        this.album = album;
        this.user = album.getUser();
        this.photo = photo;
        imageView.setImage(photo.getImage());
        centerImage();

        nameLabel.setText(photo.getName());
        dateLabel.setText(photo.getDate());
        captionLabel.setText(photo.getCaption());
        tagTable.setItems(photo.getTags());
        keyCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
        valueCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue()));
    }

    public void addTag(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Tag key:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String key = result.get();
            dialog = new TextInputDialog();
            dialog.setHeaderText(null);
            dialog.setContentText("Tag value:");
            result = dialog.showAndWait();
            if (result.isPresent() && !photo.addTag(key, result.get()))
                GeneralMethods.popAlert("Invalid or duplicate tag.");
        }
    }

    public void removeTag(ActionEvent e) {
        Tag tag = tagTable.getSelectionModel().getSelectedItem();
        if (tag == null || !GeneralMethods.popConfirm("Remove this tag?"))
            return;
        if (!photo.removeTag(tag))
            GeneralMethods.popAlert("Cannot remove.");
    }

    public void back(ActionEvent e) throws IOException {
        FXMLLoader photoLoader = new FXMLLoader(getClass().getResource("/view/Photo.fxml"));
        Pane albumPane = photoLoader.load();
        PhotoController photoController = photoLoader.getController();
        photoController.start(primaryStage, album);
        primaryStage.setScene(new Scene(albumPane, 450, 300));
        photoController.select(photo);
    }

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

    public void copy(ActionEvent e) {

    }

    public void move(ActionEvent e) {

    }

    public void prev(ActionEvent e) throws IOException {
        ObservableList<Photo> photoList = album.getPhotoList();
        int index = photoList.indexOf(photo) - 1;
        if (index < 0) {
            GeneralMethods.popAlert("No more photos.");
            return;
        }
        FXMLLoader slideLoader = new FXMLLoader(getClass().getResource("/view/Slide.fxml"));
        Pane slidePane = slideLoader.load();
        Photo newPhoto = photoList.get(index);
        SlideController slideController = slideLoader.getController();
        slideController.start(primaryStage, album, newPhoto);
        primaryStage.setScene(new Scene(slidePane, 450, 300));
    }

    public void next(ActionEvent e) throws IOException {
        ObservableList<Photo> photoList = album.getPhotoList();
        int index = photoList.indexOf(photo) + 1;
        if (index >= photoList.size()) {
            GeneralMethods.popAlert("No more photos.");
            return;
        }
        FXMLLoader slideLoader = new FXMLLoader(getClass().getResource("/view/Slide.fxml"));
        Pane slidePane = slideLoader.load();
        Photo newPhoto = photoList.get(index);
        SlideController slideController = slideLoader.getController();
        slideController.start(primaryStage, album, newPhoto);
        primaryStage.setScene(new Scene(slidePane, 450, 300));

    }

}
