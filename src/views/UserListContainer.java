package views;

import controllers.AdminController;
import models.UserSummaryDTO;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Composant réutilisable affichant la liste des utilisateurs sous forme de cartes.
 *
 * Utilisation :
 *   UserListContainer container = new UserListContainer();
 *   somePanel.add(container.getPanelPrincipal());
 */
public class UserListContainer {

    // ════════════════════════════════════════════════════════════════════════
    //  Palette
    // ════════════════════════════════════════════════════════════════════════
    private static final Color BG_PAGE        = new Color(244, 246, 250);
    private static final Color CARD_BG        = Color.WHITE;
    private static final Color CARD_SELECTED  = new Color(235, 243, 255);

    private static final Color TXT_NAME       = new Color(18,  18,  18);
    private static final Color TXT_META       = new Color(110, 110, 120);
    private static final Color TXT_NONE       = new Color(170, 170, 175);

    private static final Color BADGE_VALIDE   = new Color(34,  197, 94);
    private static final Color BADGE_EN_COURS = new Color(33,  119, 240);
    private static final Color BADGE_ATTENTE  = new Color(249, 115, 22);
    private static final Color BADGE_REJETEE  = new Color(239, 68,  68);
    private static final Color BADGE_AUCUNE   = new Color(200, 200, 205);

    // Couleurs des boutons
    private static final Color BTN_EXPORT_BG  = new Color(33,  119, 240);
    private static final Color BTN_IMPORT_BG  = new Color(34,  197, 94);
    private static final Color BTN_FG         = Color.WHITE;

    // ════════════════════════════════════════════════════════════════════════
    //  Champs
    // ════════════════════════════════════════════════════════════════════════
    private final JPanel          panelPrincipal;
    private final AdminController adminController;
    private       JTable         table;
    private       UserTableModel tableModel;

    /** Panneau central : soit la table, soit le message "vide". */
    private final JPanel contentArea = new JPanel(new CardLayout());

