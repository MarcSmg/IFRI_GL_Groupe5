/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import utilities.AuthResult;
import dao.*;
import models.*;
import org.mindrot.jbcrypt.BCrypt;
import utilities.*;


/**
 *
 * @author Héloïse
 */
public class AuthController {
    private final UserDAO userDAO = new UserDAO();

    // le authResult représente le resultat de la connexion 
    public AuthResult tryConnection(String identifiant, String mdpSaisi) {
        
        User user = userDAO.findByLoginOrEmail(identifiant);

        if (user == null) {
            return AuthResult.NOT_FOUND;
        }

        if (!BCrypt.checkpw(mdpSaisi, user.getPassword())) {
            return AuthResult.WRONG_PASSWORD;
        }

        SessionManager.setUser(user);

        if (user.getIsTemporary()) {
            return AuthResult.MUST_CHANGE_PASSWORD;
        }

        return AuthResult.SUCCESS;
    }
}