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
        DatabaseInitializer.prepareDatabase();
        HomePage home = new HomePage();
        JFrame frame = new JFrame("Gestion des actes administratifs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(home.getPanelPrincipal);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        

    }
}