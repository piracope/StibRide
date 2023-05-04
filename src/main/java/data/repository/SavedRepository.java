package data.repository;

import data.dto.SavedDto;
import data.exception.RepositoryException;
import data.jdbc.SavedDao;

import java.util.List;

public class SavedRepository implements Repository<String, SavedDto> {
    private final SavedDao dao;

    public SavedRepository() throws RepositoryException {
        dao = SavedDao.getInstance();
    }

    @Override
    public String add(SavedDto item) throws RepositoryException {
        String key = item.getKey();
        if (contains(key)) {
            dao.update(item);
        } else {
            key = dao.insert(item);
        }
        return key;
    }

    @Override
    public void remove(String key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<SavedDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public SavedDto get(String key) throws RepositoryException {
        return dao.select(key);
    }

    @Override
    public boolean contains(String key) throws RepositoryException {
        return get(key) != null;
    }
}
