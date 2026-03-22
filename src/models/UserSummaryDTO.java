/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Héloïse
 */
public class UserSummaryDTO {
    private String matricule;
    private String firstName;
    private String lastName;
    private String filiere;
    private String studyLevel; // Nommé ainsi pour correspondre au prompt de l'UI
    private int nombreDemandes;
    private String statutDerniereDemande;

    public UserSummaryDTO(String matricule, String firstName, String lastName, String filiere, 
                          String studyLevel, int nombreDemandes, String statutDerniereDemande) {
        this.matricule = matricule;
        this.firstName = firstName;
        this.lastName = lastName;
        this.filiere = filiere;
        this.studyLevel = studyLevel;
        this.nombreDemandes = nombreDemandes;
        // gestion automatique du message si aucune demande n'est trouvée en BD
        this.statutDerniereDemande = (statutDerniereDemande == null || statutDerniereDemande.isEmpty()) 
                                     ? "Aucune demande en cours" : statutDerniereDemande;
    }

    // --- Getters ---
    public String getMatricule() { return matricule; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFiliere() { return filiere; }
    public String getStudyLevel() { return studyLevel; }
    public int getNombreDemandes() { return nombreDemandes; }
    public String getStatutDerniereDemande() { return statutDerniereDemande; }

    // --- Setters (Optionnels selon votre besoin) ---
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
    public void setStudyLevel(String studyLevel) { this.studyLevel = studyLevel; }
    public void setNombreDemandes(int nombreDemandes) { this.nombreDemandes = nombreDemandes; }
    public void setStatutDerniereDemande(String statutDerniereDemande) { this.statutDerniereDemande = statutDerniereDemande; }
}