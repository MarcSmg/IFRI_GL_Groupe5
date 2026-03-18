package models;

import models.enums.DemandStatus;

public class Demand {
    private int id;
    private String demandNumber;
    private String status;
    private String creationDate;

    public int getId() {
        return id;
    }

    public String getDemandNumber() {
        return demandNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void submit() {
        setStatus(DemandStatus.PENDING.name());
    }

    public void save() {
        setStatus(DemandStatus.SAVED.name());
    }

    public void reject() {
        setStatus(DemandStatus.REJECTED.name());
    }

    public void validate() {
        setStatus(DemandStatus.VALIDATED.name());
    }
}
