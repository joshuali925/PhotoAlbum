package controller;

import java.io.IOException;
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

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class SearchController {
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
    CheckBox tagMatchAllCheckBox;
    @FXML
    TextArea tagText;
    @FXML
    ListView<Photo> resultList;

    private Stage primaryStage;
    private User user;
    private ObservableList<Photo> results = FXCollections.observableArrayList();

    /**
     * Initialize
     * 
     * @param primaryStage
     * @param user
     */
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
     * Go back to album list
     * 
     * @param e
     * @throws IOException
     */
    public void back(ActionEvent e) throws IOException {
        FXMLLoader albumLoader = new FXMLLoader(getClass().getResource("/view/Album.fxml"));
        Pane albumPane = albumLoader.load();
        AlbumController albumController = albumLoader.getController();
        albumController.start(primaryStage, user);
        primaryStage.setScene(new Scene(albumPane, 450, 300));
    }

    /**
     * Add new album with search results
     * 
     * @param e
     */
    public void createAlbum(ActionEvent e) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(primaryStage);
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

    /**
     * Search with given conditions
     * 
     * @param e
     */
    public void search(ActionEvent e) {
        results.clear();
        LocalDate fld = fromDatePicker.getValue();
        LocalDate tld = toDatePicker.getValue();
        long fromDate = -1, toDate = -1;
        boolean searchDate = false, searchTags = false, matchAll = tagMatchAllCheckBox.isSelected();
        if (dateCheckBox.isSelected() && fld != null && tld != null) {
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
            if (fromDate <= toDate)
                searchDate = true;
        }

        List<Tag> patterns = null;
        if (tagCheckBox.isSelected()) {
            String[] tags = tagText.getText().split("\n");
            patterns = new ArrayList<Tag>();
            for (String tag : tags) {
                String[] keyVal = tag.split("=");
                if (keyVal.length != 2)
                    continue;
                Tag pattern = new Tag(keyVal[0].trim(), keyVal[1].trim());
                patterns.add(pattern);
            }
            searchTags = patterns.size() > 0;
        }

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
                    if (matchAll) {
                        for (Tag pattern : patterns) {
                            boolean add = false;
                            for (Tag photoTags : photo.getTags()) {
                                if (photoTags.equals(pattern)) {
                                    add = true;
                                    break;
                                }
                            }
                            if (!add) {
                                added = true; // not really added, just so it won't add
                                break;
                            }
                        }
                        if (!added) {
                            results.add(photo);
                            added = true;
                        } else {
                            added = false;
                        }
                    } else {
                        for (Tag photoTags : photo.getTags()) {
                            if (added)
                                break;
                            for (Tag pattern : patterns) {
                                if (photoTags.equals(pattern)) {
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

}
