package controllers;

import views.MainFrame;
import views.enums.ViewName;

public class NavigationController {

    private final MainFrame mainFrame;

    public NavigationController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void goTo(ViewName viewName) {
        mainFrame.showView(viewName);
    }
}
