package test;

import controllers.NavigationController;
import controllers.SidebarController;
import models.Administrateur;
import views.MainFrame;

import java.awt.*;

public class ViewTest {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        NavigationController navigationController = new NavigationController(mainFrame);
        SidebarController sidebarController = new SidebarController(mainFrame.getSidebar(), navigationController);

        sidebarController.configureFor(new Administrateur());

        mainFrame.pack();
        mainFrame.setMinimumSize(new Dimension(900,600));
        mainFrame.getSidebar().setUserName("Admin");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
