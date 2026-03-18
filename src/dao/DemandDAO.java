package dao;

import models.Demand;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
                "SET numero_demande = ?, statut = ?" +
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
}
