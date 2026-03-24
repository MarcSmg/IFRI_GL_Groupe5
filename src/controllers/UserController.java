/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import dao.UserDAO;
import dao.*;
import utilities.*;

/**
 *
 * @author Héloïse
 */
public class UserController {
    UserDAO userDAO;
    public UserController(){
        this.userDAO = new UserDAO();
    }
    
    public boolean updatePassword(String pwd){
        try{
            int id = SessionManager.getInstance().getUserId();
        String pwdHash = SecurityUtils.hashPassword(pwd);
        return userDAO.changePassword(id, pwdHash);
        }catch(Exception e){
            System.err.println("Erreur lors du changement de mot de passe" + e.getMessage());
            return false;
        }       
    }
}
