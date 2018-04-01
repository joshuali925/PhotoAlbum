package controller;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;
import model.UserList;

public class SearchController implements Serializable {
    @FXML
    Button logoutButton;
    @FXML
    Button backButton;
    @FXML
    Button searchButton;
    @FXML
    Button createAlbumButton;
    @FXML
    DatePicker fromDatePicker;
    @FXML
    DatePicker toDatePicker;
    @FXML
    CheckBox dateCheckBox;
    @FXML
    CheckBox tagCheckBox;
    @FXML
    TextArea tagText;
    @FXML
    ListView<Photo> resultList;

    private Stage primaryStage;
    private User user;
    private ObservableList<Photo> results = FXCollections.observableArrayList();

    public void start(Stage primaryStage, User user) {
        this.primaryStage = primaryStage;
        this.user = user;
        dateCheckBox.setSelected(true);
        tagCheckBox.setSelected(true);
        resultList.setItems(results);
        resultList.setCellFactory(cell -> new ListCell<Photo>() {
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
    }

    public void logout(ActionEvent e) throws IOException {
        new GeneralMethods().logout(primaryStage);
    }

    public void back(ActionEvent e) throws IOException {
        FXMLLoader albumLoader = new FXMLLoader(getClass().getResource("/view/Album.fxml"));
        Pane albumPane = albumLoader.load();
        AlbumController albumController = albumLoader.getController();
        albumController.start(primaryStage, user);
        primaryStage.setScene(new Scene(albumPane, 450, 300));
    }

    public void createAlbum(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("Album name:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return;
        Album album = user.addAlbum(result.get().trim().toLowerCase());
        if (album == null) {
            GeneralMethods.popAlert("Invalid or duplicate name.");
            return;
        }
        for (Photo photo : results)
            album.addPhoto(photo);
        GeneralMethods.popInfo("Created " + album.getName() + " with " + results.size() + " photos.");
        UserList.writeApp();
    }

    public void search(ActionEvent e) {
        results.clear();
        LocalDate fld = fromDatePicker.getValue();
        LocalDate tld = toDatePicker.getValue();
        long fromDate = -1, toDate = -1;
        boolean searchDate = false, searchTags = false;
        if (fld != null && tld != null) {
            searchDate = true;
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.MONTH, fld.getMonthValue() - 1);
            cal.set(Calendar.DAY_OF_MONTH, fld.getDayOfMonth());
            cal.set(Calendar.YEAR, fld.getYear());
            fromDate = cal.getTimeInMillis();

            cal.set(Calendar.MONTH, tld.getMonthValue() - 1);
            cal.set(Calendar.DAY_OF_MONTH, tld.getDayOfMonth());
            cal.set(Calendar.YEAR, tld.getYear());
            toDate = cal.getTimeInMillis() + 86400000 - 1;
        }

        String[] tags = tagText.getText().split("\n");
        List<Tag> patterns = new ArrayList<Tag>();
        for (String tag : tags) {
            String[] keyVal = tag.split("=");
            if (keyVal.length != 2)
                continue;
            Tag pattern = new Tag(keyVal[0].trim(), keyVal[1].trim());
            patterns.add(pattern);
        }

        searchDate = searchDate && dateCheckBox.isSelected();
        searchTags = patterns.size() > 0 && tagCheckBox.isSelected();

        if (!searchDate && !searchTags)
            return;

        List<Album> albums = user.getAlbumList();
        for (Album album : albums) {
            List<Photo> photos = album.getPhotoList();
            for (Photo photo : photos) {
                boolean added = false;
                if (searchDate) {
                    long timestamp = photo.getTimestamp();
                    if (fromDate <= timestamp && toDate >= timestamp) {
                        results.add(photo);
                        added = true;
                    }
                }
                if (!added && searchTags) {
                    for (Tag photoTags : photo.getTags()) {
                        if (added)
                            break;
                        for (Tag t : patterns) {
                            if (photoTags.equals(t)) {
                                results.add(photo);
                                added = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

}
