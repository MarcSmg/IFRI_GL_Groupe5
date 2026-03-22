/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

/**
 *
 * @author Héloïse
 */
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import models.*;
import models.enums.DemandStatus;
import models.enums.AdministrativeActType;
import controllers.*;


public class DashboardUsager {

    static class RoundedBorder extends AbstractBorder {
        private final Color color; private final int r, t;
        RoundedBorder(Color c, int r, int t) { color=c; this.r=r; this.t=t; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.setStroke(new BasicStroke(t));
            g2.draw(new RoundRectangle2D.Float(x+.5f,y+.5f,w-1,h-1,r,r)); g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c){return new Insets(t+7,t+14,t+7,t+14);}
    }

    static class RoundedButton extends JButton {
        private final Color bg, hover; private final int r;
        RoundedButton(String txt,Color bg,Color hover,int r){
            super(txt); this.bg=bg; this.hover=hover; this.r=r;
            setOpaque(false); setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getMousePosition()!=null?hover:bg);
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),r,r)); g2.dispose();
            super.paintComponent(g);
        }
    }

    
    private JPanel panelPrincipal;
    private JPanel cardsContainer;
    private JScrollPane scrollPane;

    private final User currentUser;
    private final UsagerController controller;

    // Couleurs palette
    private static final Color BLEU        = new Color(33, 119, 240);
    private static final Color BLEU_HOVER  = new Color(21,  96, 200);
    private static final Color BG_PAGE     = new Color(245, 247, 250);
    private static final Color CARD_BG     = Color.WHITE;

    // Couleurs statuts
    private static final Color C_EN_COURS  = new Color(33,  119, 240);
    private static final Color C_VALIDE    = new Color(34,  197, 94);
    private static final Color C_ATTENTE   = new Color(249, 115, 22);
    private static final Color C_REJETEE   = new Color(239, 68,  68);



    public DashboardUsager(User user, UsagerController controller) {
        this.currentUser = user;
        this.controller = controller;
        initComponents();
        chargerDemandes();
    }



    private void initComponents() {

        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(BG_PAGE);
        panelPrincipal.setPreferredSize(new Dimension(900, 650));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(16, 32, 16, 32)
        ));

        JLabel appTitle = new JLabel(currentUser.getFirstName()+ ", vos documents en quelques clics! ");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appTitle.setForeground(new Color(30, 30, 30));

        // Boutons d'action
        JPanel actionButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionButtons.setBackground(Color.WHITE);

        RoundedButton btnVosDemandes = new RoundedButton(
                "  📋  Vos demandes  ", BLEU, BLEU_HOVER, 10);
        btnVosDemandes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVosDemandes.setForeground(Color.WHITE);
        btnVosDemandes.setPreferredSize(new Dimension(160, 40));
        btnVosDemandes.addActionListener(e -> chargerDemandes());

        RoundedButton btnNouvelleDemande = new RoundedButton(
                "  ＋  Nouvelle demande  ", new Color(22, 163, 74), new Color(15, 120, 54), 10);
        btnNouvelleDemande.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNouvelleDemande.setForeground(Color.WHITE);
        btnNouvelleDemande.setPreferredSize(new Dimension(190, 40));
        btnNouvelleDemande.addActionListener(e -> {
            // TODO : ouvrir le formulaire de nouvelle demande
            MainFormDemand demandForm = new MainFormDemand();
            JPanel panelForm = demandForm.getPanelPrincipal();
            
            JOptionPane.showConfirmDialog(
        null,                             // Parent (null pour centrer sur l'écran)
        panelForm,                        // Le composant à afficher
        "Nouvelle Demande de Diplôme",    // Titre de la modal
        JOptionPane.OK_CANCEL_OPTION,     // Boutons (OK et Annuler)
        JOptionPane.PLAIN_MESSAGE         // Type d'icône (aucune ici car c'est un formulaire)
    );
        });
        
        
        


        actionButtons.add(btnVosDemandes);
        actionButtons.add(btnNouvelleDemande);

        topBar.add(actionButtons, BorderLayout.WEST);
        topBar.add(appTitle,      BorderLayout.EAST);

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(BG_PAGE);
        cardsContainer.setBorder(new EmptyBorder(28, 40, 28, 40));

        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BG_PAGE);
        scrollPane.getViewport().setBackground(BG_PAGE);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panelPrincipal.add(topBar,    BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
    }

  

    public void chargerDemandes() {
        cardsContainer.removeAll();

        List<Demand> demandes = controller.chargerDemandesUtilisateur(currentUser.getId());

        if (demandes == null || demandes.isEmpty()) {
            cardsContainer.add(Box.createVerticalGlue());
            cardsContainer.add(buildEmptyState());
            cardsContainer.add(Box.createVerticalGlue());
        } else {
            // Titre section
            JLabel sectionTitle = new JLabel("Mes demandes  (" + demandes.size() + ")");
            sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
            sectionTitle.setForeground(new Color(40, 40, 40));
            sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            cardsContainer.add(sectionTitle);
            cardsContainer.add(Box.createVerticalStrut(18));

            for (Demand d : demandes) {
                cardsContainer.add(buildDemandCard(d));
                cardsContainer.add(Box.createVerticalStrut(12));
            }
            cardsContainer.add(Box.createVerticalGlue());
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }


    private JPanel buildDemandCard(Demand d) {

        Color accentColor = getAccentColor(d.getStatus());
        String statutLabel = getStatutLabel(d.getStatus());

        JPanel shadow = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Ombre douce
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fill(new RoundRectangle2D.Float(3, 4, getWidth()-3, getHeight()-2, 14, 14));
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-3, getHeight()-3, 14, 14));
                g2.dispose();
            }
        };
        shadow.setOpaque(false);
        shadow.setBorder(new EmptyBorder(0, 0, 4, 4));
        shadow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        shadow.setPreferredSize(new Dimension(780, 82));
        shadow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel interne
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(0, 0, 0, 16));

        // ── Barre accent gauche ──────────────────────────────────────────
        JPanel accent = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                // Arrondi uniquement à gauche
                g2.fillRoundRect(0, 0, getWidth()+10, getHeight(), 14, 14);
                g2.dispose();
            }
        };
        accent.setOpaque(false);
        accent.setPreferredSize(new Dimension(6, 0));

        // ── Zone infos centre ────────────────────────────────────────────
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel numLabel = new JLabel("Demande #" + d.getDemandNumber());
        numLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        numLabel.setForeground(new Color(20, 20, 20));
        numLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel typeLabel = new JLabel(d.getActType());
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typeLabel.setForeground(new Color(120, 120, 120));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(numLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(typeLabel);

        // ── Badge statut droite ──────────────────────────────────────────
        JPanel badgeWrapper = new JPanel(new GridBagLayout());
        badgeWrapper.setOpaque(false);
        badgeWrapper.setBorder(new EmptyBorder(0, 0, 0, 4));

        JLabel badge = new JLabel(statutLabel) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond coloré semi-transparent
                Color bg = new Color(accentColor.getRed(), accentColor.getGreen(),
                        accentColor.getBlue(), 22);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                // Bordure
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(),
                        accentColor.getBlue(), 80));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(.5f, .5f, getWidth()-1, getHeight()-1, getHeight(), getHeight()));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(accentColor);
        badge.setBorder(new EmptyBorder(5, 14, 5, 14));
        badge.setOpaque(false);

        badgeWrapper.add(badge);

        card.add(accent,       BorderLayout.WEST);
        card.add(info,         BorderLayout.CENTER);
        card.add(badgeWrapper, BorderLayout.EAST);

        shadow.add(card, BorderLayout.CENTER);

        // Hover léger sur la carte
        MouseAdapter hoverCard = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                shadow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                shadow.repaint();
            }
            @Override public void mouseExited(MouseEvent e) { shadow.repaint(); }
        };
        card.addMouseListener(hoverCard);

        return shadow;
    }

    // État vide 

    private JPanel buildEmptyState() {
        JPanel empty = new JPanel();
        empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
        empty.setBackground(BG_PAGE);
        empty.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel drawerIcon = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2, cy = getHeight() / 2;

                // Corps du tiroir (rectangle arrondi)
                g2.setColor(new Color(215, 225, 240));
                g2.fill(new RoundRectangle2D.Float(cx - 52, cy - 38, 104, 76, 14, 14));

                // Façade tiroir
                g2.setColor(new Color(190, 205, 230));
                g2.fill(new RoundRectangle2D.Float(cx - 46, cy + 2, 92, 30, 8, 8));

                // Poignée tiroir
                g2.setColor(new Color(140, 160, 200));
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(new RoundRectangle2D.Float(cx - 14, cy + 12, 28, 10, 8, 8));

                // Lignes "vide"
                g2.setColor(new Color(180, 195, 220));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx - 28, cy - 20, cx + 28, cy - 20);
                g2.drawLine(cx - 20, cy - 8,  cx + 20, cy - 8);

                g2.dispose();
            }

            @Override public Dimension getPreferredSize() { return new Dimension(120, 110); }
        };
        drawerIcon.setBackground(BG_PAGE);
        drawerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg = new JLabel("Vous n'avez aucune demande pour l'instant");
        msg.setFont(new Font("Segoe UI", Font.BOLD, 16));
        msg.setForeground(new Color(100, 110, 130));
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Cliquez sur « Nouvelle demande » pour en créer une.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hint.setForeground(new Color(160, 165, 175));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        empty.add(Box.createVerticalStrut(60));
        empty.add(drawerIcon);
        empty.add(Box.createVerticalStrut(20));
        empty.add(msg);
        empty.add(Box.createVerticalStrut(8));
        empty.add(hint);
        empty.add(Box.createVerticalStrut(60));

        return empty;
    }


    private Color getAccentColor(DemandStatus s) {
        return switch (s) {
            case PENDING  -> C_EN_COURS;
            case VALIDATED   -> C_VALIDE;
            case SAVED-> C_ATTENTE;
            case REJECTED   -> C_REJETEE;
        };
    }

    private String getStatutLabel(DemandStatus s) {
        return switch (s) {
            case PENDING  -> "En cours";
            case VALIDATED   -> "Validé";
            case SAVED-> "En attente";
            case REJECTED   -> "Rejetée";
        };
    }

   // getters

    public JPanel getPanelPrincipal() { return panelPrincipal; }


    
    // ─────────────────────────────────────────────────────────────────────────
    //  MÉTHODE MAIN POUR TESTER LE RENDU VISUEL
    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        // 1. On applique un Look & Feel moderne (optionnel mais recommandé)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        // 2. Création d'un utilisateur fictif pour le test
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setPrenom("Héloïse");
        mockUser.setNom("Akohou");
        mockUser.setEmail("heloise@example.com");
        // Ajuste selon ton Enum Role (ex: Role.ETUDIANT ou Role.USER)
        // mockUser.setRole(Role.USER); 

        // 3. Création d'un contrôleur anonyme pour simuler les données
        UsagerController mockController = new UsagerController() {
            @Override
            public java.util.List<Demand> chargerDemandesUtilisateur(int userId) {
                // On crée une liste de demandes factices pour tester le design des cartes
                java.util.List<Demand> testDemands = new java.util.ArrayList<>();
                
                Demand d1 = new Demand();
                d1.setDemandNumber("2024-001");
                d1.setTypeAct(AdministrativeActType.CERTIFICAT_SCOLARITE);
                d1.setStatus(DemandStatus.PENDING);
                
                
                
                

                testDemands.add(d1);
             
                
                return testDemands;
            }
        };

        // 4. Lancement dans l'EDT (Event Dispatch Thread) de Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Dashboard Usager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            DashboardUsager dashboard = new DashboardUsager(mockUser, mockController);
            
            frame.add(dashboard.getPanelPrincipal());
            frame.pack();
            frame.setLocationRelativeTo(null); // Centrer l'écran
            frame.setVisible(true);
        });
    }
}
