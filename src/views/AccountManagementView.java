package views;

import models.enums.AgentFunction;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import utilities.AppColors;

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
        setLayout(new BorderLayout());
        setBackground(AppColors.BG_PAGE);

        add(createHeader(), BorderLayout.NORTH);
        add(createCenter(), BorderLayout.CENTER);
        add(createActions(), BorderLayout.SOUTH);
    }

    // ───────────────── HEADER ─────────────────
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppColors.BG_PAGE);
        header.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel title = new JLabel("Gestion des comptes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(AppColors.TEXT_DARK);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    // ───────────────── CENTER ─────────────────
    private JPanel createCenter() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(AppColors.BG_PAGE);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        panel.add(createFormCard(), BorderLayout.NORTH);
        panel.add(createTableCard(), BorderLayout.CENTER);

        return panel;
    }

    // ───────────────── FORM CARD ─────────────────
    private JPanel createFormCard() {
        JPanel card = new RoundedPanel(20, AppColors.CARD_BG);
        card.setBackground(AppColors.CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(AppColors.BORDER_COLOR, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        firstNameField = createStyledField();
        lastNameField = createStyledField();
        emailField = createStyledField();

        functionCombo = new JComboBox<>();
        for (AgentFunction f : AgentFunction.values()) {
            functionCombo.addItem(f.name());
        }
        functionCombo.setPreferredSize(new Dimension(0, 36));

        createBtn = createPrimaryButton("Créer agent");

        // ROW 1
        gbc.gridy = 0;

// label
        gbc.gridx = 0;
        gbc.weightx = 0;
        form.add(createLabel("Prénom"), gbc);

// field
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(firstNameField, gbc);

// label
        gbc.gridx = 2;
        gbc.weightx = 0;
        form.add(createLabel("Nom"), gbc);

// field
        gbc.gridx = 3;
        gbc.weightx = 1;
        form.add(lastNameField, gbc);

// ROW 2
        gbc.gridy = 1;

// label
        gbc.gridx = 0;
        gbc.weightx = 0;
        form.add(createLabel("Email"), gbc);

// field
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(emailField, gbc);

// label
        gbc.gridx = 2;
        gbc.weightx = 0;
        form.add(createLabel("Fonction"), gbc);

// field
        gbc.gridx = 3;
        gbc.weightx = 1;
        form.add(functionCombo, gbc);

        // BUTTON
        gbc.gridy = 2;
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;

        form.add(createBtn, gbc);

        GridBagLayout gbl = (GridBagLayout) form.getLayout();
        gbl.columnWeights = new double[]{0, 1, 0, 1};

        card.add(form, BorderLayout.CENTER);
        return card;
    }

    // ───────────────── TABLE CARD ─────────────────
    private JPanel createTableCard() {
        JPanel card = new RoundedPanel(20, AppColors.CARD_BG);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        card.add(createTable(), BorderLayout.CENTER);
        return card;
    }

    private JScrollPane createTable() {
        String[] columns = {"ID", "Nom", "Email", "Fonction", "Statut"};

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(AppColors.CARD_SELECTED);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(AppColors.BG_PAGE);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppColors.CARD_BG);

        return scroll;
    }

    // ───────────────── ACTIONS ─────────────────
    private JPanel createActions() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(AppColors.BG_PAGE);
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        toggleStatusBtn = createSecondaryButton("Activer / Désactiver");

        panel.add(toggleStatusBtn);
        return panel;
    }

    // ───────────────── UI HELPERS ─────────────────
    private JTextField createStyledField() {
        JTextField tf = new JTextField() {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(isFocusOwner()
                        ? AppColors.BLUE_MAIN
                        : AppColors.BORDER_COLOR);

                g2.setStroke(new BasicStroke(isFocusOwner() ? 1.5f : 1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);

                g2.dispose();
            }
        };

        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(8, 12, 8, 12));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tf.setPreferredSize(new Dimension(200, 36));

        return tf;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(AppColors.TEXT_MED);
        return lbl;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getModel().isRollover()
                        ? AppColors.BLUE_HOVER
                        : AppColors.BLUE_MAIN);

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));

        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(AppColors.SECONDARY_BG);
        btn.setForeground(AppColors.SECONDARY_TEXT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ───────────────── GETTERS ─────────────────
    public String getFirstName() { return firstNameField.getText(); }
    public String getLastName() { return lastNameField.getText(); }
    public String getEmail() { return emailField.getText(); }

    public AgentFunction getSelectedFunction() {
        return AgentFunction.valueOf((String) functionCombo.getSelectedItem());
    }

    public int getSelectedUserId() {
        int row = table.getSelectedRow();
        if (row == -1) return -1;
        return (int) model.getValueAt(row, 0);
    }

    // ───────────────── ACTIONS ─────────────────
    public void onCreateAgent(Runnable action) {
        createBtn.addActionListener(e -> action.run());
    }

    public void onToggleStatus(Runnable action) {
        toggleStatusBtn.addActionListener(e -> action.run());
    }

    // ───────────────── TABLE ─────────────────
    public void setAgents(Object[][] data) {
        model.setRowCount(0);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}