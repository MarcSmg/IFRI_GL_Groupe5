 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import models.*;
import dao.*;
import utilities.*;
import models.enums.*;
/**
 *
 * @author Héloïse
 */
public class AdminController {
    private Administrateur adminConnecte;
    private AgentAdministratifDAO agentDAO;
    
    public AdminController(Administrateur admin){
        this.adminConnecte = admin;
        this.agentDAO = new AgentAdministratifDAO();
               
    }
    
    public boolean creerAgent(String nom, String prenom, String email, AgentFunction func){
        // generation du mot de passe 
        String mdpTemp = SecurityUtils.generateTempPassword();
        //hashage du mdp
        String mdpHache = SecurityUtils.hashPassword(mdpTemp);
       AgentAdministratif agent =  new AgentAdministratif(nom, prenom, email, func , mdpHache);
       System.out.println("Agent crée");
       try{
           
           return agentDAO.addAgent(agent);
       }catch(Exception e){
           return false;
       }
       
    }
    
    public boolean addUsagerToWhiteList(String matricule, String lastName, String firstName, String fieldOfStudy, String studyLevel ){
        
        try{
            
        }catch(Exception e){
            System.err.println("Erreur lors de l'ajout de l'étudiant sur la liste blanche" + e.getMessage());
        }
    }
}
