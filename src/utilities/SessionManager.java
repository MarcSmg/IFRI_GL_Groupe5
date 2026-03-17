/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

/**
 *
 * @author Héloïse
 */

import models.*;

public class SessionManager {
    
    private static User currentUser;
    private SessionManager() {}

    public static void setUser(User user) {
        currentUser = user;
    }

    // Pour récupérer l'utilisateur n'importe où (ex: SessionManager.getUser().getNom())
    public static User getUser() {
        return currentUser;
    }

    // Pour vérifier si quelqu'un est connecté avant d'ouvrir une fenêtre
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Pour la déconnexion
    public static void logout() {
        currentUser = null;
    }
}