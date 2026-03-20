package models.enums;

public enum DemandStatus {
    PENDING("En cours de traitement"),
    SAVED("Enregistrée"),
    REJECTED("Rejetée"),
    VALIDATED("Validée");
    private final String label;
    
    DemandStatus(String label){
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
    @Override
    public String toString() {
        return label;
    }
}
