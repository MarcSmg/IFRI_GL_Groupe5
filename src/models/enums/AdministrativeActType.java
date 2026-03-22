package models.enums;

public enum AdministrativeActType {
    
    // Admission
    ATTESTATION_INSCRIPTION("Attestation d'Inscription"),
    CERTIFICAT_SCOLARITE("Certificat de Scolarité"),
    
    // Académique
    RELEVE_NOTES("Relevé de Notes"),
    ATTESTATION_REUSSITE("Attestation de Réussite"),
    CERTIFICAT_ADMISSION("Certificat d'Admission"),
    
    ATTESTATION_DIPLOME("Attestation de Diplôme");

    private final String label;

    AdministrativeActType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
