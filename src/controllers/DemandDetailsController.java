package controllers;

import dao.AdministrativeActDAO;
import dao.DemandDAO;
import models.AdministrativeAct;
import models.Demand;
import models.enums.AdministrativeActType;
import views.AgentDemandDetailsView;

import java.awt.*;
import java.io.File;

public class DemandDetailsController {

    private AgentDemandDetailsView view;
    private DemandDAO demandDAO;
    private AdministrativeActDAO actDAO;

    private Demand demand;

    public DemandDetailsController(AgentDemandDetailsView view,
                                   Demand demand,
                                   DemandDAO demandDAO,
                                   AdministrativeActDAO actDAO) {

        this.view = view;
        this.demand = demand;
        this.demandDAO = demandDAO;
        this.actDAO = actDAO;

        view.setDemandData(demand);

        checkIfActExists();

        view.onGenerateAct(this::generateAct);
        view.onOpenAct(this::openAct);
    }

    private void checkIfActExists() {
        if (demand.getId() != null) {
            view.showActAvailable();
        }
    }

    private void generateAct() {

        AdministrativeAct act = new AdministrativeAct();

        act.setType(AdministrativeActType.valueOf(demand.getActType()));
        act.setContent("Generated from demand #" + demand.getId());

        // 1. Save act in DB
        int actId = actDAO.insert(act);
        act.setId(actId);

        // 2. Generate DOCX
        boolean success = act.generateDocx();

        if (!success) return;

        // 3. Update act with URL
        actDAO.updateActUrl(act);

        // 4. Link act to demand
        demand.setActId(actId);
        demandDAO.update(demand);

        view.showActAvailable();
    }

    private void openAct() {

        AdministrativeAct act = actDAO.findByID(demand.getActId());

        try {
            Desktop.getDesktop().open(new File(act.getActUrl()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}