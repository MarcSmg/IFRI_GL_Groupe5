/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.*;
import database.*;
import java.util.*;
/**
 *
 * @author Héloïse
 */
public class WhiteListDAO {
    public Map<String, String> checkWhitelist(int matricule) {
    String sql = "SELECT nom, prenom, filiere FROM liste_blanche_etudiants WHERE matricule = ? AND est_deja_inscrit = FALSE";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, matricule);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            Map<String, String> etudiant = new HashMap<>();
            etudiant.put("nom", rs.getString("nom"));
            etudiant.put("prenom", rs.getString("prenom"));
            etudiant.put("filiere", rs.getString("filiere"));
            return etudiant;
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return null; 
}
    // apres inscription pour marque l'isncription a true
    public boolean marquerCommeInscrit(int matricule) {
        String sql = "UPDATE liste_blanche_etudiants SET est_deja_inscrit = TRUE WHERE matricule = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, matricule);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
