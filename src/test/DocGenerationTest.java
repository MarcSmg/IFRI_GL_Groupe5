package test;

import models.AdministrativeAct;

public class DocGenerationTest {
    public static void main(String[] args) {
        AdministrativeAct administrativeAct = new AdministrativeAct();
        administrativeAct.setContent("Test");
        administrativeAct.generateDocx();
    }
}
