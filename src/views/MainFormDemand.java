package views;
import models.enums.AdministrativeActType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Classe principale — SEULE à contenir une méthode main().
 *
 * Affiche :
 *   - Un JComboBox des types d'actes
 *   - Un bouton "OK"
 *   - Un panel central qui affiche dynamiquement le formulaire sélectionné
 */
public class MainFormDemand {

    // ════════════════════════════════════════════════════════════════════════
    //  Palette
    // ════════════════════════════════════════════════════════════════════════
    private static final Color C_BG        = new Color(244, 246, 250);
    private static final Color C_WHITE     = Color.WHITE;
    private static final Color C_BORDER    = new Color(215, 215, 215);
    private static final Color C_BLUE      = new Color(33,  119, 240);
    private static final Color C_BLUE_HOV  = new Color(21,  96,  200);
    private static final Color C_TITLE     = new Color(18,  18,  18);
    private static final Color C_SUBTITLE  = new Color(130, 130, 140);

    // ════════════════════════════════════════════════════════════════════════
    //  Composants
    // ════════════════════════════════════════════════════════════════════════
    private JFrame             frame;
    private JComboBox<AdministrativeActType> typeCombo;
    private JPanel             dynamicPanel;
    private JLabel             emptyStateLabel;

    // ════════════════════════════════════════════════════════════════════════
    //  Construction
    // ════════════════════════════════════════════════════════════════════════
    public MainFormDemand() {
        buildFrame();
    }

    private void buildFrame() {
        frame = new JFrame("Demande de documents académiques");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(620, 700);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(C_BG);
        frame.setLayout(new BorderLayout());

        frame.add(buildTopBar(),    BorderLayout.NORTH);
        frame.add(buildMainArea(),  BorderLayout.CENTER);
    }

    // ── Barre supérieure ─────────────────────────────────────────────────────

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(C_WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 235)),
                new EmptyBorder(18, 28, 18, 28)
        ));

        // Titre
        JPanel titles = new JPanel();
        titles.setBackground(C_WHITE);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Nouvelle demande");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(C_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Sélectionnez le type de document puis cliquez sur OK.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(C_SUBTITLE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titles.add(title);
        titles.add(Box.createVerticalStrut(3));
        titles.add(subtitle);

        // Sélecteur + bouton OK
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setBackground(C_WHITE);

        typeCombo = buildStyledCombo();
        controls.add(typeCombo);

        JButton okBtn = buildOkButton();
        controls.add(okBtn);

        bar.add(titles,   BorderLayout.WEST);
        bar.add(controls, BorderLayout.EAST);
        return bar;
    }

    /** JComboBox avec style moderne. */
    private JComboBox<AdministrativeActType> buildStyledCombo() {
        JComboBox<AdministrativeActType> combo = new JComboBox<>(AdministrativeActType.values());
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(C_WHITE);
        combo.setPreferredSize(new Dimension(240, 40));
        combo.setBorder(BorderFactory.createCompoundBorder(
                new BaseDocumentForm.RoundedBorder(C_BORDER, 10, 1),
                new EmptyBorder(0, 8, 0, 4)
        ));

        // Renderer pour couleur de fond cohérente
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBorder(new EmptyBorder(6, 12, 6, 12));
                if (isSelected) {
                    setBackground(new Color(235, 243, 255));
                    setForeground(C_BLUE);
                } else {
                    setBackground(C_WHITE);
                    setForeground(C_TITLE);
                }
                return this;
            }
        });

        return combo;
    }

    /** Bouton OK arrondi. */
    private JButton buildOkButton() {
        BaseDocumentForm.RoundedButton btn =
                new BaseDocumentForm.RoundedButton("OK", C_BLUE, C_BLUE_HOV, 10);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(C_WHITE);
        btn.setPreferredSize(new Dimension(72, 40));

        btn.addActionListener(e -> loadSelectedForm());
        return btn;
    }

    // ── Zone centrale ─────────────────────────────────────────────────────────

    private JPanel buildMainArea() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(C_BG);
        wrapper.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Carte blanche contenant le formulaire dynamique
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Ombre légère
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fill(new RoundRectangle2D.Float(2, 3, getWidth()-2, getHeight()-2, 16, 16));
                // Fond blanc
                g2.setColor(C_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-3, getHeight()-3, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);

        // Panel dynamique — reçoit les formulaires
        dynamicPanel = new JPanel(new BorderLayout());
        dynamicPanel.setBackground(C_WHITE);
        dynamicPanel.setOpaque(false);

        // État initial : message d'invitation
        emptyStateLabel = buildEmptyState();
        dynamicPanel.add(emptyStateLabel, BorderLayout.CENTER);

        card.add(dynamicPanel, BorderLayout.CENTER);
        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel buildEmptyState() {
        JLabel lbl = new JLabel(
                "<html><div style='text-align:center;'>"
                + "<span style='font-size:32px'>📄</span><br><br>"
                + "<b style='font-size:14px;color:#121212'>Aucun formulaire sélectionné</b><br>"
                + "<span style='font-size:12px;color:#888'>Choisissez un type de document dans la liste ci-dessus<br>"
                + "puis cliquez sur <b>OK</b> pour afficher le formulaire.</span>"
                + "</div></html>",
                SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(C_SUBTITLE);
        lbl.setBorder(new EmptyBorder(60, 40, 60, 40));
        return lbl;
    }

    // ── Chargement dynamique du formulaire ────────────────────────────────────

    private void loadSelectedForm() {
        AdministrativeActType selected = (AdministrativeActType) typeCombo.getSelectedItem();
        if (selected == null) return;

        BaseDocumentForm form = createForm(selected);

        dynamicPanel.removeAll();
        dynamicPanel.add(form.getPanelPrincipal(), BorderLayout.CENTER);
        dynamicPanel.revalidate();
        dynamicPanel.repaint();
    }

    /**
     * Fabrique le formulaire correspondant au type sélectionné.
     * Logique de routage centralisée — facile à étendre.
     */
    private BaseDocumentForm createForm(AdministrativeActType type) {
        return switch (type) {
            case ATTESTATION_INSCRIPTION -> new AttestationInscriptionForm();
            case CERTIFICAT_SCOLARITE    -> new CertificatScolariteForm();
            case RELEVE_NOTES            -> new ReleveNotesForm();
            case ATTESTATION_REUSSITE    -> new AttestationReussiteForm();
            case CERTIFICAT_ADMISSION    -> new CertificatAdmissionForm();
            case ATTESTATION_DIPLOME     -> new AttestationDiplomeForm();
        };
    }

    // ════════════════════════════════════════════════════════════════════════
    //  SEULE méthode main de tout le projet
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            MainFormDemand app = new MainFormDemand();
            app.frame.setVisible(true);
        });
    }
}
