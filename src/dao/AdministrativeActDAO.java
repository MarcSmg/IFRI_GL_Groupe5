package dao;

import models.AdministrativeAct;
import models.AgentAdministratif;
import models.enums.AdministrativeActType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdministrativeActDAO extends BaseDAO<AdministrativeAct> {
    public AdministrativeActDAO() {
        super("actes_administratifs");
    }

    @Override
    protected List<AdministrativeAct> findEntitiesByResultSet(ResultSet resultSet) {

        List<AdministrativeAct> administrativeActsList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                AdministrativeAct administrativeAct = new AdministrativeAct();
                administrativeAct.setId(resultSet.getInt("id"));
                administrativeAct.setType(AdministrativeActType.valueOf(resultSet.getString("type")));
                administrativeAct.setContent(resultSet.getString("contenu"));
                administrativeAct.setSignatoryID(resultSet.getInt("id_signataire"));
                administrativeAct.setIsSigned(resultSet.getBoolean("est_signe"));
                administrativeAct.setIsArchived(resultSet.getBoolean("est_archive"));

                administrativeActsList.add(administrativeAct);
            }
        } catch (SQLException e) {
            System.err.println("!! Une erreur est subvenue lors d'une collecte de données : " + e.getMessage());
        }

        return administrativeActsList;
    }

    public int insert(AdministrativeAct act) {
        String sql = "INSERT INTO " + tableName +
                " (type, contenu, id_signataire, est_signe, est_archive)" +
                " VALUES (?, ?, ?, ?. ?)" +
                ";";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, act.getType().getLabel());
            pstmt.setString(2, act.getContent());
            pstmt.setInt(3, act.getSignatoryID());
            pstmt.setBoolean(4, act.getIsSigned());
            pstmt.setBoolean(5, act.getIsArchived());

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("!! Une erreur est survenue lors d'une insertion de données : " + e.getMessage());
        }
        return -1;
    }

    public int update(AdministrativeAct act) {
        String sql = "UPDATE " + tableName +
                " SET type = ?, contenu = ?, id_signataire = ?, est_signe = ?, est_archive = ?" +
                " WHERE id = ?" +
                ";";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, act.getType().getLabel());
            pstmt.setString(2, act.getContent());
            pstmt.setInt(3, act.getSignatoryID());
            pstmt.setBoolean(4, act.getIsSigned());
            pstmt.setBoolean(5, act.getIsArchived());
            pstmt.setInt(6, act.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("!! Une erreur est survenue lors d'une modification de données : " + e.getMessage());
        }
        return 0;
    }

    public int updateActUrl(AdministrativeAct act) {

        String sql = "UPDATE actes_administratifs " +
                "SET act_url = ? " +
                "WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {

            pstmt.setString(1, act.getActUrl());
            pstmt.setInt(2, act.getId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de act_url : " + e.getMessage());
        }

        return -1;
    }

}