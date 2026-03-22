package models.enums;

/**
 * Enumération des types d'actes académiques disponibles.
 */
public enum AdministrativeActType {

    CERTIFICAT_SCOLARITE    ("Certificat de scolarité"),
    RELEVE_NOTES            ("Relevé de notes"),
    ATTESTATION_REUSSITE    ("Attestation de réussite"),
    ATTESTATION_DIPLOME     ("Attestation de diplôme");

    private final String label;

    AdministrativeActType(String label) { this.label = label; }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
