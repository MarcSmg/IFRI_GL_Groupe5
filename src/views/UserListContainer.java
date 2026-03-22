package views;

import controllers.AdminController;
import models.UserSummaryDTO;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Composant réutilisable — liste d'utilisateurs en mode Card View.
 *
 * Usage:
 *   UserListContainer container = new UserListContainer();
 *   parentPanel.add(container.getPanelPrincipal(), BorderLayout.CENTER);
 *
 * API publique :
 *   getPanelPrincipal()  → JPanel prêt à ajouter
 *   refresh()            → recharge les données depuis AdminController
 *   getSelectedUser()    → UserSummaryDTO sélectionné ou null
 */
public class UserListContainer extends JPanel {

    // ════════════════════════════════════════════════════════════════════════
    //  Palette de couleurs
    // ════════════════════════════════════════════════════════════════════════
    private static final Color COLOR_BG_PAGE       = new Color(245, 247, 251);
    private static final Color COLOR_CARD_BG       = Color.WHITE;
    private static final Color COLOR_CARD_SELECTED = new Color(232, 242, 255);
    private static final Color COLOR_CARD_BORDER   = new Color(225, 228, 235);
    private static final Color COLOR_CARD_SEL_BORDER = new Color(33, 119, 240);

    private static final Color COLOR_NAME          = new Color(15,  15,  20);
    private static final Color COLOR_MATRICULE     = new Color(140, 140, 150);
    private static final Color COLOR_META          = new Color(90,  95, 110);
    private static final Color COLOR_STATUT_NONE   = new Color(175, 178, 185);

    // Couleurs de badge par statut
    private static final Color BADGE_VALIDE        = new Color(22,  163,  74);
    private static final Color BADGE_EN_COURS      = new Color(33,  119, 240);
    private static final Color BADGE_ATTENTE       = new Color(234, 105,  10);
    private static final Color BADGE_REJETEE       = new Color(220,  38,  38);
    private static final Color BADGE_DEFAULT       = new Color(100, 116, 139);

    // ════════════════════════════════════════════════════════════════════════
    //  Champs
    // ════════════════════════════════════════════════════════════════════════
    private final AdminController adminController;
    private final JTable          table;
    private final UserTableModel  tableModel;
    private final JLabel          countLabel;

