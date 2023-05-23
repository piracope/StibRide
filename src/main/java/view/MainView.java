package view;

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

    /**
     * Displays an error message in a modal alert.
     *
     * @param errorMsg the message to show the user
     */
    public void showError(String errorMsg) {
        Alert error = new Alert(Alert.AlertType.ERROR, errorMsg);
        error.setTitle("Erreur");
        error.setHeaderText("Erreur");
        error.showAndWait();
    }

    /**
     * Asks the user for text content in a dialog, with a given prompt.
     * <p>
     * This function prevents submitting a null string.
     *
     * @param title  the title of the dialog
     * @param prompt the prompt to show the user
     * @return the text input by the user, or null if none was present
     */
    public String askText(String title, String prompt) {
        // setup content
        var dialog = new TextInputDialog();
        dialog.setContentText(prompt);
        dialog.setTitle(title);

        // forbid null input
        final Button confirmed = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        confirmed.addEventFilter(ActionEvent.ACTION, event -> {
            if (dialog.getEditor().getCharacters().isEmpty()) {
                event.consume(); // we don't accept empty station names ofc
            }
        });
        return dialog.showAndWait().orElse(null);
    }

    /**
     * Shows a confirmation dialog and returns true if the user pressed OK.
     *
     * @param prompt the prompt to show the user
     * @return true if they pressed OK
     */
    public boolean showConfirm(String prompt) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, prompt);
        confirm.showAndWait();
        return confirm.getResult() == ButtonType.OK;
    }

    public void showSucceed(String text) {
        Alert yipee = new Alert(Alert.AlertType.INFORMATION, text);
        yipee.showAndWait();
    }

    public String[] showFavoriteEditDialog(String name, String start, String end) {
        var dialog = new EditFavoriteDialogContent(source, destination);
        dialog.fillValues(name, start, end);

        return dialog.showAndWait().orElse(null);
    }

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
            about.showAndWait();
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

            use.setOnAction(e -> presenter.useFavorite(s));
            update.setOnAction(e -> presenter.modifyFavorite(s));
            delete.setOnAction(e -> presenter.deleteFavorite(s));

            ret.getItems().addAll(use, update, delete);
            return ret;
        }).toList());
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

        public SaveButtonHandler(Presenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            String start = source.getValue();
            String end = destination.getValue();

            presenter.newFavorite(start, end);
        }
    }
}
