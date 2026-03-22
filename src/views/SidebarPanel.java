package views;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SidebarPanel extends JPanel {
    private JPanel menuContainer;
    private JLabel userNameLabel;

    public SidebarPanel() {

        setBackground(Color.WHITE);

        // Main layout of sidebar
        setLayout(new BorderLayout());

        // Optional: styling
        setPreferredSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Container for menu items (vertical)
        menuContainer = new JPanel();
        menuContainer.setBackground(Color.WHITE);
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        // Align items to the top
        menuContainer.setAlignmentY(Component.TOP_ALIGNMENT);
        // Optional spacing
        menuContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Scroll support (important if many items)
        JScrollPane scrollPane = new JScrollPane(menuContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        userNameLabel = new JLabel();
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(new JSeparator());
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(userNameLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setNavItems(List<NavItem> items) {
        menuContainer.removeAll();

        for (NavItem item : items) {
            JButton btn = createButton(item);

            btn.addActionListener(e -> item.getAction().run());

            menuContainer.add(btn);
            menuContainer.add(Box.createVerticalStrut(10));

            System.out.println("Button added to Sidebar : " + btn.getText() + "\nbuttons : ");
        }

        revalidate();
        repaint();
    }

    private JButton createButton(NavItem item) {

        JButton button = new JButton(item.getLabel());

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Optional styling
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        button.addActionListener(e -> item.getAction().run());

        return button;
    }

    public void setUserName(String userName) {
        userNameLabel.setText("Utilisateur : " + userName);
    }
}
