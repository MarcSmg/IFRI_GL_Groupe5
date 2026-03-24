
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import database.*;
import models.Usager;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
/**
 *
 * @author Héloïse
 */
public class UsagerDAO {
    private WhiteListDAO whiteListDAO;

    public int insert(Usager usager) {

        String insertUser = """
        INSERT INTO users (last_name, first_name, email, password, role, is_temporary, is_able)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        String insertUsager = """
        INSERT INTO usagers (user_id, matricule, field_of_study,
                             birth_date, place_birth, gender, nationalite)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();) {

            // Transaction (important)
            conn.setAutoCommit(false);

            // 1. Insert into users
            int userId;

            try (PreparedStatement ps = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, usager.getLastName());
                ps.setString(2, usager.getFirstName());
                ps.setString(3, usager.getEmail());
                ps.setString(4, usager.getPassword());
                ps.setString(5, usager.getRole().name());
                ps.setBoolean(6, usager.getIsTemporary());
                ps.setBoolean(7, usager.getIsAble());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                } else {
                    conn.rollback();
                    return -1;
                }
            }

            // 2. Insert into usagers
            try (PreparedStatement ps = conn.prepareStatement(insertUsager)) {

                ps.setInt(1, userId);
                ps.setString(2, usager.getMatricule());
                ps.setString(3, usager.getFieldOfStudy());
                ps.setDate(4, Date.valueOf(usager.getBirthDate()));
                ps.setString(5, usager.getBirthPlace());
                ps.setString(6, usager.getGender());
                ps.setString(7, usager.getNationalite());

                ps.executeUpdate();
            }

            // Commit transaction
            conn.commit();

            return userId;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void traiterVerificationMatricule(String matriculeStr) {
//    try {
//        int matricule = Integer.parseInt(matriculeStr);
//        Map<String, String> resultat = whiteListDAO.verifierMatricule(matricule);
//
//        if (resultat != null) {
//            // Succès : On passe les infos à la Vue pour pré-remplir
//            //vueInscription.afficherFormulaireComplet(resultat);
//        } else {
//            // Échec : Matricule inconnu ou compte déjà existant
//            //vueInscription.afficherErreur("Matricule non autorisé ou déjà utilisé.");
//        }
//    } catch (NumberFormatException e) {
//        // renvoyer erreru
//        //vueInscription.afficherErreur("Le matricule doit être un nombre.");
//    }
}

    
}
