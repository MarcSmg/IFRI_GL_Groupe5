package controllers;

import views.MainFrame;
import views.enums.ViewName;

import javax.swing.*;

public class NavigationController {

    private final MainFrame mainFrame;

    public NavigationController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void goTo(ViewName viewName) {
        mainFrame.showView(viewName);
    }
    public void addView(ViewName viewName, JPanel panel) {mainFrame.addView(viewName, panel);}
}
