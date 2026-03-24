package app;
import database.*;
import views.*;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        AppInitializer.init();
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
    }
}