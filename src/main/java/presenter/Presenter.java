package presenter;

import data.exception.RepositoryException;
import model.BestPathSearcher;
import model.Node;
import util.Observable;
import util.Observer;
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

    public void initialize() throws RepositoryException {
        model.addObserver(this);
        view.setupStations(model.getStations());

    }

    public void findPath(String source, String dest) {
        model.getPath(source, dest);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (arg == null) {
            // changement de l'affichage -> mettre à jour les destinations sauvegardées
            return;
        }

        try {
            view.showResult((List<Node>) arg);
        } catch (ClassCastException e) {
            System.err.println("that's not normal");
        }

    }

    public void savePath(String start, String destination, String name) throws RepositoryException {
        model.savePath(start, destination, name);
    }
}
