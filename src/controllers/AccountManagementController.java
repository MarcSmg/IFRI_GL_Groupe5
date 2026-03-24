package controllers;

import dao.AgentAdministratifDAO;
import dao.UserDAO;
import models.User;
import models.AgentAdministratif;
import models.enums.AgentFunction;
import models.enums.Role;
import utilities.SecurityUtils;
import views.AccountManagementView;

import java.util.List;

public class AccountManagementController {

    private AccountManagementView view;
    private UserDAO userDAO;
    private AgentAdministratifDAO agentDAO;
    private NavigationController navigation;

    public AccountManagementController(AccountManagementView view,
                                       NavigationController navigation,
                                       UserDAO userDAO,
                                       AgentAdministratifDAO agentDAO) {

        this.view = view;
        this.navigation = navigation;
        this.userDAO = userDAO;
        this.agentDAO = agentDAO;

        loadAgents();

        view.onCreateAgent(this::createAgent);
        view.onToggleStatus(this::toggleStatus);
    }

    // ---------------- LOAD ----------------
    private void loadAgents() {

        List<AgentAdministratif> agents = agentDAO.findAll();

        Object[][] data = new Object[agents.size()][5];

        for (int i = 0; i < agents.size(); i++) {
            AgentAdministratif a = agents.get(i);

            data[i][0] = a.getId();
            data[i][1] = a.getFirstName() + " " + a.getLastName();
            data[i][2] = a.getEmail();
            data[i][3] = a.getFunction().name();
            data[i][4] = a.getIsAble() ? "Actif" : "Désactivé";
        }

        view.setAgents(data);
    }

    // ---------------- CREATE ----------------
    private void createAgent() {

        String firstName = view.getFirstName();
        String lastName = view.getLastName();
        String email = view.getEmail();
        AgentFunction function = view.getSelectedFunction();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            return;
        }

        AgentAdministratif agent = new AgentAdministratif();

        agent.setPrenom(firstName);
        agent.setNom(lastName);
        agent.setEmail(email);
        agent.setFunction(function);
        agent.setRole(Role.AGENT);

        String tempPassword = generateTemporaryPassword();
        agent.setPassword(SecurityUtils.hashPassword(tempPassword));

        agent.setIsTemporary(true);
        agent.setIsAble(true);

        agentDAO.insert(agent); // ONLY this

        loadAgents();
    }

    // ---------------- TOGGLE ----------------
    private void toggleStatus() {

        int agentId = view.getSelectedUserId();
        if (agentId == -1) {
            System.err.println("Agent user id not found");
            return;
        }

        int userId = agentDAO.findUserIdByAgentId(agentId);
        User user = userDAO.findById(userId);

        if (user == null) {
            System.err.println("Agent user not found");
            return;
        }

        System.out.println(" User " + userId + " old status = " + user.getIsAble());

        boolean newStatus = !user.getIsAble();
        user.setIsAble(newStatus);

        System.out.println(" User " + userId + " new status = " + newStatus);

        System.out.println(userDAO.update(user));

        User freshUser = userDAO.findById(userId);
        System.out.println(" DB status = " + freshUser.getIsAble());

        loadAgents();
    }

    // ---------------- UTILS ----------------
    private String generateTemporaryPassword() {
        return "temp1234"; // you can improve later (random generator)
    }
}