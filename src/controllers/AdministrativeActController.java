package controllers;

import dao.AdministrativeActDAO;
import models.AdministrativeAct;
import models.enums.AdministrativeActType;

import java.util.List;

public class AdministrativeActController {
    AdministrativeActDAO dao;

    public List<AdministrativeAct> listAllActs() {
        return dao.findAll();
    }

    public AdministrativeAct get(int id) {
        return dao.findByID(id);
    }

}
