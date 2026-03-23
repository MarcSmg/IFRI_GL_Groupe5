package models;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import models.enums.AdministrativeActType;

import models.enums.DemandStatus;

public class Demand {
    private Integer id;
    private String demandNumber;
    private DemandStatus status;
    private AdministrativeActType actType;
    private Integer actId;
    private int usagerId;
    private LocalDateTime creationDate;
    
    public Demand(AdministrativeActType actType){
        this.creationDate = LocalDateTime.now();
        this.demandNumber = "";
        this.status = DemandStatus.SAVED;
        this.actType = actType;
    }

    public Demand() {}

    public Demand(int id, String demandNumber, DemandStatus status, AdministrativeActType actType) {
        this.id = id;
        this.demandNumber = demandNumber;
        this.status = status;
        this.actType = actType;
    }

    public Demand(String demandNumber, DemandStatus status) {};

    //les getters

    public Integer getId() {
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

    public Integer getActId() {
        return actId;
    }
    //les setters
    
    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public void setId(Integer id) {
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

    public void setActId(Integer actId) {
        this.actId = actId;
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

    public void generateDemandNumberFromId() {

        LocalDate now = LocalDate.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        this.demandNumber = "D-" + datePart + "-" + String.format("%04d", id);
    }
}
