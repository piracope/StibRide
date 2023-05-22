package data.jdbc;

import data.dto.StopId;
import data.dto.StopsDto;
import data.exception.RepositoryException;
import data.repository.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StopsDao implements Dao<StopId, StopsDto> {
    private static StopsDao INSTANCE = null;
    private final Connection connexion;

    private StopsDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static StopsDao getInstance() throws RepositoryException {
        if (INSTANCE == null) INSTANCE = new StopsDao();
        return INSTANCE;
    }

    @Override
    public StopId insert(StopsDto item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(StopId key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(StopsDto item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<StopsDto> selectAll() throws RepositoryException {
        String sql = "SELECT id_line, id_station, id_order FROM STOPS ORDER BY id_line, id_order";
        List<StopsDto> dtos = new ArrayList<>();
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StopsDto dto = new StopsDto(rs.getInt(1), rs.getInt(2), rs.getInt(3));
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dtos;

    }

    @Override
    public StopsDto select(StopId key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("Aucune clé donnée en paramètre");
        }
        String sql = "SELECT id_line, id_station, id_order FROM STOPS WHERE id_line = ? AND id_station = ?";
        StopsDto dto = null;
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, key.lineId());
            pstmt.setInt(2, key.stationId());
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                dto = new StopsDto(rs.getInt(1), rs.getInt(2), rs.getInt(3));
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
