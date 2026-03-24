/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import models.*;
import java.sql.*;
import database.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Héloïse
 */
public class WhiteListDAO {
    public WhiteListEntry checkWhitelist(int matricule) {
    String sql = "SELECT * FROM white_list_usagers WHERE matricule = ?";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, matricule);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return new WhiteListEntry(
                String.valueOf(rs.getInt("matricule")),
                rs.getString("lastName"),
                rs.getString("firstName"),
                rs.getDate("birth_date").toLocalDate(),
                rs.getString("place_birth"),
                rs.getString("fieldOfStudy"),
                rs.getString("study_level")
            );
        }
    } catch (SQLException e) { 
        e.printStackTrace(); 
    }
    return null; 
}
    // apres inscription pour marque l'isncription a true
    public boolean marquerCommeInscrit(int matricule) {
        String sql = "UPDATE liste_blanche_etudiants SET est_deja_inscrit = TRUE WHERE matricule = ? AND has_account  = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, matricule);
            pstmt.setBoolean(2, false);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveAll(List<WhiteListEntry> entries) {
    String sql = "INSERT INTO white_list_usagers (matricule, last_name, first_name, birth_date, place_birth, fieldOfStudy, study_level) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        conn.setAutoCommit(false); // On désactive l'auto-commit pour gérer la transaction

        for (WhiteListEntry entry : entries) {
            pstmt.setString(1, entry.getMatricule());
            pstmt.setString(2, entry.getLastName());
            pstmt.setString(3, entry.getFirstName());
            pstmt.setDate(4, java.sql.Date.valueOf(entry.getBirthDate()));
            pstmt.setString(5, entry.getBirthPlace());
            pstmt.setString(6, entry.getFieldOfStudy());
            pstmt.setString(7, entry.getStudyLevel());
            
            pstmt.addBatch(); 
        }

        pstmt.executeBatch(); 
        conn.commit(); 
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public boolean addEntry(WhiteListEntry entry) {
    String sql = "INSERT INTO white_list_usagers (matricule, last_name, first_name, birth_date, place_birth, field_of_study, study_level) VALUES(?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, entry.getMatricule()); 
        pstmt.setString(2, entry.getLastName());
        pstmt.setString(3, entry.getFirstName());
        
        pstmt.setDate(4, java.sql.Date.valueOf(entry.getBirthDate()));
        
        pstmt.setString(5, entry.getBirthPlace());
        pstmt.setString(6, entry.getFieldOfStudy());
        pstmt.setString(7, entry.getStudyLevel());
        
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
        
        }catch (SQLIntegrityConstraintViolationException e) {
        System.err.println("Le matricule existe déjà dans la base de données !");
        return false;
            } catch (SQLException e) {
                // En cas de doublon de matricule, MySQL lèvera une exception ici
                e.printStackTrace();
                System.err.println("Erreur SQL lors de l'ajout : " + e.getMessage());
                return false;
            }
}

    public WhiteListEntry checkWhitelist(String matricule) {
        // Correction du nom de table pour être cohérent : white_list_usagers
        String sql = "SELECT * FROM white_list_usagers WHERE matricule = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, matricule); // Changement en setString
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new WhiteListEntry(
                    rs.getString("matricule"), // Récupération en String direct
                    rs.getString("lastName"),
                    rs.getString("firstName"),
                    rs.getDate("birth_date").toLocalDate(),
                    rs.getString("place_birth"),
                    rs.getString("fieldOfStudy"),
                    rs.getString("study_level")
                );
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return null; 
    }
    
    public boolean isAlreadyRegistered(String matricule) {
        String sql = "SELECT has_account FROM white_list_usagers WHERE matricule = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, matricule);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean("has_account");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
