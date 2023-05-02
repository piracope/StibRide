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
}
