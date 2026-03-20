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
                + "is_temporary BOOLEAN DEFAULT TRUE NOT NULL, "
                + "is_able BOOLEAN DEFAULT TRUE NOT NULL"
                + "role ENUM('ADMINISTRATEUR', 'AGENT', 'USAGER') NOT NULL,"
                + ");";
        
        
        String createAgentTable = "CREATE TABLE IF NOT EXISTS agents (" 
                + "id_agent INT PRIMARY KEY AUTO_INCREMENT,"
                + "user_id INT NOT NULL,"
                + "function ENUM(),"
                + "signature LONGBLOB,"
                + "cachet LONGBLOB, "
                + "CONSTRAINT fk_agent_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ");";

        String createUsagerTable = "CREATE TABLE IF NOT EXISTS usagers ("
        + "id_usager INT PRIMARY KEY AUTO_INCREMENT, "
        + "user_id INT NOT NULL, "
        + "matricule INT NOT NULL UNIQUE, "
        + "fieldOfStudy VARCHAR(100), "
        + "study_level VARCHAR(50), "
        + "birth_date DATE, "
        + "place_birth VARCHAR(150), "
        + "gender VARCHAR(10), "
        + "nationalite VARCHAR(100), "
        + "CONSTRAINT fk_usager_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
        + ");";

        String createWhiteList = "CREATE TABLE IF NOT EXISTS white_list_usagers ("
            + "matricule VARCHAR(50) PRIMARY KEY, "
            + "lastName VARCHAR(100), "
            + "firstName VARCHAR(100), "
            + "birth_date DATE, "
            + "place_birth VARCHAR(150), "
            + "fieldOfStudy VARCHAR(100), "
            + "study_level VARCHAR(50), "
            + "nationalite VARCHAR(100), "
            + "has_account BOOLEAN DEFAULT FALSE NOT NULL "
            + ");";

        String createAdministrativeActTable = "CREATE TABLE IF NOT EXISTS actes_administratifs ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "type VARCHAR(255), "
                + "contenu VARCHAR(255), "
                + "id_signataire INT, "
                + "est_signe BOOLEAN DEFAULT FALSE, "
                + "est_archive BOOLEAN DEFAULT FALSE"
                + ");";

        String createDemandTable = "CREATE TABLE IF NOT EXISTS demandes ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "numero_demande VARCHAR(255), "
                + "statut VARCHAR(255), "
                + "date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
                + ");";

        String createAgentsPermissions = "CREATE TABLE IF NOT EXISTS agents_permissions ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "agent_id INT, "
                + "permission_level VARCHAR(20), "
                + "act_type VARCHAR(50)"
                + "CONSTRAINT fk_agent_acte FOREIGN KEY(agent_id) REFERENCES agents(id) ON DELETE CASCADE"
                + ")";

        try(Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement()){
                stmt.execute(createUserTable);
                stmt.execute(createAgentTable);
                stmt.execute(createUsagerTable);
                stmt.execute(createWhiteList);
                stmt.execute(createAgentsPermissions);
                stmt.execute(createAdministrativeActTable);
                stmt.execute(createDemandTable);

        } catch(SQLException e){
            System.err.println("!!Erreur d'initialisation de la base de données" + e.getMessage());
        }
    }
    
}
