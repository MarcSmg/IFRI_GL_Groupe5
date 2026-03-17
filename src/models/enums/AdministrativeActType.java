package models.enums;

public enum AdministrativeActType {
    
    // Admission
    ATTESTATION_INSCRIPTION("Attestation d'Inscription"),
    CERTIFICAT_SCOLARITE("Certificat de Scolarité"),
    CARTE_ETUDIANT("Carte d'Étudiant"),
    
    // Académique
    RELEVE_NOTES("Relevé de Notes"),
    ATTESTATION_REUSSITE("Attestation de Réussite"),
    CERTIFICAT_ADMISSION("Certificat d'Admission"),
    
    ATTESTATION_DIPLOME("Attestation de Diplôme"),
    DIPLOME_DEFINITIF("Diplôme Définitif"),
    SUPPLEMENT_DIPLOME("Supplément au Diplôme"),
    
    // Divers
    ATTESTATION_BONNE_CONDUITE("Attestation de Bonne Conduite"),
    CERTIFICAT_ABANDON("Certificat d'Abandon");

    private final String label;

    AdministrativeActType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
