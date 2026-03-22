package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private final String PORT = "3306";
    private final String DB_NAME = "acts_database";
    private final String DB_URL = "jdbc:mysql://localhost:" + PORT + "/" + DB_NAME + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection connection;
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("!! Une erreur est subvenue lors de la connection : " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            System.err.println("!! Une erreur est subvenue lors de la récupération de la connexion : " + e.getMessage());
        }
        return connection;
    }

}
