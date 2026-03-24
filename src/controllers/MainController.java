package controllers;

import dao.AgentAdministratifDAO;
import dao.DemandDAO;
import dao.UserDAO;
import models.Usager;
import models.User;
import models.enums.Role;
import views.*;
import views.enums.ViewName;

import java.awt.*;

public class MainController {
    private MainFrame mainFrame;
    private NavigationController navigation;
    private SidebarController sidebarController;
    private UserDAO userDAO;
    private DemandDAO demandDAO;
    private AgentAdministratifDAO agentAdministratifDAO;

    public MainController(MainFrame mainFrame) {

        this.mainFrame = mainFrame;
        navigation = new NavigationController(mainFrame);
        sidebarController = new SidebarController(mainFrame.getSidebar(),navigation);

        userDAO = new UserDAO();
        demandDAO = new DemandDAO();
        agentAdministratifDAO = new AgentAdministratifDAO();

    }

    public void onLoginSuccess(User user) {

        // Clear previous views (important)
        mainFrame.getContainer().removeAll();
        mainFrame.getContainer().revalidate();
        mainFrame.getContainer().repaint();

        // Setup views based on role
        if (user.getRole() == Role.USAGER) {
            setupUsagerUI((Usager) user);
        } else if (user.getRole() == Role.ADMINISTRATEUR) {
            setupAdminUI(user);
        } else if (user.getRole() == Role.AGENT) {
            setupAgentUI(user);
        }

        sidebarController.configureFor(user);
        showMainFrame(user);
    }

    private void setupUsagerUI(Usager user) {

        UsagerController usagerController = new UsagerController(demandDAO);

        DashboardUsager dashboard = new DashboardUsager(user, usagerController);

        mainFrame.addView(
                ViewName.USER_DASHBOARD,
                dashboard.getPanelPrincipal()
        );

        navigation.goTo(ViewName.USER_DASHBOARD);
    }

    private void setupAdminUI(User user) {

        AdminDashboardView adminDashboardView = new AdminDashboardView();
        AccountManagementView accountManagementView = new AccountManagementView();
        UserListContainer userListContainer = new UserListContainer(new AdminController());

        new AdminDashboardController(
                adminDashboardView,
                navigation,
                userDAO,
                demandDAO
        );

        new AccountManagementController(
                accountManagementView,
                navigation,
                userDAO,
                agentAdministratifDAO
        );

        mainFrame.addView(ViewName.ADMIN_DASHBOARD, adminDashboardView);
        mainFrame.addView(ViewName.ACCOUNT_MANAGEMENT, accountManagementView);
        mainFrame.addView(ViewName.USAGER_LIST, userListContainer.getPanelPrincipal());

        navigation.goTo(ViewName.ADMIN_DASHBOARD);
    }

    private void setupAgentUI(User user) {

        AgentDemandView agentDemandsView = new AgentDemandView();

        AgentDemandController controller = new AgentDemandController(
                agentDemandsView,
                navigation,
                demandDAO,
                userDAO
        );

        mainFrame.addView(ViewName.DEMANDS_MANAGEMENT, agentDemandsView);

        navigation.goTo(ViewName.DEMANDS_MANAGEMENT);
    }

    public void showMainFrame(User user) {

        mainFrame.pack();
        mainFrame.setMinimumSize(new Dimension(900, 600));

        mainFrame.getSidebar().setUserName(
                user.getFirstName() + " " + user.getLastName()
        );

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
