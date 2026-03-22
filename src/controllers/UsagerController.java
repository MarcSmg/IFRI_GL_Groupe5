/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import models.enums.AdministrativeActType;
import models.enums.DemandStatus;
        
import models.*;
import dao.*;
import java.util.*;

/**
 *
 * @author Héloïse
 */
public class UsagerController {
    
    private DemandDAO demandDAO;
    public UsagerController(DemandDAO demandDAO){
        this.demandDAO = demandDAO;
    }
    
    public boolean createDemand(AdministrativeActType actType){
        try{
            Demand d = new Demand(actType);
            String numeroDemande = demandDAO.registerEtGenererRef(d);
            d.setDemandNumber(numeroDemande);
            
            
           return true;
        }catch(Exception e){
            System.err.println("Erreur lors de la création de demande");
            return false;
        }
    }
    
    public String consulterEtatDemand(int id){
        try{
            DemandStatus statutDB = demandDAO.seeStatus(id);
            String statut = statutDB.getLabel();
            return statut;             
        }catch(Exception e){
            System.err.println("Statu non trouvé en BD");
            return null;
        }
              
        
    }

    public List<Demand> chargerDemandesUtilisateur(int userId) {
        // Logique métier si nécessaire (ex: vérifier si l'ID est valide)
        if (userId <= 0) return new ArrayList<>();
        
        return demandDAO.getDemandesByUserId(userId);
    }
}
