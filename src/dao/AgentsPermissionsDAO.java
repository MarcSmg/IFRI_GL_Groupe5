/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.*;
import models.enums.AdministrativeActType;
import database.*;

/**
 *
 * @author Héloïse
 */
public class AgentsPermissionsDAO {
    
    public boolean addPermission(int agentId, AdministrativeActType actType, String level) {
    String sql = "INSERT INTO agents_permissions (agent_id, act_type, permission_level) VALUES (?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, agentId);
        pstmt.setString(2, actType.name()); 
        pstmt.setString(3, level);           
        
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
}
