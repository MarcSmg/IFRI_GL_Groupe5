package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private final String DB_NAME = "database";
    private final String DB_URL = "jdbc:sqlite:" + DB_NAME + ".sqlite";
    private Connection connection;
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("!! Une erreur est subvenue lors de la connection : " + e.getMessage());
        }

//        if (connection != null){
//            try (Statement stmt = connection.createStatement()){
//                SeedQueries.createTables(stmt);
//                SeedQueries.insertBaseDBStatus(stmt);
//                SeedQueries.insertBaseIdentifiers(stmt);
//                File dbFile = new File(DB_NAME + ".sqlite");
//                if (dbFile.exists()){
//                    if (!DatabaseStatus.isIsInitialized(stmt)){
//                        boolean inserted = SeedQueries.insertBaseData(stmt);
//                        if (inserted) DatabaseStatus.setStatusToInitialized(stmt);
//                    }
//                }
//            }
//            catch (SQLException e) {
//                System.err.println("!! Une erreur est subvenue lors de la création des tables : " + e.getMessage());
//            }
//        }
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
