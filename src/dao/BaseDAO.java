package dao;

import database.DatabaseConnection;

import java.sql.*;
import java.util.List;

public abstract class BaseDAO<T> {

    private final String tableName;
    private Connection connection;

    public BaseDAO(String tableName) {
        this.tableName = tableName;
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<T> getAll() {

        String query = "SELECT * FROM " + tableName;

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            return findEntitiesByResultSet(resultSet);
        } catch (SQLException e) {
            System.err.println("!! Une erreur est survenue lors d'une requête getAll sur la table " + tableName + " : " + e.getMessage());
        }

        return null;
    }

    public T findByID(int id) {

        String query = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> results = findEntitiesByResultSet(resultSet);
                if (results == null) return null;
                return results.isEmpty() ? null : results.get(0);
            }
        } catch (SQLException e) {
            System.err.println("!! Une erreur est survenue lors d'une requête getByID sur la table " + tableName + " : " + e.getMessage());
        }

        return null;
    }

    public int findCountInDB() {

        String query = "SELECT COUNT(*) FROM " + tableName;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("!! Une erreur est survenue lors d'une requête getCountInDB sur la table " + tableName + " : " + e.getMessage());
        }

        return 0;

    }

    protected abstract List<T> findEntitiesByResultSet(ResultSet resultSet);

    protected Connection getConnection() {
        connection = DatabaseConnection.getInstance().getConnection();
        return connection;
    }

}