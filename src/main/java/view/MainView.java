package view;

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
    public Menu savedMenu;
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
    private Button saveButton;

    @FXML

    public void initialize() {
        menuSetup();

        linesCol.setCellValueFactory(new PropertyValueFactory<>("lines"));
        stationsCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

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

    public void addButtonHandlers(Presenter presenter) {
        startButton.setOnAction(new StartButtonHandler(presenter));
        saveButton.setOnAction(new SaveButtonHandler(presenter));
    }

    public void setupStations(List<String> stations) {
        source.getItems().clear();
        source.getItems().addAll(stations);

        destination.getItems().clear();
        destination.getItems().addAll(stations);
    }

    public void showResult(List<Node> results) {
        table.getItems().clear();
        table.getItems().addAll(results);
    }

    public void updateSaved(Presenter presenter, List<String> saved) {
        savedMenu.getItems().clear();
        savedMenu.getItems().addAll(saved.stream().map(s -> {
            MenuItem ret = new MenuItem(s);
            ret.setOnAction(e -> {
                try {
                    presenter.fetchSave(ret.getText());
                } catch (RepositoryException ex) {
                    throw new RuntimeException(ex);
                }
            });
            return ret;
        }).toList());
    }

    public void executeSave(String source, String dest) {
        this.source.setValue(source);
        this.destination.setValue(dest);
        startButton.fire();
    }

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

    private class SaveButtonHandler implements EventHandler<ActionEvent> {
        private final Presenter presenter;

        private final TextInputDialog saveDialog;

        public SaveButtonHandler(Presenter presenter) {
            this.presenter = presenter;
            this.saveDialog = new TextInputDialog();
            saveDialog.setTitle("Sauvegarde de l'itinéraire");
            final Button button = (Button) saveDialog.getDialogPane().lookupButton(ButtonType.OK);
            button.addEventFilter(ActionEvent.ACTION, event -> {
                if (saveDialog.getEditor().getCharacters().isEmpty()) {
                    event.consume();
                }
            });
        }

        private String getName() {
            saveDialog.setContentText("Quel est le nom de l'itinéraire " + source.getValue() + " -- " + destination.getValue());
            saveDialog.showAndWait();

            return saveDialog.getEditor().getCharacters().toString();
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            String name = getName();
            try {
                presenter.savePath(source.getValue(), destination.getValue(), name);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
