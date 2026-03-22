package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import utilities.AppColors;

public class AgentDemandView extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> statusFilter;
    private JButton saveBtn;
    private JButton rejectBtn;
    private JButton detailsBtn;

    public AgentDemandView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createCenter(), BorderLayout.CENTER);
        add(createActions(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Gestion des demandes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        header.add(title, BorderLayout.WEST);

        header.setBackground(AppColors.HEADER_BG);

        title.setForeground(AppColors.TEXT_DARK);

        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.BORDER_COLOR),
                BorderFactory.createEmptyBorder(16, 32, 16, 32)
        ));
        return header;
    }

    private JPanel createCenter() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBackground(Color.WHITE);
        panel.add(createFilters(), BorderLayout.NORTH);
        panel.add(createTableContainer(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFilters() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        statusFilter = new JComboBox<>(new String[]{
                "Tous", "En cours", "Validé", "Rejeté"
        });

        JLabel label = new JLabel("Statut :");

        panel.add(label);
        panel.add(statusFilter);

        panel.setBackground(Color.WHITE);
        label.setForeground(AppColors.TEXT_MED);

        statusFilter.setBackground(Color.WHITE);
        statusFilter.setBorder(BorderFactory.createLineBorder(AppColors.BORDER_COLOR));

        return panel;
    }

    private JScrollPane createTable() {

        String[] columns = {"ID", "Utilisateur", "Type", "Date", "Statut"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.setBackground(Color.WHITE);
        table.setForeground(AppColors.TEXT_DARK);

        table.setSelectionBackground(AppColors.SECONDARY_BG);
        table.setSelectionForeground(AppColors.TEXT_DARK);

        table.getTableHeader().setBackground(AppColors.HEADER_BG);
        table.getTableHeader().setForeground(AppColors.TEXT_DARK);
        table.getTableHeader().setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppColors.BORDER_COLOR)
        );

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;

    }

    private JPanel createTableContainer() {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(AppColors.CARD_BG);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(AppColors.BORDER_COLOR, 1, true)
        ));

        card.add(createTable(), BorderLayout.CENTER);

        return card;
    }

    private JPanel createActions() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        saveBtn = createPrimaryButton("Enregistrer");
        rejectBtn = createPrimaryButton("Rejeter");
        detailsBtn = createPrimaryButton("Voir plus");

        panel.add(detailsBtn);
        panel.add(saveBtn);
        panel.add(rejectBtn);


        panel.setBackground(Color.WHITE);

        return panel;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);

        btn.setBackground(AppColors.BLUE_MAIN);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    public void setDemands(Object[][] data) {
        model.setRowCount(0);

        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    public int getSelectedDemandId() {
        int row = table.getSelectedRow();
        if (row == -1) return -1;

        return (int) model.getValueAt(row, 0);
    }

    public void onSave(Runnable action) {
        saveBtn.addActionListener(e -> action.run());
    }

    public void onReject(Runnable action) {
        rejectBtn.addActionListener(e -> action.run());
    }

    public void onViewDetails(Runnable action) {
        detailsBtn.addActionListener(e -> action.run());
    }

    public String getSelectedStatusFilter() {
        return (String) statusFilter.getSelectedItem();
    }
}