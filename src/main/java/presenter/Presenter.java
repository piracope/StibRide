package presenter;

import data.exception.RepositoryException;
import model.BestPathSearcher;
import util.Observable;
import util.Observer;
import view.MainView;

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

    }
}
