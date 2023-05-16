package view;

import data.dto.FavoriteDto;
import data.exception.RepositoryException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Node;
import org.controlsfx.control.SearchableComboBox;
import presenter.Presenter;

import java.util.List;

public class MainView {

    @FXML
    public Menu favoriteMenu;
    @FXML
    private MenuItem aboutItem;

    @FXML
    private SearchableComboBox<String> destination;

    @FXML
    private MenuItem quitItem;

    @FXML
    private SearchableComboBox<String> source;

    @FXML
    private Button startButton;

    @FXML
    private TableView<Node> table;

    @FXML
    private TableColumn<Node, List<Integer>> linesCol;

    @FXML
    private TableColumn<Node, String> stationsCol;

    @FXML
    private Button favButton;

    @FXML
    public void initialize() {
        menuSetup();

        linesCol.setCellValueFactory(new PropertyValueFactory<>("lines"));
        stationsCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * Sets up the basic menu elements
     */
    private void menuSetup() {
        quitItem.setOnAction(e -> Platform.exit());
        aboutItem.setOnAction(e -> {
            Alert about = new Alert(Alert.AlertType.NONE, """
                    STIBRide
                    Made by Ayoub MOUFIDI (58089)
                    For ATLG4""",
                    ButtonType.CLOSE);
            about.setTitle("About");
            about.show();
        });
    }

    /**
     * Adds button handlers to all (most) clickable buttons
     *
     * @param presenter the presenter to which we will communicate
     */
    public void addButtonHandlers(Presenter presenter) {
        startButton.setOnAction(new StartButtonHandler(presenter));
        favButton.setOnAction(new SaveButtonHandler(presenter));
    }

    /**
     * Sets up the searchable fields with an updated list of stations.
     *
     * @param stations the stations that we have to choose from
     */
    public void setupStations(List<String> stations) {
        source.getItems().clear();
        source.getItems().addAll(stations);

        destination.getItems().clear();
        destination.getItems().addAll(stations);
    }

    /**
     * Shows the shortest path resulting from a search in the table.
     *
     * @param results the shortest path
     */
    public void showResult(List<Node> results) {
        table.getItems().clear();
        table.getItems().addAll(results);
    }

    /**
     * Updates the list of favorites.
     *
     * @param presenter the presenter with which we will communicate future actions
     * @param favs      the favorite trips
     */
    public void updateFavorite(Presenter presenter, List<String> favs) {
        favoriteMenu.getItems().clear();
        favoriteMenu.getItems().addAll(favs.stream().map(s -> {
            // TRIP NAME
            //      Utiliser -> fires the search
            //      Modifier -> shows a popup to modify that save
            //      Supprimer -> shows a confirmation popup
            Menu ret = new Menu(s);

            MenuItem use = new MenuItem("Utiliser");
            MenuItem update = new MenuItem("Modifier");
            MenuItem delete = new MenuItem("Supprimer");

            use.setOnAction(e -> {
                try {
                    String[] save = presenter.fetchSave(s);
                    if (save != null) {
                        executeFavorite(save[0], save[1]);
                    }
                } catch (RepositoryException ex) {
                    throw new RuntimeException(ex);
                }
            });

            update.setOnAction(e -> modifySave(presenter, s));

            delete.setOnAction(e -> {
                try {
                    Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer le trajet " + s + "?");
                    conf.showAndWait();
                    if (conf.getResult() == ButtonType.OK) {
                        presenter.deleteFav(s);
                    }
                } catch (RepositoryException ex) {
                    throw new RuntimeException(ex);
                }
            });

            ret.getItems().addAll(use, update, delete);
            return ret;
        }).toList());
    }

    public void modifySave(Presenter presenter, String name) {
        Dialog<FavoriteDto> dialog = new Dialog<>();
        dialog.setTitle("Modification du trajet " + name);

        // TODO : create a new FXML dialog and like put everything in it.

    }

    /**
     * Replaces a favorite trip's stations in their fields and starts the search.
     *
     * @param source the favorite's starting station's name
     * @param dest   the favorite's end station's name
     */
    public void executeFavorite(String source, String dest) {
        this.source.setValue(source);
        this.destination.setValue(dest);
        startButton.fire();
    }

    /**
     * The handler for the start button
     */
    private class StartButtonHandler implements EventHandler<ActionEvent> {
        private final Presenter presenter;

        public StartButtonHandler(Presenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            presenter.findPath(source.getValue(), destination.getValue());
        }
    }

    // TODO : the handlers should just fetch the values and send them to the presenter. The presenter then asks the view
    //  to display the necessary dialogs.

    /**
     * The handler for the favorite button
     */
    private class SaveButtonHandler implements EventHandler<ActionEvent> {
        private final Presenter presenter;
        private final TextInputDialog saveDialog;

        public SaveButtonHandler(Presenter presenter) {
            this.presenter = presenter;
            this.saveDialog = new TextInputDialog();
            saveDialog.setTitle("Sauvegarde de l'itinéraire");
            final Button confirmed = (Button) saveDialog.getDialogPane().lookupButton(ButtonType.OK);
            confirmed.addEventFilter(ActionEvent.ACTION, event -> {
                if (saveDialog.getEditor().getCharacters().isEmpty()) {
                    event.consume(); // we don't accept empty station names ofc
                }
            });
        }

        /**
         * Gets the name of that trip.
         *
         * @param start the starting station's name
         * @param end   the final station's name
         * @return the trip's name
         */
        private String getFavName(String start, String end) {
            saveDialog.setContentText("Quel est le nom de l'itinéraire " + start + " -- " + end);
            return saveDialog.showAndWait().orElse(null);
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            String start = source.getValue();
            String end = destination.getValue();

            if (start == null || end == null) { // don't show the modal if there's nothing to work with
                actionEvent.consume();
                return;
            }

            String name = getFavName(start, end);
            if (name == null) return;

            try {
                presenter.savePath(start, end, name);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
