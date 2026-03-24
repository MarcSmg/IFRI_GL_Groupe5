package controllers;

import dao.AgentAdministratifDAO;
import dao.UserDAO;
import models.User;
import models.AgentAdministratif;
import models.enums.AgentFunction;
import models.enums.Role;
import views.AccountManagementView;

import java.util.List;

public class AccountManagementController {

    private AccountManagementView view;
    private UserDAO userDAO;
    private AgentAdministratifDAO agentDAO;

    public AccountManagementController(AccountManagementView view,
                                       UserDAO userDAO,
                                       AgentAdministratifDAO agentDAO) {

        this.view = view;
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

        // 1. Create user
        User user = new User();
        user.setPrenom(firstName);
        user.setNom(lastName);
        user.setEmail(email);
        user.setRole(Role.AGENT_ADMINISTRATIF);

        // Temporary password
        String tempPassword = generateTemporaryPassword();
        user.setPassword(tempPassword);

        user.setIsTemporary(true);
        user.setIsAble(true);

        int userId = userDAO.insert(user);
        if (userId == -1) return;

        // 2. Create agent
        AgentAdministratif agent = new AgentAdministratif();
        agent.setId(userId);
        agent.setFunction(function);

        agentDAO.createAgent(agent);

        // Reload table
        loadAgents();
    }

    // ---------------- TOGGLE ----------------
    private void toggleStatus() {

        int userId = view.getSelectedUserId();
        if (userId == -1) return;

        User user = userDAO.findById(userId);
        if (user == null) return;

        user.setIsAble(!user.getIsAble());

        userDAO.update(user);

        loadAgents();
    }

    // ---------------- UTILS ----------------
    private String generateTemporaryPassword() {
        return "temp1234"; // you can improve later (random generator)
    }
}