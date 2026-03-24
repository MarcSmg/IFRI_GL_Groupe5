package app;

import dao.UserDAO;
import models.User;
import models.enums.Role;
import utilities.SecurityUtils;

public class AppInitializer {

    public static void init() {
        ensureAdminExists();
    }

    public static void ensureAdminExists() {
        UserDAO userDAO = new UserDAO();

        boolean adminExists = userDAO.existsByRole(Role.ADMINISTRATEUR);

        if (!adminExists) {
            User admin = new User();

            admin.setPrenom("Admin");
            admin.setNom("System");
            admin.setEmail("admin@app.com");

            admin.setPassword(SecurityUtils.hashPassword("admin123"));

            admin.setRole(Role.ADMINISTRATEUR);
            admin.setIsAble(true);

            userDAO.insert(admin);

            System.out.println("Default admin created.");
        }
    }

}