package models.enums;

/**
 * Enumération des types d'actes académiques disponibles.
 */
public enum AdministrativeActType {

    ATTESTATION_INSCRIPTION ("Attestation d'inscription"),
    CERTIFICAT_SCOLARITE    ("Certificat de scolarité"),
    RELEVE_NOTES            ("Relevé de notes"),
    ATTESTATION_REUSSITE    ("Attestation de réussite"),
    CERTIFICAT_ADMISSION    ("Certificat d'admission"),
    ATTESTATION_DIPLOME     ("Attestation de diplôme");

    private final String label;

    AdministrativeActType(String label) { this.label = label; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
