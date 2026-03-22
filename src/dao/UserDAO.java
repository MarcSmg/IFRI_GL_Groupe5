/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import models.*;
import java.sql.*;
import database.*;
import models.enums.Role;
import utilities.*;
import java.util.*;

/**
 *
 * @author Héloïse
 */
public class UserDAO {
    
    public UserDAO(){
        
        
        
    }    
    public User findByLoginOrEmail(String identifier) {
            String sql = "SELECT * FROM users WHERE login = ? OR email = ?";
            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, identifier);
                pstmt.setString(2, identifier);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    User user = new User();
                    user.setEmail(rs.getString("email"));
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("lastName"));
                    user.setPrenom(rs.getString("firstName"));
                    String role = rs.getString("role");
                    user.setRole(Role.valueOf(role));
                    user.setPassword(rs.getString("password"));
                    user.setIsTemporary(rs.getBoolean("is_temporary"));
                    return user; 
                }
            } catch (SQLException e) { e.printStackTrace(); }
            return null;
        }

    public boolean changePassword(int id , String pwd){
        String sql = "UPDATE users SET password = ?, is_temporary= ?,  WHERE id = ? ";
        try(Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, pwd);
            pstmt.setBoolean(2, false);
            pstmt.setInt(3, id);
            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0 ;
            
        }catch(SQLException e){
            System.err.println("Erreur lors du changement de mot de passe" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean updateUserStatus(int userId, boolean status) {
    String sql = "UPDATE users SET is_active = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setBoolean(1, status);
        pstmt.setInt(2, userId);
        
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public List<UserSummaryDTO> getAllUsersWithDemandes() {
    List<UserSummaryDTO> users = new ArrayList<>();
    
    // Requête SQL mise à jour : on sélectionne 'nom' et 'prenom' séparément
    String sql = "SELECT u.matricule, u.nom, u.prenom, u.filiere, u.niveau_etude, " +
                 "COUNT(d.id) AS total_demandes, " +
                 "(SELECT status FROM demandes d2 WHERE d2.user_id = u.id " +
                 " ORDER BY d2.date_creation DESC LIMIT 1) AS dernier_statut " +
                 "FROM users u " +
                 "LEFT JOIN demandes d ON u.id = d.user_id " +
                 "GROUP BY u.id, u.matricule, u.nom, u.prenom, u.filiere, u.niveau_etude";

    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            // On récupère les valeurs individuelles
            String lastName = rs.getString("nom");
            String firstName = rs.getString("prenom");
            
            // Création de l'objet avec les nouveaux champs firstName et lastName
            UserSummaryDTO user = new UserSummaryDTO(
                rs.getString("matricule"),
                firstName,
                lastName,
                rs.getString("filiere"),
                rs.getString("niveau_etude"),
                rs.getInt("total_demandes"),
                rs.getString("dernier_statut")
            );
            users.add(user);
        }
    } catch (SQLException e) {
        // Dans un environnement pro, il serait mieux d'utiliser un Logger
        e.printStackTrace();
    }
    return users;
}
}

