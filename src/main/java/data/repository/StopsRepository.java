package data.repository;

import data.dto.StationsDto;
import data.dto.StopId;
import data.dto.StopsDto;
import data.exception.RepositoryException;
import data.jdbc.StopsDao;

import java.util.List;

public class StopsRepository implements Repository<StopId, StopsDto> {
    private final StopsDao dao;

    public StopsRepository() throws RepositoryException{
        dao = StopsDao.getInstance();
    }

    StopsRepository(StopsDao dao) {
        this.dao = dao;
    }

    @Override
    public StopId add(StopsDto item) throws RepositoryException {
        StopId key = item.getKey();
        if (contains(key)) {
            dao.update(item);
        } else {
            key = dao.insert(item);
        }
        return key;

    }

    @Override
    public void remove(StopId key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<StopsDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public StopsDto get(StopId key) throws RepositoryException {
        return dao.select(key);

    }

    @Override
    public boolean contains(StopId key) throws RepositoryException {
        return get(key) != null;
    }
}
