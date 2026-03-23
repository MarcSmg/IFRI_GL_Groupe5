package app;
import database.*;
import views.*;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        System.out.println("1. Initialisation BDD...");
        DatabaseInitializer.prepareDatabase();
        System.out.println("2. Création de la HomePage...");
        HomePage home = new HomePage();
        System.out.println("3. Tentative d'affichage...");
        SwingUtilities.invokeLater(() -> home.setVisible(true));


        

    }
}