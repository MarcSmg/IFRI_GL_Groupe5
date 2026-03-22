package models.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumération des types d'actes académiques disponibles.
 */
public enum AdministrativeActType {

    CERTIFICAT_SCOLARITE    ("Certificat de scolarité"),
    RELEVE_NOTES            ("Relevé de notes"),
    ATTESTATION_REUSSITE    ("Attestation de réussite"),
    ATTESTATION_DIPLOME     ("Attestation de diplôme");

    private static final Map<String, AdministrativeActType> BY_LABEL = new HashMap<>();
    private final String label;

    AdministrativeActType(String label) { this.label = label; }

    static {
        for (AdministrativeActType t : values()) {
            BY_LABEL.put(t.label, t);
        }
    }

    public static AdministrativeActType valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
