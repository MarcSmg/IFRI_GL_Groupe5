package test;

import models.AdministrativeAct;
import models.enums.AdministrativeActType;

public class DocGenerationTest {
    public static void main(String[] args) {
        AdministrativeAct administrativeAct = new AdministrativeAct();
        administrativeAct.setContent("Test");
        administrativeAct.setType(AdministrativeActType.ATTESTATION_INSCRIPTION);
        administrativeAct.generateDocx();
    }
}
