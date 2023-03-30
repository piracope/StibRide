package data.jdbc;

import data.config.ConfigManager;
import data.exception.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author jlc
 */
class DBManager {

    private Connection connection;

    private DBManager() {
    }

    static DBManager getInstance() {
        return DBManagerHolder.INSTANCE;
    }

    Connection getConnection() throws RepositoryException {
        String jdbcUrl = "jdbc:sqlite:" + ConfigManager.getInstance().getProperties("db.url");
        //|| connection.isClosed()
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(jdbcUrl);
            } catch (SQLException ex) {
                throw new RepositoryException("Connexion impossible: " + ex.getMessage());
            }
        }
        return connection;
    }

    void startTransaction() throws RepositoryException {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) {
            throw new RepositoryException("Impossible de démarrer une transaction: " + ex.getMessage());
        }
    }

    void startTransaction(int isolationLevel) throws RepositoryException {
        try {
            getConnection().setAutoCommit(false);

            int isol = switch (isolationLevel) {
                case 0 -> Connection.TRANSACTION_READ_UNCOMMITTED;
                case 1 -> Connection.TRANSACTION_READ_COMMITTED;
                case 2 -> Connection.TRANSACTION_REPEATABLE_READ;
                case 3 -> Connection.TRANSACTION_SERIALIZABLE;
                default -> throw new RepositoryException("Degré d'isolation inexistant!");
            };
            getConnection().setTransactionIsolation(isol);
        } catch (SQLException ex) {
            throw new RepositoryException("Impossible de démarrer une transaction: " + ex.getMessage());
        }
    }

    void validateTransaction() throws RepositoryException {
        try {
            getConnection().commit();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) {
            throw new RepositoryException("Impossible de valider la transaction: " + ex.getMessage());
        }
    }

    void cancelTransaction() throws RepositoryException {
        try {
            getConnection().rollback();
            getConnection().setAutoCommit(true);
        } catch (SQLException ex) {
            throw new RepositoryException("Impossible d'annuler la transaction: " + ex.getMessage());
        }
    }

    private static class DBManagerHolder {

        private static final DBManager INSTANCE = new DBManager();
    }
}
