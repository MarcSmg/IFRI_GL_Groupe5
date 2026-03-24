package views;

import utilities.AppColors;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SidebarPanel extends JPanel {
    private JPanel menuContainer;
    private JLabel userNameLabel;
    private JButton selectedButton;

    public SidebarPanel() {

        setBackground(AppColors.BG_PAGE);
        setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        // Main layout of sidebar
        setLayout(new BorderLayout());

        // Optional: styling
        setPreferredSize(new Dimension(200, 0));

        // Container for menu items (vertical)
        menuContainer = new JPanel();
        menuContainer.setBackground(AppColors.BG_PAGE);
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

        JLabel section = new JLabel("Navigation");
        section.setFont(new Font("Segoe UI", Font.BOLD, 12));
        section.setForeground(AppColors.TEXT_MED);
        section.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        menuContainer.add(section);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userNameLabel.setForeground(AppColors.TEXT_MED);
        userNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JSeparator sep = new JSeparator();
        sep.setForeground(AppColors.BORDER_COLOR);
        bottomPanel.add(sep);

        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(userNameLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setNavItems(List<NavItem> items) {
        menuContainer.removeAll();

        for (NavItem item : items) {
            JButton btn = createButton(item);

            btn.addActionListener(e -> {
                if (selectedButton != null) {
                    selectedButton.setForeground(AppColors.TEXT_DARK);
                }

                selectedButton = btn;
                btn.setForeground(AppColors.BLUE_MAIN);

                item.getAction().run();
            });

            menuContainer.add(btn);
            menuContainer.add(Box.createVerticalStrut(10));

            System.out.println("Button added to Sidebar : " + btn.getText() + "\nbuttons : ");
        }

        revalidate();
        repaint();
    }

    private JButton createButton(NavItem item) {

        JButton button = new JButton(item.getLabel()) {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2.setColor(AppColors.CARD_SELECTED);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        // Text styling
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(AppColors.TEXT_DARK);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // Remove default button look
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        // Padding inside button
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 10));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    public void setUserName(String userName) {
        userNameLabel.setText("Utilisateur : " + userName);
    }
}
