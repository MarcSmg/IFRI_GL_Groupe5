/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
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
        int id = SessionManager.getUser().getId();
        String pwdHashe = SecurityUtils.hashPassword(pwd);
       if(userDAO.changePassword(id, pwdHashe) ){
           
       }
    }
}
