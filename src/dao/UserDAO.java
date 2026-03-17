/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import models.*;
import java.sql.*;
import database.*;
import utilities.*;

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
}

