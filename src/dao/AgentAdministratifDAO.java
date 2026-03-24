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
        String sql = "INSERT INTO agents(last_name, first_name, email, role, func, isTemporary) VALUES (?, ?, ?, ?, ?,?)";
        try(Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);  ){
            
            pstmt.setString(1, agent.getLastName());
            pstmt.setString(2, agent.getFirstName());
            pstmt.setString(3, agent.getEmail());
            pstmt.setString(4, agent.getRole().name());
            pstmt.setString(5, agent.getFunction().name());
            pstmt.setBoolean(6, true);
            pstmt.executeUpdate();
            conn.close();
            return true;
        }catch(SQLException e){
            System.out.println("Erreur lors de l'insertion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insert(AgentAdministratif agent) {

        String insertUser = """
        INSERT INTO users (last_name, first_name, email, password, is_temporary, is_able, role)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        String insertAgent = """
        INSERT INTO agents (user_id, function)
        VALUES (?, ?)
    """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {

            conn.setAutoCommit(false);

            int userId;

            try (PreparedStatement stmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, agent.getLastName());
                stmt.setString(2, agent.getFirstName());
                stmt.setString(3, agent.getEmail());
                stmt.setString(4, agent.getPassword());

                stmt.setBoolean(5, true);  // temporary password
                stmt.setBoolean(6, true);  // active account
                stmt.setString(7, agent.getRole().name()); // must match ENUM

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        throw new SQLException("Erreur récupération ID user");
                    }
                }
            }

            // ─── 2. Insert AGENT ─────────────────────────────
            try (PreparedStatement stmt = conn.prepareStatement(insertAgent)) {

                stmt.setInt(1, userId);
                stmt.setString(2, agent.getFunction().name());

                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur insertion agent : " + e.getMessage());
        }

        return false;
    }
    
    
    public byte[] getAgentSignature(int agentId) {
    String sql = "SELECT signature FROM agents WHERE id = ?";
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

        String sql = "SELECT a.id, a.function, u.* " +
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

                agent.setAgentId(rs.getInt("id"));
                agent.setFunction(AgentFunction.valueOf(rs.getString("function")));

                agents.add(agent);
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll agents : " + e.getMessage());
        }

        return agents;
    }

    public int findUserIdByAgentId(int agentId) {

        String sql = "SELECT user_id FROM agents WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {

            pstmt.setInt(1, agentId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (SQLException e) {
            System.err.println("Erreur findUserIdByAgentId : " + e.getMessage());
        }

        return -1;
    }
    
}
