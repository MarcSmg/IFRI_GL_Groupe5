 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import dao.UserDAO;
import models.*;
import dao.*;
import utilities.*;
import models.enums.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Héloïse
 */
import java.time.LocalDate;

public class AdminController {
    private Administrateur adminConnecte;
    private AgentAdministratifDAO agentDAO;
    private WhiteListDAO whiteListDAO; 
    private UserDAO userDAO;
      private AgentsPermissionsDAO permissionsDAO;

      public AdminController() {}
    public AdminController(Administrateur admin) {
        this.adminConnecte = admin;
        this.agentDAO = new AgentAdministratifDAO();
        this.whiteListDAO = new WhiteListDAO(); 
        this.userDAO = new UserDAO();
        this.permissionsDAO = new AgentsPermissionsDAO();
    }

   
    public boolean addAgent(String nom, String prenom, String email, AgentFunction func) {
        String mdpTemp = SecurityUtils.generateTempPassword();
        String mdpHache = SecurityUtils.hashPassword(mdpTemp);
        AgentAdministratif agent = new AgentAdministratif(nom, prenom, email, func, mdpHache);
        
        
        try {
            System.out.println("Tentative de création de l'agent : " + email);
            return agentDAO.createAgent(agent);
        } catch (Exception e) {
            System.err.println("Erreur creation agent : " + e.getMessage());
            return false;
        }
    }

    public boolean addUsagerToWhiteList(String matricule, String lastName, String firstName, String fieldOfStudy, String studyLevel, LocalDate birthDate, String birthPlace) {
    WhiteListEntry entry = new WhiteListEntry(matricule, lastName, firstName, birthDate, birthPlace, fieldOfStudy, studyLevel);
    
    try {
        return whiteListDAO.addEntry(entry); 
        
    } catch (Exception e) {
        System.err.println("Erreur lors de l'ajout de l'étudiant sur la liste blanche : " + e.getMessage());
        return false;
    }
}

    
    public boolean importExcelDoc(File file) {
        List<WhiteListEntry> entries = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); 
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Extraction et création de l'objet
                WhiteListEntry entry = new WhiteListEntry(
                    String.valueOf((int) row.getCell(0).getNumericCellValue()), // matricule
                    row.getCell(1).getStringCellValue(),  // lastName
                    row.getCell(2).getStringCellValue(),  // firstName
                    row.getCell(3).getLocalDateTimeCellValue().toLocalDate(), // birthDate
                    row.getCell(4).getStringCellValue(),  // birthPlace
                    row.getCell(5).getStringCellValue(),  // fieldOfStudy
                    row.getCell(6).getStringCellValue()   // studyLevel
                );
                
                entries.add(entry); // Ajout à la liste (ton "tableau" dynamique)
            }

            return whiteListDAO.saveAll(entries);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Activer et desactiver un compte 
  public boolean enableAccount(int id ) {
      //System.out.println("L'admin " + adminConnecte.getLogin() + " active le compte : " + matricule);
      return userDAO.updateUserStatus(id, true); 
  }

  public boolean disableAccount(int id) {
      //System.out.println("L'admin " + adminConnecte.getLogin() + " suspend le compte : " + matricule);
      return userDAO.updateUserStatus(id, false);
  }
  
    public boolean donnerDroitRedaction(int agentId, AdministrativeActType type) {
        return permissionsDAO.addPermission(agentId, type, "REDACTEUR");
    }
    public boolean donnerDroitSignature(int agentId, AdministrativeActType type) {
        return permissionsDAO.addPermission(agentId, type, "SIGNATAIRE");
    }

    public List<UserSummaryDTO> chargerListeUtilisateurs() {
        return userDAO.getAllUsersWithDemandes();
    }
}
