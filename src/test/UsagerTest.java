package test;

import dao.UsagerDAO;
import dao.UserDAO;
import models.Usager;
import models.enums.Role;

import java.time.LocalDate;

public class UsagerTest {
    public static void main(String[] args) {
        Usager usager = new Usager("1411", "SOSSOU", "René", "rene@gmail.com", Role.USAGER, "");
        usager.setBirthDate(LocalDate.now());
        UsagerDAO usagerDAO = new UsagerDAO();
        usagerDAO.insert(usager);
    }
}
