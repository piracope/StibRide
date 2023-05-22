package data.jdbc;

import data.dto.StationsDto;
import data.exception.RepositoryException;
import data.repository.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StationsDao implements Dao<Integer, StationsDto> {
    private static StationsDao INSTANCE = null;
    private final Connection connexion;

    private StationsDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static StationsDao getInstance() throws RepositoryException {
        if (INSTANCE == null) INSTANCE = new StationsDao();
        return INSTANCE;
    }

    @Override
    public Integer insert(StationsDto item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(StationsDto item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<StationsDto> selectAll() throws RepositoryException {
        String sql = "SELECT id,name FROM STATIONS";
        List<StationsDto> dtos = new ArrayList<>();
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StationsDto dto = new StationsDto(rs.getInt("id"), rs.getString("name"));
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dtos;

    }

    @Override
    public StationsDto select(Integer key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("Aucune clé donnée en paramètre");
        }
        String sql = "SELECT id,name FROM STATIONS WHERE  id = ?";
        StationsDto dto = null;
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, key);
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                dto = new StationsDto(rs.getInt(1), rs.getString(2));
                count++;
            }
            if (count > 1) {
                throw new RepositoryException("Record pas unique " + key);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dto;

    }
}
