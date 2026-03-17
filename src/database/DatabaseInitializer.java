/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.sql.*;

/**
 *
 * @author Héloïse
 */
public class DatabaseInitializer {
    public static void prepareDatabase(){
        String createUserTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT PRIMARY KEY AUTO_INCREMENT,"
                + "last_name VARCHAR(50) NOT NULL"
                + "first_name VARCHAR(100) NOT NULL"
                + "login VARCHAR(50) DEFAULT NULL UNIQUE,"
                + "email VARCHAR(100)DEFAULT NULL UNIQUE"
                + "password VARCHAR(255) NOT NULL,"
                + "is_temporary BOOLEAN DEFAULT NOT NULL TRUE"
                + "role ENUM('ADMINISTRATEUR', 'AGENT', 'USAGER') NOT NULL"
                + ");";
        
        
        String createAgentTable = "CREATE TABLE IF NOT EXISTS agents (" 
                + "id_agent INT PRIMARY KEY AUTO_INCREMENT,"
                + "user_id INT NOT NULL,"
                + "function ENUM(),"
                + "signature LONGBLOB,"
                + "cachet LONGBLOB"
                + "CONSTRAINT fk_agent_user FOREIGN KEY (user_id) REFERENCES utilisateurs(id) ON DELETE CASCADE" +
                ");";

            String createUsagerTable = "CREATE TABLE IF NOT EXISTS usagers ("
            + "id_usager INT PRIMARY KEY AUTO_INCREMENT, "
            + "user_id INT NOT NULL, " 
            + "matricule INT NOT NULL UNIQUE, "
            + "filiere VARCHAR(100), "
            + "study_level INT, "
            + "birth_date DATE, " 
            + "place_birth VARCHAR(150), "
            + "gender VARCHAR(10), "
            + "nationalite VARCHAR(100), "
            + "CONSTRAINT fk_usager_user FOREIGN KEY (user_id) REFERENCES utilisateurs(id) ON DELETE CASCADE"
            + ");";
            
            String createWhiteList = "CREATE TABLE IF NOT EXISTS liste_blanche_etudiants ("
                + "matricule INT PRIMARY KEY, "
                + "nom VARCHAR(100), "
                + "prenom VARCHAR(100), "
                + "filiere VARCHAR(100), "
                + "est_deja_inscrit BOOLEAN DEFAULT FALSE"
                + ");";
        
        try(Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement()){
                stmt.execute(createUserTable);
                stmt.execute(createAgentTable);
                stmt.execute(createUsagerTable);
                stmt.execute(createWhiteList);
            
        }catch(SQLException e){
            System.err.println("!!Erreur d'initialisation de la base de données" + e.getMessage());
        }
    }
    
}
