package models;
import java.time.LocalDateTime;
import models.enums.AdministrativeActType;

import models.enums.DemandStatus;

public class Demand {
    private int id;
    private String demandNumber;
    private DemandStatus status;
    private AdministrativeActType actType;
    private int usagerId;
    private LocalDateTime creationDate;
    
    public Demand(AdministrativeActType actType){
        this.creationDate = LocalDateTime.now();
        this.demandNumber = "";
        this.status = DemandStatus.SAVED;
        this.actType = actType;
    }
    
    
    //les getters
    public Demand() {}

    public Demand(String demandNumber, DemandStatus status) {
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

    public DemandStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public String getActType(){
        return actType.name();
    }
    
    //les setters
    
    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public void setUsagerId(int id){
        this.usagerId = id;
    }
    
    public void setTypeAct(AdministrativeActType type){
        this.actType = type;
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
