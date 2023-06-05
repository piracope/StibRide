import data.config.ConfigManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BestPathSearcher;
import presenter.Presenter;
import view.MainView;

import java.util.Objects;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // 1. load the FXML and fill a MainView
        FXMLLoader fxml = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/view.fxml")));

        Parent root = fxml.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("StibRide");
        stage.setResizable(false);

        // 2. get the created controller to pass it to presenter
        MainView view = fxml.getController();

        try {
            ConfigManager.getInstance().load();

            BestPathSearcher model = new BestPathSearcher();
            Presenter presenter = new Presenter(model, view);
            view.addButtonHandlers(presenter);

            stage.show();
        } catch (Exception e) {
            view.showError(e.getMessage()); // in a last effort, at least show the exception
        }
    }
}