    // ════════════════════════════════════════════════════════════════════════
    //  Constructeur
    // ════════════════════════════════════════════════════════════════════════
    /**
     * Constructeur principal — reçoit le controller déjà initialisé
     * (avec l'administrateur connecté et les DAOs injectés).
     */
    public UserListContainer(AdminController adminController) {
        this.adminController = adminController;
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(BG_PAGE);
        buildUI();
        loadData();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Construction de l'UI
    // ════════════════════════════════════════════════════════════════════════
    private void buildUI() {

        // ── En-tête ──────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(BG_PAGE);
        header.setBorder(new EmptyBorder(24, 28, 12, 28));

        // --- Côté gauche : titre + compteur ---
        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftSide.setOpaque(false);

        JLabel titleLbl = new JLabel("Utilisateurs");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(new Color(18, 18, 18));

        JLabel countLbl = new JLabel("Chargement…");
        countLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLbl.setForeground(TXT_META);
        countLbl.setName("countLabel");

        leftSide.add(titleLbl);
        leftSide.add(countLbl);

        // --- Côté droit : boutons Export / Import ---
        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightSide.setOpaque(false);

        JButton btnAjouter = createStyledButton("＋  Ajouter un matricule",        BTN_EXPORT_BG);
        JButton btnImport  = createStyledButton("⬆  Importer un fichier Excel",   BTN_IMPORT_BG);

        // ── Listeners ─────────────────────────────────────────────────────
        btnAjouter.addActionListener(e -> onAjouterMatricule());
        btnImport .addActionListener(e -> onImporterFichierExcel());
        // ──────────────────────────────────────────────────────────────────

        rightSide.add(btnAjouter);
        rightSide.add(btnImport);

        header.add(leftSide,  BorderLayout.WEST);
        header.add(rightSide, BorderLayout.EAST);

        // ── Table ────────────────────────────────────────────────────────
        tableModel = new UserTableModel();
        table = new JTable(tableModel);

        table.setTableHeader(null);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(BG_PAGE);
        table.setRowHeight(110);
        table.setFocusable(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setCellRenderer(new UserCardRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.setBackground(BG_PAGE);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(BG_PAGE);
        tableWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));
        tableWrapper.add(scroll, BorderLayout.CENTER);

        // ── Panneau "Aucun étudiant" ──────────────────────────────────────
        JPanel emptyPanel = buildEmptyPanel();

        // ── Zone centrale commutée par CardLayout ─────────────────────────
        contentArea.setBackground(BG_PAGE);
        contentArea.add(tableWrapper, "TABLE");
        contentArea.add(emptyPanel,   "EMPTY");

        panelPrincipal.add(header,      BorderLayout.NORTH);
        panelPrincipal.add(contentArea, BorderLayout.CENTER);
    }

    /** Crée le panneau centré affiché quand il n'y a aucun utilisateur. */
    private JPanel buildEmptyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_PAGE);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);

        // Icône (emoji unicode, pas de dépendance externe)
        JLabel iconLbl = new JLabel("👤");
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLbl.setForeground(new Color(200, 202, 210));

        JLabel msgLbl = new JLabel("Aucun étudiant inscrit");
        msgLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        msgLbl.setForeground(new Color(160, 163, 175));
        msgLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLbl = new JLabel("Les étudiants enregistrés apparaîtront ici.");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLbl.setForeground(new Color(190, 192, 200));
        subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(iconLbl);
        inner.add(Box.createVerticalStrut(14));
        inner.add(msgLbl);
        inner.add(Box.createVerticalStrut(6));
        inner.add(subLbl);

        panel.add(inner);
        return panel;
    }

    /** Fabrique un bouton arrondi stylisé avec la couleur donnée. */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond arrondi
                Color bg = getModel().isPressed()
                        ? bgColor.darker()
                        : getModel().isRollover()
                                ? bgColor.brighter()
                                : bgColor;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(BTN_FG);
        btn.setBackground(bgColor);   // nécessaire pour certains L&F
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        return btn;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Chargement des données
    // ════════════════════════════════════════════════════════════════════════
    private void loadData() {
        List<UserSummaryDTO> users = adminController.chargerListeUtilisateurs();
        tableModel.setData(users);

        // Mise à jour du compteur dans l'en-tête
        JPanel header = (JPanel) panelPrincipal.getComponent(0);
        // leftSide est le premier enfant de header (BorderLayout.WEST)
        for (Component c : header.getComponents()) {
            if (c instanceof JPanel leftSide) {
                for (Component lc : leftSide.getComponents()) {
                    if (lc instanceof JLabel lbl && "countLabel".equals(lbl.getName())) {
                        int n = users.size();
                        lbl.setText(n == 0
                                ? "Aucun utilisateur"
                                : n + " utilisateur" + (n > 1 ? "s" : ""));
                    }
                }
            }
        }

        // Affichage conditionnel : TABLE ou EMPTY
        CardLayout cl = (CardLayout) contentArea.getLayout();
        cl.show(contentArea, users.isEmpty() ? "EMPTY" : "TABLE");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Listeners des boutons
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Bouton "Ajouter un matricule" — affiche un formulaire modal stylisé.
     * Les données saisies sont transmises à adminController.addUsagerToWhiteList().
     */
    private void onAjouterMatricule() {

        // ── Dialogue modal ────────────────────────────────────────────────
        Window owner = SwingUtilities.getWindowAncestor(panelPrincipal);
        JDialog dialog = (owner instanceof Frame f)
                ? new JDialog(f, "Ajouter un étudiant", true)
                : new JDialog((Dialog) owner, "Ajouter un étudiant", true);
        dialog.setUndecorated(true);    // on dessine nous-mêmes la fenêtre
        dialog.setBackground(new Color(0, 0, 0, 0));

        // ── Panneau principal du dialogue ─────────────────────────────────
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));

        // ── Barre de titre ────────────────────────────────────────────────
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(new EmptyBorder(20, 24, 12, 24));

        JLabel titleDialogLbl = new JLabel("Ajouter un étudiant");
        titleDialogLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titleDialogLbl.setForeground(new Color(18, 18, 18));

        JLabel subTitleLbl = new JLabel("Renseignez les informations de l'étudiant");
        subTitleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitleLbl.setForeground(TXT_META);

        JPanel titlesStack = new JPanel();
        titlesStack.setLayout(new BoxLayout(titlesStack, BoxLayout.Y_AXIS));
        titlesStack.setOpaque(false);
        titlesStack.add(titleDialogLbl);
        titlesStack.add(Box.createVerticalStrut(2));
        titlesStack.add(subTitleLbl);

        // Bouton fermeture ×
        JButton closeBtn = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(240, 242, 246));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        closeBtn.setForeground(TXT_META);
        closeBtn.setOpaque(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setPreferredSize(new Dimension(28, 28));
        closeBtn.addActionListener(e -> dialog.dispose());

        titleBar.add(titlesStack, BorderLayout.CENTER);
        titleBar.add(closeBtn,    BorderLayout.EAST);

        // ── Corps du formulaire ───────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(0, 24, 16, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5, 0, 5, 8);
        gbc.weightx   = 1.0;

        // Champs
        JTextField tfMatricule   = createStyledField("Matricule *");
        JTextField tfNom         = createStyledField("Nom *");
        JTextField tfPrenom      = createStyledField("Prénom *");
        JTextField tfDateNaiss   = createStyledField("Date de naissance (jj/MM/aaaa) *");
        JTextField tfLieuNaiss   = createStyledField("Lieu de naissance *");
        JTextField tfFiliere     = createStyledField("Filière *");
        JTextField tfNiveau      = createStyledField("Niveau d'études *");

        // Ligne 0 : Matricule (pleine largeur)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(5, 0, 5, 0);
        form.add(tfMatricule, gbc);

        // Ligne 1 : Nom | Prénom
        gbc.gridwidth = 1; gbc.insets = new Insets(5, 0, 5, 8);
        gbc.gridx = 0; gbc.gridy = 1; form.add(tfNom,    gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(5, 0, 5, 0);
        form.add(tfPrenom, gbc);

        // Ligne 2 : Date naissance | Lieu naissance
        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(5, 0, 5, 8);
        form.add(tfDateNaiss, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(5, 0, 5, 0);
        form.add(tfLieuNaiss, gbc);

        // Ligne 3 : Filière | Niveau
        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(5, 0, 5, 8);
        form.add(tfFiliere, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.insets = new Insets(5, 0, 5, 0);
        form.add(tfNiveau, gbc);

        // Label d'erreur
        JLabel errorLbl = new JLabel(" ");
        errorLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        errorLbl.setForeground(BADGE_REJETEE);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(2, 0, 0, 0);
        form.add(errorLbl, gbc);

        // ── Barre de boutons ──────────────────────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);
        btnBar.setBorder(new EmptyBorder(4, 24, 20, 24));

        JButton btnAnnuler  = createStyledButton("Annuler",    new Color(230, 232, 238));
        JButton btnValider  = createStyledButton("Enregistrer", BTN_EXPORT_BG);
        btnAnnuler.setForeground(new Color(60, 60, 70));

        btnAnnuler.addActionListener(e -> dialog.dispose());

        btnValider.addActionListener(e -> {
            // ── Validation ──────────────────────────────────────────────
            String matricule = tfMatricule.getText().trim();
            String nom       = tfNom.getText().trim();
            String prenom    = tfPrenom.getText().trim();
            String dateStr   = tfDateNaiss.getText().trim();
            String lieu      = tfLieuNaiss.getText().trim();
            String filiere   = tfFiliere.getText().trim();
            String niveau    = tfNiveau.getText().trim();

            if (matricule.isEmpty() || nom.isEmpty() || prenom.isEmpty()
                    || dateStr.isEmpty() || lieu.isEmpty()
                    || filiere.isEmpty() || niveau.isEmpty()) {
                errorLbl.setText("Veuillez remplir tous les champs obligatoires.");
                return;
            }

            LocalDate dateNaiss;
            try {
                dateNaiss = LocalDate.parse(dateStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException ex) {
                errorLbl.setText("Format de date invalide. Utilisez jj/MM/aaaa.");
                tfDateNaiss.requestFocus();
                return;
            }

            // ── Appel controller ─────────────────────────────────────────
            boolean ok = adminController.addUsagerToWhiteList(
                    matricule, nom, prenom, filiere, niveau, dateNaiss, lieu);

            if (ok) {
                dialog.dispose();
                refresh();
                JOptionPane.showMessageDialog(panelPrincipal,
                        "L'étudiant a été ajouté avec succès.",
                        "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                errorLbl.setText("Échec de l'ajout. Vérifiez les données ou réessayez.");
            }
        });

        btnBar.add(btnAnnuler);
        btnBar.add(btnValider);

        // ── Séparateur bas ────────────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 232, 238));

        root.add(titleBar, BorderLayout.NORTH);
        root.add(form,     BorderLayout.CENTER);
        JPanel southStack = new JPanel(new BorderLayout());
        southStack.setOpaque(false);
        southStack.add(sep,    BorderLayout.NORTH);
        southStack.add(btnBar, BorderLayout.CENTER);
        root.add(southStack, BorderLayout.SOUTH);

        // Ombre portée via un panneau englobant légèrement plus grand
        JPanel shadow = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 8, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        shadow.setOpaque(false);
        shadow.setBorder(new EmptyBorder(6, 6, 10, 6));
        shadow.add(root);

        dialog.setContentPane(shadow);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(480, dialog.getPreferredSize().height));
        dialog.setLocationRelativeTo(panelPrincipal);
        dialog.setVisible(true);
    }

    /**
     * Fabrique un JTextField au style cohérent avec le reste de l'UI.
     * Le placeholder est effacé au premier focus.
     */
    private JTextField createStyledField(String placeholder) {
        JTextField tf = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(247, 248, 252));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isFocusOwner()
                        ? new Color(33, 119, 240, 120)
                        : new Color(220, 222, 228));
                g2.setStroke(new BasicStroke(isFocusOwner() ? 1.5f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(TXT_META);
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(8, 12, 8, 12));
        tf.setPreferredSize(new Dimension(0, 38));

        // Comportement placeholder
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(TXT_NAME);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isBlank()) {
                    tf.setText(placeholder);
                    tf.setForeground(TXT_META);
                }
            }
        });
        return tf;
    }

    /**
     * Bouton "Importer un fichier Excel" — ouvre l'explorateur de fichiers
     * puis appelle adminController.importExcelDoc().
     */
    private void onImporterFichierExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Sélectionner le fichier Excel à importer");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Fichier Excel (*.xlsx, *.xls)", "xlsx", "xls"));

        int result = chooser.showOpenDialog(panelPrincipal);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            boolean ok = adminController.importExcelDoc(file);
            if (ok) {
                refresh();
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Import réussi depuis : " + file.getName(),
                        "Import Excel", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Échec de l'import. Vérifiez le format du fichier.",
                        "Erreur d'import", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  API publique
    // ════════════════════════════════════════════════════════════════════════

    /** Recharge les données depuis le controller (utile après une opération CRUD). */
    public void refresh() { loadData(); }

    /** Retourne l'utilisateur sélectionné, ou null si aucune sélection. */
    public UserSummaryDTO getSelectedUser() {
        int row = table.getSelectedRow();
        return row >= 0 ? tableModel.getUser(row) : null;
    }

    public JPanel getPanelPrincipal() { return panelPrincipal; }

    // ════════════════════════════════════════════════════════════════════════
    //  Modèle de table — 1 colonne, 1 DTO par ligne
    // ════════════════════════════════════════════════════════════════════════
    private static class UserTableModel extends AbstractTableModel {

        private List<UserSummaryDTO> data = List.of();

        void setData(List<UserSummaryDTO> data) {
            this.data = data == null ? List.of() : data;
            fireTableDataChanged();
        }

        UserSummaryDTO getUser(int row) { return data.get(row); }

        @Override public int    getRowCount()                  { return data.size(); }
        @Override public int    getColumnCount()               { return 1; }
        @Override public Object getValueAt(int r, int c)       { return data.get(r); }
        @Override public Class<?> getColumnClass(int c)        { return UserSummaryDTO.class; }
        @Override public boolean  isCellEditable(int r, int c) { return false; }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Renderer : fabrique un UserCardPanel pour chaque ligne
    // ════════════════════════════════════════════════════════════════════════
    private static class UserCardRenderer implements TableCellRenderer {

        private final UserCardPanel card = new UserCardPanel();

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object  value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int     row,
                                                       int     column) {
            if (value instanceof UserSummaryDTO dto) {
                card.populate(dto, isSelected);
            }
            return card;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Carte visuelle d'un utilisateur
    // ════════════════════════════════════════════════════════════════════════
    static class UserCardPanel extends JPanel {

        private final JLabel nameLabel    = new JLabel();
        private final JLabel matriculeLbl = new JLabel();
        private final JLabel filiereLbl   = new JLabel();
        private final JLabel demandesLbl  = new JLabel();
        private final JLabel statutBadge  = new JLabel();

        private boolean selected = false;

        UserCardPanel() {
            setLayout(new BorderLayout(0, 0));
            setOpaque(false);

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.setOpaque(false);
            inner.setBorder(new EmptyBorder(14, 18, 14, 18));

            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            nameLabel.setForeground(TXT_NAME);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel metaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            metaRow.setOpaque(false);
            metaRow.setAlignmentX(Component.LEFT_ALIGNMENT);

            matriculeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            matriculeLbl.setForeground(TXT_META);

            JLabel sep = new JLabel("  ·  ");
            sep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            sep.setForeground(new Color(200, 200, 205));

            filiereLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            filiereLbl.setForeground(TXT_META);

            metaRow.add(matriculeLbl);
            metaRow.add(sep);
            metaRow.add(filiereLbl);

            demandesLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            demandesLbl.setForeground(new Color(160, 160, 165));
            demandesLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            inner.add(nameLabel);
            inner.add(Box.createVerticalStrut(4));
            inner.add(metaRow);
            inner.add(Box.createVerticalStrut(6));
            inner.add(demandesLbl);

            JPanel badgeWrapper = new JPanel(new GridBagLayout());
            badgeWrapper.setOpaque(false);
            badgeWrapper.setBorder(new EmptyBorder(0, 0, 0, 18));

            statutBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
            statutBadge.setOpaque(false);
            statutBadge.setBorder(new EmptyBorder(4, 12, 4, 12));

            badgeWrapper.add(statutBadge);

            add(inner,        BorderLayout.CENTER);
            add(badgeWrapper, BorderLayout.EAST);
        }

        void populate(UserSummaryDTO dto, boolean isSelected) {
            this.selected = isSelected;
            nameLabel.setText(dto.getFirstName() + " " + dto.getLastName());
            matriculeLbl.setText(dto.getMatricule());
            filiereLbl.setText(dto.getFiliere());

            int nb = dto.getNombreDemandes();
            demandesLbl.setText(nb == 0
                    ? "Aucune demande"
                    : nb + " demande" + (nb > 1 ? "s" : ""));

            applyStatutStyle(dto.getStatutDerniereDemande());
            repaint();
        }

        private void applyStatutStyle(String statut) {
            if (statut == null || statut.isBlank() || statut.equalsIgnoreCase("Aucune demande en cours")) {
                statutBadge.setText("Aucune demande en cours");
                statutBadge.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                statutBadge.setForeground(TXT_NONE);
                statutBadge.setBorder(new EmptyBorder(4, 0, 4, 0));
            } else {
                Color badgeColor = resolveBadgeColor(statut);
                statutBadge.setText(statut);
                statutBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
                statutBadge.setForeground(badgeColor);
                statutBadge.setBorder(new RoundedBadgeBorder(badgeColor, 20));
            }
        }

        private Color resolveBadgeColor(String statut) {
            String s = statut.toLowerCase();
            if (s.contains("valid"))   return BADGE_VALIDE;
            if (s.contains("cours"))   return BADGE_EN_COURS;
            if (s.contains("attente")) return BADGE_ATTENTE;
            if (s.contains("rejet"))   return BADGE_REJETEE;
            return BADGE_AUCUNE;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int pad = 6;

            g2.setColor(new Color(0, 0, 0, selected ? 18 : 10));
            g2.fill(new RoundRectangle2D.Float(pad + 1, pad + 2, w - pad * 2, h - pad * 2 - 2, 16, 16));

            g2.setColor(selected ? CARD_SELECTED : CARD_BG);
            g2.fill(new RoundRectangle2D.Float(pad, pad, w - pad * 2 - 1, h - pad * 2 - 3, 16, 16));

            g2.setColor(selected
                    ? new Color(33, 119, 240, 60)
                    : new Color(220, 222, 228, 120));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(pad + .5f, pad + .5f,
                    w - pad * 2 - 2, h - pad * 2 - 4, 16, 16));

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 110);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Bordure arrondie pour le badge de statut
    // ════════════════════════════════════════════════════════════════════════
    private static class RoundedBadgeBorder extends AbstractBorder {
        private final Color color;
        private final int   radius;

        RoundedBadgeBorder(Color color, int radius) {
            this.color  = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = new Color(color.getRed(), color.getGreen(), color.getBlue(), 22);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, radius, radius));

            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(x + .5f, y + .5f, w - 1, h - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 12, 4, 12);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Main de test
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            JFrame frame = new JFrame("Test – UserListContainer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(820, 620);
            frame.setLocationRelativeTo(null);

            // Remplacez ce stub par votre vrai Administrateur connecté
            // ex : AdminController ctrl = new AdminController(adminConnecte);
            AdminController ctrl = new AdminController(new models.Administrateur());
            UserListContainer container = new UserListContainer(ctrl);
            frame.setContentPane(container.getPanelPrincipal());
            frame.setVisible(true);
        });
    }
}
