package controllers;

import models.User;
import models.enums.Role;
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

        if (user.getRole() == Role.ADMINISTRATEUR) {

            items.add(new NavItem("Dashboard", () -> navigation.goTo(ViewName.ADMIN_DASHBOARD)));
            items.add(new NavItem("Gestion de comptes", () -> navigation.goTo(ViewName.ACCOUNT_MANAGEMENT)));

        } else if (user.getRole() == Role.AGENT_ADMINISTRATIF) {

            items.add(new NavItem("Gestion des demandes", () -> navigation.goTo(ViewName.DEMANDS_MANAGEMENT)));

        } else if (user.getRole() == Role.USAGER) {

            items.add(new NavItem("Dashboard", () -> navigation.goTo(ViewName.USER_DASHBOARD)));

        }

        sidebar.setNavItems(items);
    }
}
