/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.*;
import models.enums.AgentFunction;
import models.enums.Role;

/**
 *
 * @author Héloïse
 */
public class AgentAdministratifDAO {
    
    public boolean createAgent(AgentAdministratif agent){
        String sql = "INSERT INTO agents(lastName, firstName, email, role, func, isTemporary) VALUES (?, ?, ?, ?, ?,?)";
        try(Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);  ){
            
            pstmt.setString(1, agent.getLastName());
            pstmt.setString(2, agent.getFirstName());
            pstmt.setString(3, agent.getEmail());
            pstmt.setString(4, agent.getRole().name());
            pstmt.setString(5, agent.getFunction().name());
            pstmt.executeUpdate();
            conn.close();
            return true;
        }catch(SQLException e){
            System.out.println("Erreur lors de l'insertion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    public byte[] getAgentSignature(int agentId) {
    String sql = "SELECT signature FROM agents WHERE id_agent = ?";
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, agentId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getBytes("signature"); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    public List<AgentAdministratif> findAll() {

        List<AgentAdministratif> agents = new ArrayList<>();

        String sql = "SELECT a.id_agent, a.function, u.* " +
                "FROM agents a " +
                "JOIN users u ON a.user_id = u.id";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                AgentAdministratif agent = new AgentAdministratif();

                agent.setId(rs.getInt("id")); // from users table
                agent.setPrenom(rs.getString("first_name"));
                agent.setNom(rs.getString("last_name"));
                agent.setEmail(rs.getString("email"));
                agent.setRole(Role.valueOf(rs.getString("role")));
                agent.setIsAble(rs.getBoolean("is_able"));
                agent.setIsTemporary(rs.getBoolean("is_temporary"));

                agent.setAgentId(rs.getInt("id_agent"));
                agent.setFunction(AgentFunction.valueOf(rs.getString("function")));

                agents.add(agent);
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll agents : " + e.getMessage());
        }

        return agents;
    }
    
    
}
