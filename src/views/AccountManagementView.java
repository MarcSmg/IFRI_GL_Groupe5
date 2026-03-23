package views;

import models.enums.AgentFunction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AccountManagementView extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JComboBox<String> functionCombo;

    private JButton createBtn;
    private JButton toggleStatusBtn;

    public AccountManagementView() {
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createCenter(), BorderLayout.CENTER);
        add(createActions(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Gestion des comptes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createCenter() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        panel.add(createForm(), BorderLayout.NORTH);
        panel.add(createTable(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createForm() {

        JPanel form = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();

        functionCombo = new JComboBox<>();
        for (AgentFunction f : AgentFunction.values()) {
            functionCombo.addItem(f.name());
        }

        createBtn = new JButton("Créer agent");

        // -------- ROW 1 --------
        gbc.gridy = 0;

        gbc.gridx = 0;
        form.add(new JLabel("Prénom"), gbc);

        gbc.gridx = 1;
        form.add(firstNameField, gbc);

        gbc.gridx = 2;
        form.add(new JLabel("Nom"), gbc);

        gbc.gridx = 3;
        form.add(lastNameField, gbc);

        // -------- ROW 2 --------
        gbc.gridy = 1;

        gbc.gridx = 0;
        form.add(new JLabel("Email"), gbc);

        gbc.gridx = 1;
        form.add(emailField, gbc);

        gbc.gridx = 2;
        form.add(new JLabel("Fonction"), gbc);

        gbc.gridx = 3;
        form.add(functionCombo, gbc);

        // -------- ROW 3 (button aligned right) --------
        gbc.gridy = 2;
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;

        form.add(createBtn, gbc);

        return form;
    }

    private JScrollPane createTable() {

        String[] columns = {"ID", "Nom", "Email", "Fonction", "Statut"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        return new JScrollPane(table);
    }

    private JPanel createActions() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        toggleStatusBtn = new JButton("Activer / Désactiver");

        panel.add(toggleStatusBtn);

        return panel;
    }

    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public AgentFunction getSelectedFunction() {
        return AgentFunction.valueOf((String) functionCombo.getSelectedItem());
    }

    public int getSelectedUserId() {
        int row = table.getSelectedRow();
        if (row == -1) return -1;

        return (int) model.getValueAt(row, 0);
    }

    // actions
    public void onCreateAgent(Runnable action) {
        createBtn.addActionListener(e -> action.run());
    }

    public void onToggleStatus(Runnable action) {
        toggleStatusBtn.addActionListener(e -> action.run());
    }

    // table
    public void setAgents(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}