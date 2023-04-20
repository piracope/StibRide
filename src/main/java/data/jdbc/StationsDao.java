package data.jdbc;

import data.dto.StationsDto;
import data.exception.RepositoryException;
import data.repository.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StationsDao implements Dao<Integer, StationsDto> {
    private static StationsDao INSTANCE = null;
    private final Connection connexion;

    private StationsDao() throws RepositoryException {
        connexion = DBManager.getInstance().getConnection();
    }

    public static StationsDao getInstance() throws RepositoryException {
        if(INSTANCE == null) INSTANCE = new StationsDao();
        return INSTANCE;
    }

    @Override
    public Integer insert(StationsDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("Aucun élément donné en paramètre");
        }
        Integer id = null;
        String sql = "INSERT INTO STATIONS(id, name) values(?, ? )";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, item.getKey());
            pstmt.setString(2, item.getName());
            pstmt.executeUpdate();

            ResultSet result = pstmt.getGeneratedKeys();
            while (result.next()) {
                id = result.getInt(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return id;

    }

    @Override
    public void delete(Integer key) throws RepositoryException {
        if (key == null) {
            throw new RepositoryException("Aucune clé donnée en paramètre");
        }
        String sql = "DELETE FROM STATIONS WHERE id = ?";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setInt(1, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

    }

    @Override
    public void update(StationsDto item) throws RepositoryException {
        if (item == null) {
            throw new RepositoryException("Aucun élément donné en paramètre");
        }
        String sql = "UPDATE STATIONS SET name=? where id=? ";
        try (PreparedStatement pstmt = connexion.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getKey());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }

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
