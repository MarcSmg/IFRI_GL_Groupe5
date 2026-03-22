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
        SwingUtilities.invokeLater(() -> home.setVisible(true));


        

    }
}