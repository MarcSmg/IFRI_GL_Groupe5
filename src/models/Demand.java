package models;
import java.time.LocalDateTime;
import models.enums.AdministrativeActType;

import models.enums.DemandStatus;

public class Demand {
    private int id;
    private String demandNumber;
    private String status;
    private AdministrativeActType actType;
    private int usagerId;
    private LocalDateTime creationDate;
    
    public Demand(AdministrativeActType actType){
        this.creationDate = LocalDateTime.now();
        this.demandNumber = "";
        this.status = DemandStatus.SAVED.name();
        this.actType = actType;
    }
    
    
    //les getters
    public Demand() {}

    public Demand(String demandNumber, String status) {
        this.demandNumber = demandNumber;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    
    public int getUsagerId(){
        return usagerId;
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
    public String getActType(){
        return actType.name();
    }
    
    //les setters
    
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