    // ════════════════════════════════════════════════════════════════════════
    //  Constructeur
    // ════════════════════════════════════════════════════════════════════════
    public UserListContainer() {
        super(new BorderLayout());
        setBackground(COLOR_BG_PAGE);

        adminController = new AdminController();
        tableModel      = new UserTableModel();

        // ── En-tête ──────────────────────────────────────────────────────
        JPanel header = buildHeader();
        countLabel = findCountLabel(header);

        // ── Table ────────────────────────────────────────────────────────
        table = buildTable();

        // ── ScrollPane invisible ─────────────────────────────────────────
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.setBackground(COLOR_BG_PAGE);
        scroll.getViewport().setBackground(COLOR_BG_PAGE);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel scrollWrapper = new JPanel(new BorderLayout());
        scrollWrapper.setBackground(COLOR_BG_PAGE);
        scrollWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));
        scrollWrapper.add(scroll, BorderLayout.CENTER);

        add(header,        BorderLayout.NORTH);
        add(scrollWrapper, BorderLayout.CENTER);

        // ── Chargement initial ───────────────────────────────────────────
        loadData();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Construction des sous-composants
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_BG_PAGE);
        header.setBorder(new EmptyBorder(26, 24, 14, 24));

        // Côté gauche : titre + sous-titre
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(COLOR_BG_PAGE);

        JLabel titleLbl = new JLabel("Utilisateurs");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLbl.setForeground(COLOR_NAME);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Compteur — nommé pour être retrouvé après chargement
        JLabel count = new JLabel("Chargement…");
        count.setName("__countLabel__");
        count.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        count.setForeground(COLOR_MATRICULE);
        count.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(titleLbl);
        left.add(Box.createVerticalStrut(3));
        left.add(count);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    /** Parcourt le header pour retrouver le JLabel compteur par son name. */
    private JLabel findCountLabel(JPanel header) {
        for (Component c : header.getComponents()) {
            if (c instanceof JPanel p) {
                for (Component cc : p.getComponents()) {
                    if (cc instanceof JLabel lbl && "__countLabel__".equals(lbl.getName()))
                        return lbl;
                }
            }
        }
        return new JLabel(); // fallback silencieux
    }

    private JTable buildTable() {
        JTable t = new JTable(tableModel);

        // Masque tout ce qui n'est pas une carte
        t.setTableHeader(null);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setBackground(COLOR_BG_PAGE);
        t.setRowHeight(116);
        t.setFocusable(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Renderer personnalisé
        t.getColumnModel().getColumn(0).setCellRenderer(new UserCardRenderer());

        return t;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Données
    // ════════════════════════════════════════════════════════════════════════

    private void loadData() {
        List<UserSummaryDTO> users = adminController.chargerListeUtilisateurs();
        tableModel.setData(users);

        int n = (users == null) ? 0 : users.size();
        if (countLabel != null)
            countLabel.setText(n + " utilisateur" + (n > 1 ? "s" : ""));
    }

    // ════════════════════════════════════════════════════════════════════════
    //  API publique
    // ════════════════════════════════════════════════════════════════════════

    /** Retourne le JPanel principal prêt à être intégré dans une JFrame/JDialog. */
    public JPanel getPanelPrincipal() { return this; }

    /** Recharge la liste depuis le controller (utile après un CRUD). */
    public void refresh() { loadData(); }

    /** Retourne l'utilisateur dont la carte est sélectionnée, ou null. */
    public UserSummaryDTO getSelectedUser() {
        int row = table.getSelectedRow();
        return (row >= 0) ? tableModel.getUser(row) : null;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Modèle de table (1 colonne → 1 UserSummaryDTO par ligne)
    // ════════════════════════════════════════════════════════════════════════
    private static class UserTableModel extends AbstractTableModel {

        private List<UserSummaryDTO> data = new ArrayList<>();

        void setData(List<UserSummaryDTO> list) {
            this.data = (list != null) ? list : new ArrayList<>();
            fireTableDataChanged();
        }

        UserSummaryDTO getUser(int row) { return data.get(row); }

        @Override public int      getRowCount()                  { return data.size(); }
        @Override public int      getColumnCount()                { return 1; }
        @Override public Object   getValueAt(int r, int c)       { return data.get(r); }
        @Override public Class<?> getColumnClass(int c)          { return UserSummaryDTO.class; }
        @Override public boolean  isCellEditable(int r, int c)   { return false; }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Renderer : instancie un UserCardPanel par ligne
    // ════════════════════════════════════════════════════════════════════════
    private static class UserCardRenderer implements TableCellRenderer {

        // Un seul panneau réutilisé (pattern flyweight du renderer Swing)
        private final UserCardPanel cardPanel = new UserCardPanel();

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object  value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int     row,
                                                       int     col) {
            if (value instanceof UserSummaryDTO dto)
                cardPanel.populate(dto, isSelected);
            return cardPanel;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Carte visuelle d'un utilisateur
    // ════════════════════════════════════════════════════════════════════════
    static class UserCardPanel extends JPanel {

        // ── Composants ───────────────────────────────────────────────────
        private final JLabel nameLbl       = new JLabel();
        private final JLabel matriculeLbl  = new JLabel();
        private final JLabel filiereLbl    = new JLabel();
        private final JLabel studyLvlLbl   = new JLabel();
        private final JLabel demandesLbl   = new JLabel();
        private final JLabel statutLbl     = new JLabel();

        // État courant pour repaintComponent
        private boolean cardSelected = false;

        // ── Construction ─────────────────────────────────────────────────
        UserCardPanel() {
            setLayout(new BorderLayout(0, 0));
            setOpaque(false); // fond peint manuellement

            // Espace entre la bordure du JTable et la carte dessinée
            setBorder(new EmptyBorder(5, 8, 5, 8));

            // ── Contenu gauche ───────────────────────────────────────────
            JPanel content = new JPanel(new GridBagLayout());
            content.setOpaque(false);
            content.setBorder(new EmptyBorder(14, 20, 14, 14));

            GridBagConstraints g = new GridBagConstraints();
            g.anchor = GridBagConstraints.WEST;
            g.insets  = new Insets(0, 0, 0, 0);
            g.fill    = GridBagConstraints.HORIZONTAL;
            g.weightx = 1.0;
            g.gridx   = 0;

            // Ligne 0 — Nom complet (DUPONT Prénom)
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            nameLbl.setForeground(COLOR_NAME);
            g.gridy = 0;
            content.add(nameLbl, g);

            // Ligne 1 — Matricule discret
            content.add(Box.createVerticalStrut(4), makeStrut(g, 1));
            matriculeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            matriculeLbl.setForeground(COLOR_MATRICULE);
            g.gridy = 2;
            content.add(matriculeLbl, g);

            // Ligne 2 — Filière · Niveau
            content.add(Box.createVerticalStrut(6), makeStrut(g, 3));
            JPanel filiereRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            filiereRow.setOpaque(false);
            filiereLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            filiereLbl.setForeground(COLOR_META);
            JLabel dot = new JLabel("  ·  ");
            dot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dot.setForeground(new Color(200, 203, 210));
            studyLvlLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            studyLvlLbl.setForeground(COLOR_META);
            filiereRow.add(filiereLbl);
            filiereRow.add(dot);
            filiereRow.add(studyLvlLbl);
            g.gridy = 4;
            content.add(filiereRow, g);

            // Ligne 3 — Nombre de demandes
            content.add(Box.createVerticalStrut(4), makeStrut(g, 5));
            demandesLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            demandesLbl.setForeground(new Color(170, 173, 180));
            g.gridy = 6;
            content.add(demandesLbl, g);

            // ── Contenu droit : badge statut ─────────────────────────────
            JPanel badgeWrapper = new JPanel(new GridBagLayout());
            badgeWrapper.setOpaque(false);
            badgeWrapper.setBorder(new EmptyBorder(0, 0, 0, 20));

            statutLbl.setOpaque(false);
            badgeWrapper.add(statutLbl);

            add(content,      BorderLayout.CENTER);
            add(badgeWrapper, BorderLayout.EAST);
        }

        /** Utilitaire : crée un strut vertical sans modifier gridx. */
        private GridBagConstraints makeStrut(GridBagConstraints base, int gridy) {
            GridBagConstraints c = (GridBagConstraints) base.clone();
            c.gridy   = gridy;
            c.weighty = 0;
            return c;
        }

        // ── Remplissage ──────────────────────────────────────────────────
        void populate(UserSummaryDTO dto, boolean selected) {
            this.cardSelected = selected;

            // Nom : DUPONT Héloïse
            nameLbl.setText(dto.getLastName().toUpperCase() + "  " + dto.getFirstName());

            matriculeLbl.setText(dto.getMatricule());
            filiereLbl.setText(dto.getFiliere());
            studyLvlLbl.setText(dto.getStudyLevel());

            int nb = dto.getNombreDemandes();
            demandesLbl.setText(nb == 0
                    ? "Aucune demande enregistrée"
                    : nb + " demande" + (nb > 1 ? "s" : "") + " enregistrée" + (nb > 1 ? "s" : ""));

            applyStatut(dto.getStatutDerniereDemande());
            repaint();
        }

        private void applyStatut(String statut) {
            boolean aucune = statut == null
                    || statut.isBlank()
                    || statut.equalsIgnoreCase("Aucune demande en cours");

            if (aucune) {
                statutLbl.setText("Aucune demande en cours");
                statutLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                statutLbl.setForeground(COLOR_STATUT_NONE);
                statutLbl.setBorder(new EmptyBorder(4, 0, 4, 0));
            } else {
                Color badgeColor = resolveBadgeColor(statut);
                statutLbl.setText(statut);
                statutLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
                statutLbl.setForeground(badgeColor);
                statutLbl.setBorder(new RoundedBadgeBorder(badgeColor));
            }
        }

        private Color resolveBadgeColor(String s) {
            String lower = s.toLowerCase();
            if (lower.contains("valid"))   return BADGE_VALIDE;
            if (lower.contains("cours"))   return BADGE_EN_COURS;
            if (lower.contains("attente")) return BADGE_ATTENTE;
            if (lower.contains("rejet"))   return BADGE_REJETEE;
            return BADGE_DEFAULT;
        }

        // ── Dessin personnalisé de la carte ──────────────────────────────
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            // Zone réelle de la carte (en tenant compte de l'EmptyBorder)
            int shadow = 3;
            int pad    = 8; // correspond à EmptyBorder(5,8,5,8)
            int x = pad,         y = 5;
            int w = getWidth()  - pad * 2 - 1;
            int h = getHeight() - 10;
            int arc = 14;

            // Ombre portée légère
            g2.setColor(new Color(0, 0, 0, cardSelected ? 14 : 8));
            g2.fill(new RoundRectangle2D.Float(x + 1, y + shadow,
                    w, h, arc, arc));

            // Fond de la carte
            g2.setColor(cardSelected ? COLOR_CARD_SELECTED : COLOR_CARD_BG);
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, arc, arc));

            // Bordure
            if (cardSelected) {
                g2.setStroke(new BasicStroke(1.6f));
                g2.setColor(COLOR_CARD_SEL_BORDER);
            } else {
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(COLOR_CARD_BORDER);
            }
            g2.draw(new RoundRectangle2D.Float(x + .5f, y + .5f,
                    w - 1, h - 1, arc, arc));

            g2.dispose();
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Bordure arrondie pour le badge de statut
    // ════════════════════════════════════════════════════════════════════════
    private static class RoundedBadgeBorder extends AbstractBorder {
        private final Color accent;
        RoundedBadgeBorder(Color accent) { this.accent = accent; }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            // Fond teinté
            g2.setColor(new Color(accent.getRed(), accent.getGreen(),
                                  accent.getBlue(), 20));
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, h, h));
            // Contour
            g2.setColor(new Color(accent.getRed(), accent.getGreen(),
                                  accent.getBlue(), 70));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(x + .5f, y + .5f,
                    w - 1, h - 1, h, h));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 14, 5, 14);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Main de test
    //  Branchez votre vrai AdminController et UserSummaryDTO pour intégration.
    //  Pour tester en standalone, créez des stubs dans les bons packages.
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            JFrame frame = new JFrame("Test — UserListContainer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(740, 640);
            frame.setMinimumSize(new Dimension(500, 400));
            frame.setLocationRelativeTo(null);

            UserListContainer container = new UserListContainer();
            frame.setContentPane(container.getPanelPrincipal());
            frame.setVisible(true);

            // Démo : affiche l'utilisateur sélectionné dans la console
            container.table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    UserSummaryDTO sel = container.getSelectedUser();
                    if (sel != null)
                        System.out.println("Sélectionné : "
                                + sel.getLastName().toUpperCase()
                                + " " + sel.getFirstName()
                                + " — " + sel.getStatutDerniereDemande());
                }
            });
        });
    }
}
