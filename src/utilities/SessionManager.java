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
    private static SessionManager instance;
    private int userId;
    private String userEmail;
    private String userRole;

    // Constructeur privé pour empêcher l'instanciation directe
    private SessionManager() {}

    // Méthode pour récupérer l'unique instance
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Méthodes pour initialiser et récupérer les infos
    public void createSession(int id, String email, String role) {
        this.userId = id;
        this.userEmail = email;
        this.userRole = role;
    }

    public void closeSession() {
        this.userId = 0;
        this.userEmail = null;
        this.userRole = null;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUserRole() { return userRole; }
}
