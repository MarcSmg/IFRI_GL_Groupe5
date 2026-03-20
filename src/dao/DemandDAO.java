package dao;

import models.Demand;

import java.sql.*;
import java.util.ArrayList;
import database.*;
import models.*;
import models.enums.DemandStatus;
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
                demand.setStatus(resultSet.getString("statut"));
                demand.setCreationDate(resultSet.getString("date_creation)"));

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
        String sql = "UPDATE " + tableName +
                "SET statut = ?" +
                "WHERE id = ?" +
                ";";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, demand.getDemandNumber());
            pstmt.setString(2, demand.getStatus());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("!! Une erreur est survenue lors d'une insertion de données : " + e.getMessage());
        }
        return -1;
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
        try(Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String statut = rs.getString("statut");
                return DemandStatus.valueOf(statut);
            }
            
            
            
        }catch(Exception e ){
            System.err.println("Erreur lors de la lecture du statut de la demande" + e.getMessage());
            return null;
        }
        
    }
}
