package test;

import controllers.UsagerController;
import dao.DemandDAO;
import models.Demand;
import models.enums.AdministrativeActType;

public class DemandsTest {
    public static void main(String[] args) {
        DemandDAO demandDAO = new DemandDAO();
        UsagerController usagerController = new UsagerController(demandDAO);
        usagerController.createDemand(AdministrativeActType.ATTESTATION_DIPLOME, 6);
    }
}
