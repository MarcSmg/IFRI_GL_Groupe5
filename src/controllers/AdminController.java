 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import models.*;
import dao.*;
import utilities.*;
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
    
    public boolean creerAgent(String nom, String prenom, String email, Function func){
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
}
