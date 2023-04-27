package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.SearchableComboBox;

import java.io.InputStream;
import java.util.Objects;

public class MainView {

    @FXML
    private MenuItem aboutItem;

    @FXML
    private SearchableComboBox<String> destination;

    @FXML
    private TableColumn<?, String> linesCol;

    @FXML
    private MenuItem quitItem;

    @FXML
    private SearchableComboBox<String> source;

    @FXML
    private Button startButton;

    @FXML
    private TableColumn<?, String> stationsCol;

    @FXML
    private TableView<?> table;

    public void initialize() {
        menuSetup();
    }

    private void menuSetup(){
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

}
