/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import models.*;
import java.sql.*;
import database.*;
import models.enums.Role;

import java.util.*;

/**
 *
 * @author Héloïse
 */
public class UserDAO {
    
    public UserDAO(){
        
    }

    public int insert(User user) {

        String sql = "INSERT INTO users (first_name, last_name, email, password, role, is_temporary, is_able) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setString(5, user.getRole().name());
            pstmt.setBoolean(6, user.getIsTemporary());
            pstmt.setBoolean(7, user.getIsAble());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) return -1;

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur insertion user : " + e.getMessage());
        }

        return -1;
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
                    user.setNom(rs.getString("last_name"));
                    user.setPrenom(rs.getString("first_name"));
                    String role = rs.getString("role");
                    user.setRole(Role.valueOf(role));
                    user.setPassword(rs.getString("password"));
                    user.setIsTemporary(rs.getBoolean("is_temporary"));
                    return user; 
                }
            } catch (SQLException e) { e.printStackTrace(); }
            return null;
        }

    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("last_name"));
                user.setPrenom(rs.getString("first_name"));
                String role = rs.getString("role");
                user.setRole(Role.valueOf(role));
                user.setPassword(rs.getString("password"));
                user.setIsAble(rs.getBoolean("is_able"));
                user.setIsTemporary(rs.getBoolean("is_temporary"));
                return user;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean changePassword(int id , String pwd){
        String sql = "UPDATE users SET password = ?, is_temporary= ?  WHERE id = ? ";
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

        String sql =
                "SELECT u.id, u.login, u.last_name, u.first_name, " +
                        "us.field_of_study, us.study_level, " +
                        "COUNT(d.id) AS total_demandes, " +
                        "ld.statut AS dernier_statut " +

                        "FROM users u " +

                        // join usager info
                        "LEFT JOIN usagers us ON us.user_id = u.id " +

                        // count demandes
                        "LEFT JOIN demandes d ON u.id = d.user_id " +

                        // latest demande
                        "LEFT JOIN demandes ld ON ld.id = ( " +
                        "   SELECT d2.id FROM demandes d2 " +
                        "   WHERE d2.user_id = u.id " +
                        "   ORDER BY d2.date_creation DESC " +
                        "   LIMIT 1 " +
                        ") " +

                        "GROUP BY u.id, u.login, u.last_name, u.first_name, " +
                        "us.field_of_study, us.study_level, ld.statut";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                UserSummaryDTO user = new UserSummaryDTO(
                        rs.getString("login"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("field_of_study"),
                        rs.getString("study_level"),
                        rs.getInt("total_demandes"),
                        rs.getString("dernier_statut")
                );

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean update(User user) {

        String sql = "UPDATE users SET is_able = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {

            pstmt.setBoolean(1, user.getIsAble());
            pstmt.setInt(2, user.getId());

            int rows = pstmt.executeUpdate();
            System.out.println("Rows updated = " + rows);

        } catch (SQLException e) {
            System.err.println("Erreur update user : " + e.getMessage());
        }

        return false;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM users";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur countAll users : " + e.getMessage());
        }

        return 0;
    }

    public int countActive() {
        String sql = "SELECT COUNT(*) FROM users WHERE is_able = TRUE";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur countActive users : " + e.getMessage());
        }

        return 0;
    }
    public int countInactive() {
        String sql = "SELECT COUNT(*) FROM users WHERE is_able = FALSE";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erreur countInactive users : " + e.getMessage());
        }

        return 0;
    }

    public boolean existsByRole(Role function) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, function.name());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}

