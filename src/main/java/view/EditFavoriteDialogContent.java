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

public class EditFavoriteDialogContent extends Dialog<String[]> {
    private final GridPane root;
    private final TextField name;
    private final SearchableComboBox<String> source;
    private final SearchableComboBox<String> destination;
    private final Button ok;
    private final Button cancel;


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

    public void fillValues(String name, String start, String end) {
        if (source.getItems().stream().noneMatch(s -> s.equals(start)) || destination.getItems().stream().noneMatch(s -> s.equals(end))) {
            throw new IllegalArgumentException("Unknown stations!");
        }
        this.name.setText(name);
        this.source.setValue(start);
        this.destination.setValue(end);
    }

}
