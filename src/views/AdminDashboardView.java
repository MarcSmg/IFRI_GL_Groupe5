package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminDashboardView extends JPanel {
    private JLabel totalUsersLabel = new JLabel("0");
    private JLabel activeUsersLabel = new JLabel("0");
    private JLabel inactiveUsersLabel = new JLabel("0");
    private JLabel totalDemandsLabel = new JLabel("0");
    private JLabel pendingDemandsLabel = new JLabel("0");

    private JButton manageAccountsBtn = new JButton("Manage accounts");
    private JButton viewDemandsBtn = new JButton("View demands");
    private JButton manageActsBtn = new JButton("Manage acts");

    public AdminDashboardView() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel stats = createStatsPanel();
        JPanel actions = createActionsPanel();

        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, stats.getPreferredSize().height));
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, actions.getPreferredSize().height));

        content.add(stats);
        content.add(Box.createVerticalStrut(15));
        content.add(actions);

        add(createHeader(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel title = new JLabel("Dashboard administrateur");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 30, 30));

        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // stretch horizontally
        gbc.weighty = 0; // NO vertical stretch

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStatCard("Total users", totalUsersLabel), gbc);

        gbc.gridx = 1;
        panel.add(createStatCard("Active users", activeUsersLabel), gbc);

        gbc.gridx = 2;
        panel.add(createStatCard("Inactive users", inactiveUsersLabel), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createStatCard("Total demands", totalDemandsLabel), gbc);

        gbc.gridx = 1;
        panel.add(createStatCard("Pending demands", pendingDemandsLabel), gbc);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(0, 80));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);

        return card;
    }

    private JPanel createActionsPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Color.WHITE);

        panel.add(createActionCard("Manage accounts", manageAccountsBtn));
        panel.add(createActionCard("View demands", viewDemandsBtn));
        panel.add(createActionCard("Manage acts", manageActsBtn));

        return panel;
    }

    private JPanel createActionCard(String title, JButton button) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setPreferredSize(new Dimension(200, 120));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(label);
        card.add(Box.createVerticalGlue());
        card.add(button);

        return card;
    }

    public void setStats(int totalUsers,
                         int activeUsers,
                         int inactiveUsers,
                         int totalDemands,
                         int pendingDemands) {

        totalUsersLabel.setText(String.valueOf(totalUsers));
        activeUsersLabel.setText(String.valueOf(activeUsers));
        inactiveUsersLabel.setText(String.valueOf(inactiveUsers));
        totalDemandsLabel.setText(String.valueOf(totalDemands));
        pendingDemandsLabel.setText(String.valueOf(pendingDemands));
    }

    public void onManageAccounts(Runnable action) {
        manageAccountsBtn.addActionListener(e -> action.run());
    }

    public void onViewDemands(Runnable action) {
        viewDemandsBtn.addActionListener(e -> action.run());
    }

    public void onManageActs(Runnable action) {
        manageActsBtn.addActionListener(e -> action.run());
    }
}
