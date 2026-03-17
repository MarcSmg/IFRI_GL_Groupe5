package models;

import models.enums.DemandStatus;

public class Demand {
    private String demandNumber;
    private DemandStatus status;

    public String getDemandNumber() {
        return demandNumber;
    }

    public DemandStatus getStatus() {
        return status;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public void submit() {
        setStatus(DemandStatus.PENDING);
    }

    public void save() {
        setStatus(DemandStatus.SAVED);
    }

    public void reject() {
        setStatus(DemandStatus.REJECTED);
    }

    public void validate() {
        setStatus(DemandStatus.VALIDATED);
    }
}
