package view;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import org.controlsfx.control.SearchableComboBox;

import java.util.Arrays;
import java.util.Objects;

/**
 * The content of the favorite trip editing window/dialog.
 */
public class EditFavoriteDialogContent extends Dialog<String[]> {
    /**
     * The window's DialogPane's root.
     */
    private final GridPane root;
    /**
     * The trip's name.
     */
    private final TextField name;
    /**
     * A search bar for the beginning station.
     */
    private final SearchableComboBox<String> source;
    /**
     * A search bar for the ending station.
     */
    private final SearchableComboBox<String> destination;
    /**
     * The confirmation button.
     */
    private final Button ok;
    /**
     * The cancellation button.
     */
    private final Button cancel;


    /**
     * Creates the content for a favorite trip editing dialog.
     *
     * @param source      the search bar containing all values possible for the starting station
     * @param destination the search bar containing all values possible for the ending station
     */
    public EditFavoriteDialogContent(SearchableComboBox<String> source, SearchableComboBox<String> destination) {
        super();
        this.root = new GridPane();
        this.name = new TextField();
        this.source = new SearchableComboBox<>(source.getItems());
        this.destination = new SearchableComboBox<>(destination.getItems());
        this.ok = new Button("Modifier");
        this.cancel = new Button("Annuler");

        setupLayout();
        setupHandlers();
    }

    /**
     * Sets up the handlers for the buttons.
     * <p>
     * Pressing OK does nothing until all values are in a valid state (are present). It will then return
     * a String[] containing the trip's information.
     * <p>
     * Pressing Cancel or the X (window closing button) will abruptly close the window.
     */
    private void setupHandlers() {
        Window window = this.getDialogPane().getScene().getWindow();

        ok.setOnAction(e -> {
            e.consume();
            String[] res = new String[]{name.getText(), source.getValue(), destination.getValue()};
            if (Arrays.stream(res).noneMatch(Objects::isNull) && Arrays.stream(res).noneMatch(String::isBlank)) {
                this.setResult(res);
            }
        });

        cancel.setOnAction(e -> window.hide());

        window.setOnCloseRequest(event -> window.hide());
    }

    /**
     * Sets up the layout of this dialog.
     */
    private void setupLayout() {
        this.setTitle("Modification de l'itinéraire");
        this.getDialogPane().setContent(root);


        root.setHgap(5);
        root.setVgap(2);
        root.add(new Label("Nom : "), 0, 0);
        root.add(name, 1, 0);
        root.add(new Label("Départ"), 0, 1);
        root.add(source, 1, 1);
        root.add(new Label("Destination"), 0, 2);
        root.add(destination, 1, 2);
        root.add(ok, 0, 3);
        root.add(cancel, 1, 3);
    }

    /**
     * Puts the asked values in their place in this dialog.
     *
     * @param name  the trip's current name
     * @param start the trip's current starting station
     * @param end   the trip's current ending station
     */
    public void fillValues(String name, String start, String end) {
        if (source.getItems().stream().noneMatch(s -> s.equals(start)) || destination.getItems().stream().noneMatch(s -> s.equals(end))) {
            throw new IllegalArgumentException("Unknown stations!");
        }
        this.name.setText(name);
        this.source.setValue(start);
        this.destination.setValue(end);
    }

}
