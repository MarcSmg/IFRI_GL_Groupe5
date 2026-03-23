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
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "last_name VARCHAR(50) NOT NULL, "
                + "first_name VARCHAR(100) NOT NULL, "
                + "login VARCHAR(50) UNIQUE, "
                + "email VARCHAR(100) UNIQUE, "
                + "password VARCHAR(255) NOT NULL, "
                + "is_temporary BOOLEAN NOT NULL DEFAULT TRUE, "
                + "is_able BOOLEAN NOT NULL DEFAULT TRUE, "
                + "role ENUM('ADMINISTRATEUR', 'AGENT', 'USAGER') NOT NULL"
                + ");";


        String createAgentTable = "CREATE TABLE IF NOT EXISTS agents ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "user_id INT NOT NULL, "
                + "function VARCHAR(100), "
                + "signature LONGBLOB, "
                + "cachet LONGBLOB, "
                + "CONSTRAINT fk_agent_user "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ");";


        String createUsagerTable = "CREATE TABLE IF NOT EXISTS usagers ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "user_id INT NOT NULL, "
                + "matricule VARCHAR(50) NOT NULL UNIQUE, "
                + "field_of_study VARCHAR(100), "
                + "study_level VARCHAR(50), "
                + "birth_date DATE, "
                + "place_birth VARCHAR(150), "
                + "gender VARCHAR(10), "
                + "nationalite VARCHAR(100), "
                + "CONSTRAINT fk_usager_user "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                + ");";


        String createWhiteList = "CREATE TABLE IF NOT EXISTS white_list_usagers ("
                + "matricule VARCHAR(50) PRIMARY KEY, "
                + "last_name VARCHAR(100), "
                + "first_name VARCHAR(100), "
                + "birth_date DATE, "
                + "place_birth VARCHAR(150), "
                + "field_of_study VARCHAR(100), "
                + "study_level VARCHAR(50), "
                + "nationalite VARCHAR(100), "
                + "has_account BOOLEAN NOT NULL DEFAULT FALSE"
                + ");";


        String createAdministrativeActTable = "CREATE TABLE IF NOT EXISTS actes_administratifs ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "type VARCHAR(255), "
                + "contenu TEXT, "
                + "signataire_id INT, "
                + "est_signe BOOLEAN DEFAULT FALSE, "
                + "est_archive BOOLEAN DEFAULT FALSE, "
                + "act_url VARCHAR(255), "
                + "CONSTRAINT fk_act_signataire "
                + "FOREIGN KEY (signataire_id) REFERENCES users(id)"
                + ");";


        String createDemandTable = "CREATE TABLE IF NOT EXISTS demandes ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "user_id INT NOT NULL, "
                + "numero_demande VARCHAR(100), "
                + "statut VARCHAR(50), "
                + "type_act VARCHAR(50), "
                + "act_id INT, "
                + "date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "

                + "CONSTRAINT fk_demande_user "
                + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, "

                + "CONSTRAINT fk_demande_act "
                + "FOREIGN KEY (act_id) REFERENCES actes_administratifs(id) ON DELETE SET NULL"

                + ");";


        String createAgentsPermissions = "CREATE TABLE IF NOT EXISTS agents_permissions ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "agent_id INT NOT NULL, "
                + "permission_level VARCHAR(20), "
                + "act_type VARCHAR(50), "
                + "CONSTRAINT fk_agent_permission "
                + "FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE CASCADE"
                + ");";

        try(Connection conn = DatabaseConnection.getInstance().getConnection();
                
            Statement stmt = conn.createStatement()){
            
            if (conn == null) {
        System.err.println("ERREUR : La connexion est nulle. Vérifiez que MySQL est lancé sur XAMPP.");
        return;}
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
