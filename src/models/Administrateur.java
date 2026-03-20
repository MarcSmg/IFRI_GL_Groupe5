/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import models.enums.Role;

/**
 *
 * @author Héloïse
 */
public class Administrateur extends User {
    private String login;
    public Administrateur() {};

    public Administrateur(String login, String nom, String prenom, String email, Role role, String  motDePasseTemporaire){
     
        super(nom, prenom, email, Role.ADMINISTRATEUR, motDePasseTemporaire, true);
        this.login = login;
        
    }
    public void setLogin(String login){
        this.login = login ;
        
    }
    
    public String getLogin(){
        return login;
    }
    
 
}
