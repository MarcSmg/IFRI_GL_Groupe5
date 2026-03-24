package controllers;

import dao.DemandDAO;
import dao.UserDAO;
import views.AdminDashboardView;
import views.enums.ViewName;

public class AdminDashboardController {

    private AdminDashboardView view;
    private UserDAO userDAO;
    private NavigationController navigation;
    private DemandDAO demandDAO;

    public AdminDashboardController(AdminDashboardView view, NavigationController navigation, UserDAO userDAO, DemandDAO demandDAO) {
        this.view = view;

        this.userDAO = new UserDAO();
        this.demandDAO = new DemandDAO();
        this.navigation = navigation;
        init();
    }

    private void init() {
        loadStats();
        bindActions();
    }

    private void loadStats() {

        int totalUsers = userDAO.countAll();
        int activeUsers = userDAO.countActive();
        int inactiveUsers = userDAO.countInactive();

        int totalDemands = demandDAO.findCountInDB();
        int pendingDemands = demandDAO.countPending();

        view.setStats(
                totalUsers,
                activeUsers,
                inactiveUsers,
                totalDemands,
                pendingDemands
        );
    }

    private void bindActions() {

        view.onManageAccounts(() -> navigation.goTo(ViewName.ACCOUNT_MANAGEMENT));

        view.onViewDemands(() -> navigation.goTo(ViewName.DEMANDS_MANAGEMENT));

        view.onManageActs(() -> navigation.goTo(ViewName.ACTES));
    }
}
