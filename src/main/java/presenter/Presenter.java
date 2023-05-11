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
        updateSaved();
    }

    public void updateSaved() throws RepositoryException {
        view.updateSaved(this, model.getSaved());
    }

    public void findPath(String source, String dest) {
        model.getPath(source, dest);
    }

    @Override
    public void update(Observable observable, Object arg) {
        try {
            Update up = (Update) arg;
            switch (up.type()) {
                case SEARCH_RESULT -> view.showResult((List<Node>) up.arg());
                case SAVED -> view.updateSaved(this, (List<String>) up.arg());
                case SAVE_FETCH -> {
                    String[] obj = (String[]) up.arg();
                    view.executeSave(obj[0], obj[1]);
                }
            }
        } catch (ClassCastException e) {
            System.err.println("that's not normal");
        }

    }

    public void savePath(String start, String destination, String name) throws RepositoryException {
        model.savePath(start, destination, name);
    }

    public String[] fetchSave(String text) throws RepositoryException {
        return model.fetchSave(text);
    }
}
