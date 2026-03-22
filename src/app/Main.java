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
<<<<<<< HEAD
        SwingUtilities.invokeLater(() -> home.setVisible(true));


=======
        JFrame frame = new JFrame("Gestion des actes administratifs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(home.getContentPane());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
>>>>>>> 7428b0a07d0210cb828529a1c0e20d5d80b0c6ae
        

    }
}