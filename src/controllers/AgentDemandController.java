package controllers;

import dao.AdministrativeActDAO;
import dao.DemandDAO;
import models.Demand;
import models.enums.DemandStatus;
import views.AgentDemandView;
import views.AgentDemandDetailsView;
import views.enums.ViewName;

public class AgentDemandController {

    private AgentDemandView view;
    NavigationController navigation;
    private DemandDAO demandDAO;

    public AgentDemandController(AgentDemandView view,
                                 NavigationController navigation,
                                 DemandDAO demandDAO) {

        this.view = view;
        this.navigation = navigation;
        this.demandDAO = demandDAO;

        loadDemands();

        view.onViewDetails(this::openDetails);
        view.onSave(this::saveSelected);
        view.onReject(this::rejectSelected);

    }

    private void loadDemands() {

        Object[][] data = {
                {1, "Marc Dupont", "Attestation d'inscription", "2026-03-22", "En cours"},
                {2, "Alice Martin", "Certificat de scolarité", "2026-03-20", "Validé"},
                {3, "Jean Kossi", "Relevé de notes", "2026-03-18", "Rejeté"}
        };

        view.setDemands(data);
    }

    private void saveSelected() {
        int id = view.getSelectedDemandId();
        if (id == -1) return;

        boolean success = demandDAO.updateStatus(id, DemandStatus.PENDING.name());

        if (success) {
            loadDemands();
        }
    }

    private void rejectSelected() {
        int id = view.getSelectedDemandId();
        if (id == -1) return;

        boolean success = demandDAO.updateStatus(id, DemandStatus.REJECTED.name());
        if (success) {
            loadDemands();
        }
    }

    private void openDetails() {

        int id = view.getSelectedDemandId();
        if (id == -1) return;

        Demand demand = demandDAO.findByID(id);

        if (demand == null) {
            System.err.println("Demande introuvable pour ID = " + id);
            return;
        }


        AgentDemandDetailsView detailsView = new AgentDemandDetailsView();

        new DemandDetailsController(
                detailsView,
                demand,
                demandDAO,
                new AdministrativeActDAO()
        );

        navigation.goTo(ViewName.DEMAND_DETAILS);
    }
}
