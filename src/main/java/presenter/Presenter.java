package presenter;

import data.exception.RepositoryException;
import model.BestPathSearcher;
import model.Node;
import util.Observable;
import util.Observer;
import util.Update;
import view.MainView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Presenter implements Observer {

    private final BestPathSearcher model;
    private final MainView view;

    public Presenter(BestPathSearcher model, MainView view) throws RepositoryException {
        this.model = model;
        this.view = view;

        initialize();
    }

    /**
     * Sets up this presenter and sends the required working info to the view.
     *
     * @throws RepositoryException database problem
     */
    public void initialize() throws RepositoryException {
        model.addObserver(this);
        view.setupStations(model.getStations());
        updateFavorite();
    }

    /**
     * Sends the view an updated list of favorite trips.
     *
     * @throws RepositoryException database problem
     */
    public void updateFavorite() throws RepositoryException {
        view.updateFavorite(this, model.getFavorite());
    }

    /**
     * Starts a best path search from one station to another.
     *
     * @param source the beginning of the path
     * @param dest   the path's end
     */
    public void findPath(String source, String dest) {
        if (source == null || dest == null) {
            MainView.showError("Entrez deux stations valides.");
            return;
        }
        model.getPath(source, dest);
    }

    @Override
    public void update(Observable observable, Object arg) {
        try {
            Update up = (Update) arg;
            switch (up.type()) {
                // we have a result -> we need to display it
                case SEARCH_RESULT -> view.showResult((List<Node>) up.arg());
                // we have a new favorite -> we need to add it to the list
                case NEW_FAVORITE -> view.updateFavorite(this, (List<String>) up.arg());
            }
        } catch (ClassCastException e) {
            System.err.println("that's not normal");
        }

    }

    public void useFavorite(String name) {
        String[] favorite = fetchFavorite(name);
        view.executeFavorite(favorite[0], favorite[1]);
    }

    /**
     * Fetches the path from a favorite's name.
     *
     * @param name the favorite path's name
     * @return its corresponding path
     */
    public String[] fetchFavorite(String name) {
        String[] save = model.fetchSave(name);
        if (save == null) {
            MainView.showError("Cet itinéraire n'existe plus. Redémarrez le logiciel.");
        }

        return save;
    }

    /**
     * Deletes a favorite from the model.
     *
     * @param name the favorite trip's name
     */
    public void deleteFavorite(String name) throws RepositoryException {
        if (MainView.showConfirm("Voulez-vous vraiment supprimer le trajet " + name + "?")) {
            model.deleteFavorite(name);
            MainView.showSucceed("Itinéraire supprimé!");
        }
    }

    /**
     * Save a given path as a favorite trip.
     *
     * @param start the starting station's name
     * @param end   the final station's name
     */
    public void newFavorite(String start, String end) {
        if (start == null || end == null) {
            MainView.showError("Entrez deux stations valides.");
            return;
        }

        String name = MainView.askText("Sauvegarde de l'itinéraire",
                "Quel nom voulez-vous donner à l'itinéraire " + start + " — " + end + " ?");
        if (name == null || name.isBlank()) {
            MainView.showError("Entrez un nom pour ce trajet.");
            return;
        }

        model.savePath(start, end, name);
        MainView.showSucceed("Itinéraire ajouté !");
    }

    public void modifyFavorite(String name) {
        String[] save = model.fetchSave(name);
        if (save == null) {
            MainView.showError("Cet itinéraire n'existe plus. Redémarrez le logiciel.");
            return;
        }

        String[] newFav = view.showFavoriteEditDialog(name, save[0], save[1]);
        if(newFav == null || Arrays.stream(newFav).anyMatch(Objects::isNull)) {
            MainView.showError("Veuillez remplir tous les champs.");
            return;
        }

        model.deleteFavorite(name);
        model.savePath(newFav[0], newFav[1], newFav[2]);
    }
}
