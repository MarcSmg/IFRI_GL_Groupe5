package controllers;

import models.Administrateur;
import models.AgentAdministratif;
import models.Usager;
import models.User;
import views.NavItem;
import views.SidebarPanel;
import views.enums.ViewName;

import java.util.ArrayList;
import java.util.List;

public class SidebarController {
    private SidebarPanel sidebar;
    private NavigationController navigation;

    public SidebarController(SidebarPanel sidebar, NavigationController navigation) {
        this.sidebar = sidebar;
        this.navigation = navigation;
    }

    public void configureFor(User user) {
        List<NavItem> items = new ArrayList<>();

        if (user instanceof Administrateur) {

            items.add(new NavItem("Dashboard", () -> navigation.goTo(ViewName.ADMIN_DASHBOARD)));
            items.add(new NavItem("Gestion de comptes", () -> navigation.goTo(ViewName.ACCOUNT_MANAGEMENT)));

        } else if (user instanceof AgentAdministratif) {

        } else if (user instanceof Usager) {

        }

        sidebar.setNavItems(items);
    }
}
