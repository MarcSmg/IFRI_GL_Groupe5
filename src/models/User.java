/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Héloïse
 */
public class User {
    private int id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private Role role;
    private boolean isTemporary;
    
    //constructeur 
    // pour l'admin et agent 
    public User(String nom, String prenom, String email,Role role, String mdpTemporaire, boolean isTemp){
        this.lastName = nom;
        this.email = email;
        this.firstName = prenom;
        this.password = mdpTemporaire;
        this.role = role;
        isTemporary = isTemp;
    }
    public User(){
        
    }
    
    
    
    // les getters 
    public int getId(){
        return id;
            }
    public String getLastName(){
        return lastName;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    
    public Role getRole(){
        return role;
    }
    public boolean getIsTemporary(){
        return isTemporary;
    }
    // les setters
    public void setId(int id){
        this.id = id;
    }
    public void setNom(String nom){
        this.lastName = nom;
    }
    
    public void setPrenom(String prenom){
        this.firstName = prenom;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setRole(Role role){
        this.role = role;
    }
    public void setPassword(String mdp){
        this.password = mdp;
    }
    public void setIsTemporary(boolean temporary){
        this.isTemporary = temporary;
        
    }
}
