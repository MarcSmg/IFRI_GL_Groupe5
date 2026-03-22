package test;

import controllers.MainController;
import controllers.NavigationController;
import controllers.SidebarController;
import models.Administrateur;
import models.AgentAdministratif;
import models.Usager;
import models.enums.AgentFunction;
import models.enums.Role;
import views.MainFrame;

import java.awt.*;

public class ViewTest {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();

        MainController mainController = new MainController(mainFrame);

        Usager usager = new Usager("1511", "SOSSOU", "Bob", "sossoubob@gmail.com", Role.USAGER, "");
        Administrateur admin = new Administrateur();
        AgentAdministratif agent = new AgentAdministratif("DONAN", "Pierre", "donanp@gmail.com", AgentFunction.SECRETAIRE_GENERAL, "");
//        mainController.onLoginSuccess(usager);
//        mainController.onLoginSuccess(admin);
        mainController.onLoginSuccess(agent);
    }
}
