
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import database.*;
import java.util.*;
/**
/**
 *
 * @author Héloïse
 */
public class UsagerDAO {
    
    public void traiterVerificationMatricule(String matriculeStr) {
    try {
        int matricule = Integer.parseInt(matriculeStr);
        Map<String, String> resultat = whitelistDAO.verifierMatricule(matricule);

        if (resultat != null) {
            // Succès : On passe les infos à la Vue pour pré-remplir
            //vueInscription.afficherFormulaireComplet(resultat);
        } else {
            // Échec : Matricule inconnu ou compte déjà existant
            vueInscription.afficherErreur("Matricule non autorisé ou déjà utilisé.");
        }
    } catch (NumberFormatException e) {
        // renvoyer erreru
        //vueInscription.afficherErreur("Le matricule doit être un nombre.");
    }
}
    
}
