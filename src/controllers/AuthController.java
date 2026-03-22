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
import views.*;
import javax.swing.*;

/**
 *
 * @author Héloïse
 */
public class AuthController {
    private final UserDAO userDAO;
    private final WhiteListDAO whiteListDAO;
    public AuthController() {
        this.whiteListDAO = new WhiteListDAO();
        this.userDAO = new UserDAO();
    }

    // le authResult représente le resultat de la connexion 
    public AuthResult tryConnection(String identifiant, String mdpSaisi) {
        
        User user = userDAO.findByLoginOrEmail(identifiant);

        if (user == null) {
            return AuthResult.NOT_FOUND;
        }

        if (!BCrypt.checkpw(mdpSaisi, user.getPassword())) {
            return AuthResult.WRONG_PASSWORD;
        }


        if (user.getIsTemporary()) {
            return AuthResult.MUST_CHANGE_PASSWORD;
        }

        

                SessionManager.getInstance().createSession(
                    user.getId(), 
                    user.getEmail(), 
                    user.getRole().name()
                );
                System.out.println("Session créée pour l'ID : " + user.getId());
                SessionManager.getInstance().createSession(
                user.getId(), 
                user.getEmail(), 
                user.getRole().name()
            );
        return AuthResult.SUCCESS;
    }

   public User getAuthenticatedUser(String identifier) {
        return userDAO.findByLoginOrEmail(identifier);
    }
   
   public String getUserRole(String identifier) {
        User user = userDAO.findByLoginOrEmail(identifier);
        if (user != null && user.getRole() != null) {
            return user.getRole().name(); // Retourne "ADMIN", "AGENT" ou "ETUDIANT"
        }
        return "GUEST"; // Rôle par défaut si rien n'est trouvé
    }

   public void controlerMatriculeForm(MatriculeForm view) {
        view.addNextListener(e -> {
            String matricule = view.getMatricule();

            // 1. Validation de saisie
            if (matricule.isEmpty()) {
                view.afficherMessage("Veuillez entrer votre matricule.", "Champ requis", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Vérification Liste Blanche
            WhiteListEntry entry = whiteListDAO.checkWhitelist(matricule);
            if (entry == null) {
                view.afficherMessage("Matricule '" + matricule + "' non reconnu.", "Accès refusé", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Vérification si compte existant
            if (whiteListDAO.isAlreadyRegistered(matricule)) {
                view.afficherMessage("Un compte existe déjà. Redirection...", "Connexion", JOptionPane.INFORMATION_MESSAGE);
                
                NavigationManager.closeCurrent(view.getPanelPrincipal());
                NavigationManager.showConnexion();
            } else {
                // 4. Succès -> Vers Inscription avec les données de la liste blanche
                NavigationManager.closeCurrent(view.getPanelPrincipal());
                NavigationManager.showInscription(
                    entry.getLastName(), 
                    entry.getFirstName(), 
                    entry.getFieldOfStudy(), 
                    entry.getStudyLevel()
                );
            }
        });
    }


   public void controlerChangePasswordForm(ChangePasswordForm view, int userId) {
    view.addConfirmerListener(e -> {
        String nouveau = view.getNouveauPassword();
        String confirmation = view.getConfirmPassword();

        // 1. Vérification si les champs sont vides
        if (nouveau.isEmpty() || confirmation.isEmpty()) {
            JOptionPane.showMessageDialog(view.getPanelPrincipal(), 
                "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Vérification de la correspondance
        if (!nouveau.equals(confirmation)) {
            JOptionPane.showMessageDialog(view.getPanelPrincipal(), 
                "Les nouveaux mots de passe ne correspondent pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Mise à jour dans la base de données
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.changePassword(userId, nouveau);

        if (success) {
            JOptionPane.showMessageDialog(view.getPanelPrincipal(), 
                "Mot de passe modifié avec succès ! Veuillez vous reconnecter.", 
                "Succès", JOptionPane.INFORMATION_MESSAGE);

            // 4. Redirection vers la page de connexion
            NavigationManager.closeCurrent(view.getPanelPrincipal());
            NavigationManager.showConnexion();
        } else {
            JOptionPane.showMessageDialog(view.getPanelPrincipal(), 
                "Une erreur est survenue lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    });
}

   public void controlerConnexion(ConnexionForm view) {
    view.addConnexionListener(e -> {
        String identifier = view.getEmail();
        String password = view.getPassword();

        // 1. Validation de base
        if (identifier.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view.getPanelPrincipal(), 
                "Veuillez remplir tous les champs.", "Champs vides", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Recherche et Authentification via le DAO
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByLoginOrEmail(identifier);

        if (user != null && user.getPassword().equals(password)) {
            
            // 3. Création de la Session
            SessionManager.getInstance().createSession(
                user.getId(), 
                user.getEmail(), 
                user.getRole().name()
            );

            // 4. Logique de redirection
            if (user.getIsTemporary()) {
                JOptionPane.showMessageDialog(view.getPanelPrincipal(), 
                    "Première connexion : vous devez changer votre mot de passe.", 
                    "Sécurité", JOptionPane.INFORMATION_MESSAGE);
                
                NavigationManager.closeCurrent(view.getPanelPrincipal());
                NavigationManager.showChangePassword(user.getId()); 
            } else {
                NavigationManager.closeCurrent(view.getPanelPrincipal());
                // creation du main 
                MainFrame  mainFrame = new MainFrame();
                SidebarPanel  sidebarPanel = new SidebarPanel();
                NavigationController navigationController = new NavigationController(mainFrame);
                SidebarController sidebarCtrl = new SidebarController(sidebarPanel, navigationController);
                sidebarCtrl.configureFor(user);
                JPanel contenuContainer = mainFrame.getContainer();
                //
                //contenuConteneur = 
             
                mainFrame.getRoot();
            }
            
        } else {
            // CAS : Échec (Utilisateur inconnu ou mauvais MDP)
            JOptionPane.showMessageDialog(view.getPanelPrincipal(), 
                "Email ou mot de passe incorrect.", "Erreur d'authentification", JOptionPane.ERROR_MESSAGE);
        }
    });
}
}
