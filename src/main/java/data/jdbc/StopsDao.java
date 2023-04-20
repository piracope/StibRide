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

    /*
    @Override
    public StopId insert(StopsDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("Aucun élément donné en paramètre");
        }
        StopId id = null;
        String sql = "INSERT INTO STOPS(id_line, id_station, id_order) values(?, ?, ?)";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, item.getKey().lineId());
            pstmt.setInt(2, item.getKey().stationId());
            pstmt.setInt(3, item.getOrder());
            pstmt.executeUpdate();

            ResultSet result = pstmt.getGeneratedKeys();
            while (result.next()) {
                id = new StopId(result.getInt(1), result.getInt(2));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return id;

    }

    @Override
    public void delete(StopId key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("Aucune clé donnée en paramètre");
        }
        String sql = "DELETE FROM STOPS WHERE id_line = ? AND id_station = ?";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, key.lineId());
            pstmt.setInt(2, key.stationId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(StopsDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("Aucun élément donné en paramètre");
        }
        String sql = "UPDATE STOPS SET id_order=? where id_line=? AND id_station=? ";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, item.getOrder());
            pstmt.setInt(2, item.getKey().lineId());
            pstmt.setInt(3, item.getKey().stationId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

    }

     */

    @Override
    public StopId insert(StopsDto item) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(StopId key) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(StopsDto item) throws RepositoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<StopsDto> selectAll() throws RepositoryException {
        String sql = "SELECT id_line, id_station, id_order FROM STOPS";
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
