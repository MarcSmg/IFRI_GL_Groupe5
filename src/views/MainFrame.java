package views;

import views.enums.ViewName;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout layout;
    private final JPanel container;
    private SidebarPanel sidebar;
    JPanel root;


    public MainFrame() {
        root = new JPanel(new BorderLayout());

        layout = new CardLayout();
        container = new JPanel(layout);

        sidebar = new SidebarPanel();

        root.add(sidebar, BorderLayout.WEST);
        root.add(container, BorderLayout.CENTER);

        add(root);
    }

    public JPanel getRoot() {
        return root;
    }

    public JPanel getContainer() {
        return container;
    }

    public SidebarPanel getSidebar() {
        return sidebar;
    }

    public void addView(ViewName viewName, JPanel panel) {
        container.add(panel, viewName.name());
    }

    public void showView(ViewName viewName) {
        layout.show(container, viewName.name());
    }
}
