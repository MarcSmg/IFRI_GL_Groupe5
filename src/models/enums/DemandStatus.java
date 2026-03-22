package models.enums;

import java.util.HashMap;
import java.util.Map;

public enum DemandStatus {
    PENDING("En cours de traitement"),
    SAVED("Enregistré"),
    REJECTED("Rejetée"),
    VALIDATED("Validée");

    private static final Map<String, DemandStatus> BY_LABEL = new HashMap<>();
    private final String label;

    DemandStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    static {
        for (DemandStatus s : values()) {
            BY_LABEL.put(s.label, s);
        }
    }

    public static DemandStatus valueOfLabel(DemandStatus label) {
        return BY_LABEL.get(label);
    }
}
