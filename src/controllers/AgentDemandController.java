package controllers;

import dao.AdministrativeActDAO;
import dao.DemandDAO;
import dao.UsagerDAO;
import dao.UserDAO;
import models.Demand;
import models.enums.AdministrativeActType;
import models.enums.DemandStatus;
import views.AgentDemandView;
import views.AgentDemandDetailsView;
import views.enums.ViewName;

import java.util.List;

public class AgentDemandController {

    private List<Demand> demands;
    private AgentDemandView view;
    private NavigationController navigation;
    private DemandDAO demandDAO;
    private UserDAO userDAO;

    public AgentDemandController(AgentDemandView view,
                                 NavigationController navigation,
                                 DemandDAO demandDAO, UserDAO userDAO) {

        this.view = view;
        this.navigation = navigation;
        this.demandDAO = demandDAO;
        this.userDAO = userDAO;

        loadDemands();

        view.onViewDetails(this::openDetails);
        view.onSave(this::saveSelected);
        view.onReject(this::rejectSelected);

    }

    private void loadDemands() {
        demands = demandDAO.findAll();
        Object[][] data = new Object[demands.size()][5];


        for (int i = 0; i < demands.size(); i++) {
            Demand d = demands.get(i);

            data[i][0] = d.getId();
            data[i][1] = userDAO.findById(demands.get(i).getUsagerId()).getFullName();
            data[i][2] = AdministrativeActType.valueOf(d.getActType()).getLabel();
            data[i][3] = d.getCreationDate();
            data[i][4] = d.getStatus().getLabel();
        }

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

        int selectedId = view.getSelectedDemandId();
        if (selectedId == -1) return;

        Demand demand = demands.stream()
                .filter(d -> d.getId() == selectedId)
                .findFirst()
                .orElse(null);

        if (demand == null) return;

        AgentDemandDetailsView detailsView = new AgentDemandDetailsView();

        new DemandDetailsController(
                detailsView,
                demand,
                demandDAO,
                new AdministrativeActDAO()
        );

        navigation.addView(ViewName.DEMAND_DETAILS, detailsView);
        navigation.goTo(ViewName.DEMAND_DETAILS);
    }
}
