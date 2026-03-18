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
public class Usager extends User {
    private String  matricule;
    public Usager(String matricule, String nom, String prenom, String email, Role role, String mdp){
       
        super(nom, prenom, email,Role.USAGER, mdp, false);
        this.matricule = matricule;
    }
    
}
