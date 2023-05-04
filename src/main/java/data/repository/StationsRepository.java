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

    @Override
    public Integer add(StationsDto item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Integer key) {
        throw new UnsupportedOperationException();
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
