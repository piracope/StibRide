package data.jdbc;

import data.dto.SavedDto;
import data.exception.RepositoryException;
import data.repository.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SavedDao implements Dao<String, SavedDto> {
    private static SavedDao INSTANCE = null;
    private final Connection connexion;

    private SavedDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static SavedDao getInstance() throws RepositoryException {
        if (INSTANCE == null) INSTANCE = new SavedDao();
        return INSTANCE;
    }

    @Override
    public String insert(SavedDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("Aucun élément donné en paramètre");
        }
        String id = null;
        String sql = "INSERT INTO SAVED(id_station_start, id_station_dest, name) values(?, ?, ?)";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, item.getStartId());
            pstmt.setInt(2, item.getDestId());
            pstmt.setString(3, item.getKey());
            pstmt.executeUpdate();

            ResultSet result = pstmt.getGeneratedKeys();
            while (result.next()) {
                id = result.getString(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return id;
    }

    @Override
    public void delete(String key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("Aucune clé donnée en paramètre");
        }
        String sql = "DELETE FROM SAVED WHERE name = ?";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(SavedDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("Aucun élément donné en paramètre");
        }
        String sql = "UPDATE SAVED SET id_station_start=?, id_station_dest=? where name=? ";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, item.getStartId());
            pstmt.setInt(2, item.getDestId());
            pstmt.setString(3, item.getKey());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public SavedDto select(String key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("Aucune clé donnée en paramètre");
        }
        String sql = "SELECT id_station_start, id_station_dest, name " +
                "FROM SAVED WHERE name = ?";
        SavedDto dto = null;
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                dto = new SavedDto(rs.getInt(1), rs.getInt(2), rs.getString(3));
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


    @Override
    public List<SavedDto> selectAll() throws RepositoryException {
        String sql = "SELECT id_station_start, id_station_dest, name FROM SAVED ORDER BY name";
        List<SavedDto> dtos = new ArrayList<>();
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SavedDto dto = new SavedDto(rs.getInt(1), rs.getInt(2), rs.getString(3));
                dtos.add(dto);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return dtos;

    }
}
