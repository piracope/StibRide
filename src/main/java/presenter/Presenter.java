package presenter;

import data.exception.RepositoryException;
import model.BestPathSearcher;
import model.Node;
import util.Observable;
import util.Observer;
import util.Update;
import view.MainView;

import java.util.List;

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
     * @param source
     * @param dest
     */
    public void findPath(String source, String dest) {
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
                // we fetched info from a favorite -> we need to put that info at the correct place
                case FAV_FETCH -> { // probably unused
                    String[] obj = (String[]) up.arg();
                    view.executeFavorite(obj[0], obj[1]);
                }
            }
        } catch (ClassCastException e) {
            System.err.println("that's not normal");
        }

    }

    /**
     * Save a given path as a favorite trip.
     *
     * @param start       the starting station's name
     * @param destination the final station's name
     * @param name        the trip's name
     * @throws RepositoryException database problem
     */
    public void savePath(String start, String destination, String name) throws RepositoryException {
        model.savePath(start, destination, name);
    }

    /**
     * Fetches the
     *
     * @param text
     * @return
     * @throws RepositoryException
     */
    public String[] fetchSave(String text) throws RepositoryException {
        return model.fetchSave(text);
    }

    /**
     * Deletes a favorite from the model.
     *
     * @param fav the favorite trip's name
     */
    public void deleteFav(String fav) throws RepositoryException {
        model.deleteFav(fav);
    }
}
