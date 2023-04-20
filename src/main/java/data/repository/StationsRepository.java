package data.repository;

import data.dto.StationsDto;
import data.exception.RepositoryException;
import data.jdbc.StationsDao;

import java.util.List;

public class StationsRepository implements Repository<Integer, StationsDto> {
    private final StationsDao dao;

    public StationsRepository() throws RepositoryException {
        dao = StationsDao.getInstance();
    }

    StationsRepository(StationsDao dao) { // for testing purposes, I assume
        this.dao = dao;
    }

    @Override
    public Integer add(StationsDto item) throws RepositoryException {
        Integer key = item.getKey();
        if (contains(key)) {
            dao.update(item);
        } else {
            key = dao.insert(item);
        }
        return key;

    }

    @Override
    public void remove(Integer key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<StationsDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public StationsDto get(Integer key) throws RepositoryException {
        return dao.select(key);

    }

    @Override
    public boolean contains(Integer key) throws RepositoryException {
        return get(key) != null;
    }
}
