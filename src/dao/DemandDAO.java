package dao;

import models.Demand;
import models.enums.DemandStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import database.*;
import models.*;
import models.enums.AdministrativeActType;
import utilities.*;
import java.util.List;

public class DemandDAO extends BaseDAO<Demand> {
    public DemandDAO() {super("demandes");}

    @Override
    protected List<Demand> findEntitiesByResultSet(ResultSet resultSet) {
        List<Demand> demandsList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Demand demand = new Demand();
                demand.setId(resultSet.getInt("id"));
                demand.setDemandNumber(resultSet.getString("numero_demande"));
                String statutStr = resultSet.getString("statut");
                demand.setStatus(DemandStatus.valueOf(statutStr.toUpperCase())); 
                java.sql.Timestamp ts = resultSet.getTimestamp("date_creation");
                if (ts != null) {
                    demand.setCreationDate(ts.toLocalDateTime());
                }
                demandsList.add(demand);
            }
        } catch (SQLException e) {
            System.err.println("!! Une erreur est subvenue lors d'une collecte de données : " + e.getMessage());
        }

        return demandsList;
    }

    public int insert(Demand demand) {
        String sql = "INSERT INTO " + tableName +
                "(numero_demande, statut)" +
                "VALUES (?, ?)" +
                ";";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, "numero_demande");
            pstmt.setString(2, "statut");

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("!! Une erreur est survenue lors d'une insertion de données : " + e.getMessage());
        }
        return -1;
    }

    public int update(Demand demand) {

        String sql = "UPDATE " + tableName + " SET statut = ? WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {

            pstmt.setString(1, demand.getStatus().name());
            pstmt.setInt(2, demand.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }

        return -1;
    }

    public boolean updateStatus(int id, String status) {

        String sql = "UPDATE " + tableName + " SET statut = ? WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut : " + e.getMessage());
        }

        return false;
    }
    
    public String registerEtGenererRef(Demand d) {
    String sql = "INSERT INTO demandes (usager_id, type_acte, statut) VALUES (?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        pstmt.setInt(1, d.getUsagerId());
        pstmt.setString(2, d.getActType());
        pstmt.setString(3, "SAVE");

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            int uniqueId = rs.getInt(1); 
            return StringUtilities.formaterNumero("DEMD", uniqueId); 
        }
    } catch (SQLException e) { e.printStackTrace(); }
    return null;
}
    
    public DemandStatus seeStatus(int id){
        String sql = "SELECT statut FROM demandes WHERE id = ?";
        String statut =  null;
        try(Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
                 statut = rs.getString("statut");
                
            }
            
            
        }catch(Exception e ){
            System.err.println("Erreur lors de la lecture du statut de la demande" + e.getMessage());
        }
        if (statut != null) {
        return DemandStatus.valueOf(statut.toUpperCase());
    }
        return null;
    }

    public List<Demand> getDemandesByUserId(int userId) {
    List<Demand> liste = new ArrayList<>();
    String sql = "SELECT * FROM demandes WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Demand d = new Demand();
            d.setId(rs.getInt("id"));
            d.setUsagerId(rs.getInt("user_id"));
            d.setDemandNumber(rs.getString("numero_demande"));
            d.setStatus(DemandStatus.valueOf(rs.getString("statut")));
            AdministrativeActType type = AdministrativeActType.valueOf(rs.getString("type_act"));
            d.setTypeAct(type);
            Timestamp ts = rs.getTimestamp("date_creation");
            if (ts != null) {
                d.setCreationDate(ts.toLocalDateTime()); 
            }
            liste.add(d);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return liste;
}
}
