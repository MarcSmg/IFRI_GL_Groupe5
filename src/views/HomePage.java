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
import javax.swing.border.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class HomePage extends JFrame {

    // ─── Palette ────────────────────────────────────────────────
    private static final Color BG_PAGE       = new Color(0xEFF4FB);
    private static final Color BLUE_MAIN     = new Color(0x1D4ED8);
    private static final Color BLUE_HOVER    = new Color(0x1E3A8A);
    private static final Color CARD_BG       = Color.WHITE;
    private static final Color TEXT_DARK     = new Color(0x0F172A);
    private static final Color TEXT_MED      = new Color(0x334155);
    private static final Color BORDER_COLOR  = new Color(0xCBD5E1);
    private static final Color HEADER_BG     = Color.WHITE;
    private static final Color STRIP1 = new Color(0x1A5276);
    private static final Color STRIP2 = new Color(0xE74C3C);
    private static final Color STRIP3 = new Color(0x27AE60);
    private static final Color STRIP4 = new Color(0xF39C12);
    private static final Color HERO_TOP  = new Color(0x1E3A8A);
    private static final Color HERO_BOT  = new Color(0x2563EB);
    private static final Color SECONDARY_BG     = new Color(0xEFF6FF);
    private static final Color SECONDARY_BORDER  = new Color(0x93C5FD);
    private static final Color SECONDARY_TEXT    = new Color(0x1D4ED8);

    
    private static final String LOGO_IFRI_PATH = "/ressources/logoifri.png";   
    private static final String LOGO_UAC_PATH  = "/ressources/logouac.png";    

    // ─── Chargement d'image avec redimensionnement ──────────────
    /**
     * Charge une image depuis un chemin local et la redimensionne.
     * Retourne null si le fichier est introuvable (le logo dessiné sera utilisé en fallback).
     */
    private static ImageIcon loadLogo(String path, int width, int height) {
    try {
        // Correction : Utiliser getResource au lieu de File
        java.net.URL imgURL = HomePage.class.getResource(path);
        
        if (imgURL == null) {
            System.err.println("[IFRI] Ressource introuvable : " + path);
            return null;
        }

        BufferedImage img = ImageIO.read(imgURL);
        if (img == null) return null;
        
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    } catch (Exception e) {
        System.err.println("[IFRI] Erreur de chargement : " + path);
        return null;
    }
}
    public HomePage() {
        setTitle("IFRI – Plateforme numérique de documents académiques");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(860, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_PAGE);
        setLayout(new BorderLayout());

        add(buildHeader(),         BorderLayout.NORTH);
        add(buildScrollableBody(), BorderLayout.CENTER);
    }

    // ════════════════════════════════════════════════════════════
    //  HEADER
    // ════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(HEADER_BG);
        wrapper.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JPanel strip = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                int w4 = getWidth() / 4;
                g.setColor(STRIP1); g.fillRect(0,    0, w4,              getHeight());
                g.setColor(STRIP2); g.fillRect(w4,   0, w4,              getHeight());
                g.setColor(STRIP3); g.fillRect(w4*2, 0, w4,              getHeight());
                g.setColor(STRIP4); g.fillRect(w4*3, 0, getWidth()-w4*3, getHeight());
            }
        };
        strip.setPreferredSize(new Dimension(0, 6));

        JPanel logoRow = new JPanel(new BorderLayout());
        logoRow.setBackground(HEADER_BG);
        logoRow.setBorder(new EmptyBorder(10, 24, 10, 24));
        logoRow.add(buildLeftLogo(),  BorderLayout.WEST);
        logoRow.add(buildRightLogo(), BorderLayout.EAST);

        wrapper.add(strip,   BorderLayout.NORTH);
        wrapper.add(logoRow, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildLeftLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        p.setOpaque(false);

        // ── Logo IFRI : image locale, sinon logo dessiné en fallback ──
        final ImageIcon ifriIcon = loadLogo(LOGO_IFRI_PATH, 64, 64);

        JPanel logo = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (ifriIcon != null) {
                    // ── IMAGE CHARGÉE : on l'affiche centrée ──
                    int iw = ifriIcon.getIconWidth();
                    int ih = ifriIcon.getIconHeight();
                    g2.drawImage(ifriIcon.getImage(),
                            (getWidth()-iw)/2, (getHeight()-ih)/2, iw, ih, null);
                } else {
                    // ── FALLBACK : logo dessiné ──
                    int d = Math.min(getWidth(), getHeight()) - 4;
                    int x = (getWidth()-d)/2, y = (getHeight()-d)/2;
                    GradientPaint gp = new GradientPaint(x, y, STRIP1, x+d, y+d, STRIP3);
                    g2.setPaint(gp);
                    g2.fillOval(x, y, d, d);
                    int inner = (int)(d * 0.70);
                    g2.setColor(Color.WHITE);
                    g2.fillOval((getWidth()-inner)/2, (getHeight()-inner)/2, inner, inner);
                    g2.setColor(STRIP1);
                    g2.setFont(new Font("Arial", Font.BOLD, 15));
                    FontMetrics fm = g2.getFontMetrics();
                    String t = "IFRI";
                    g2.drawString(t, (getWidth()-fm.stringWidth(t))/2,
                            (getHeight()+fm.getAscent()-fm.getDescent())/2);
                }
            }
            @Override public Dimension getPreferredSize() { return new Dimension(64, 64); }
        };
        logo.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);

        JLabel l1 = new JLabel("INSTITUT DE FORMATION ET DE RECHERCHE EN INFORMATIQUE");
        l1.setFont(new Font("Arial", Font.BOLD, 13));
        l1.setForeground(TEXT_DARK);

        JLabel l2 = new JLabel("Nous bâtissons l'excellence");
        l2.setFont(new Font("Arial", Font.ITALIC, 11));
        l2.setForeground(TEXT_MED);

        titles.add(l1);
        titles.add(Box.createVerticalStrut(3));
        titles.add(l2);

        p.add(logo);
        p.add(titles);
        return p;
    }

    private JPanel buildRightLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        p.setOpaque(false);

        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setOpaque(false);

        JLabel univ = new JLabel("UNIVERSITE D'ABOMEY-CALAVI");
        univ.setFont(new Font("Arial", Font.BOLD, 11));
        univ.setForeground(TEXT_DARK);
        univ.setAlignmentX(LEFT_ALIGNMENT);

        JPanel miniStrip = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                int w4 = getWidth()/4;
                g.setColor(STRIP1); g.fillRect(0,    0, w4,              getHeight());
                g.setColor(STRIP2); g.fillRect(w4,   0, w4,              getHeight());
                g.setColor(STRIP3); g.fillRect(w4*2, 0, w4,              getHeight());
                g.setColor(STRIP4); g.fillRect(w4*3, 0, getWidth()-w4*3, getHeight());
            }
        };
        miniStrip.setPreferredSize(new Dimension(170, 4));
        miniStrip.setMaximumSize(new Dimension(170, 4));
        miniStrip.setAlignmentX(LEFT_ALIGNMENT);

        JLabel mini = new JLabel("<html>MNISTERE DE &nbsp;L'ENSEIGNEMENT<br>"
                + "SUPERIEUR ET DE &nbsp;LA &nbsp;RECHERCHE<br>SCIENTIFIQUE</html>");
        mini.setFont(new Font("Arial", Font.PLAIN, 9));
        mini.setForeground(TEXT_MED);
        mini.setAlignmentX(LEFT_ALIGNMENT);

        block.add(univ);
        block.add(Box.createVerticalStrut(3));
        block.add(miniStrip);
        block.add(Box.createVerticalStrut(4));
        block.add(mini);

        // ── Logo UAC : image locale, sinon logo dessiné en fallback ──
        final ImageIcon uacIcon = loadLogo(LOGO_UAC_PATH, 64, 64);

        JPanel uac = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (uacIcon != null) {
                    // ── IMAGE CHARGÉE : on l'affiche centrée ──
                    int iw = uacIcon.getIconWidth();
                    int ih = uacIcon.getIconHeight();
                    g2.drawImage(uacIcon.getImage(),
                            (getWidth()-iw)/2, (getHeight()-ih)/2, iw, ih, null);
                } else {
                    // ── FALLBACK : logo dessiné ──
                    int d = Math.min(getWidth(), getHeight()) - 4;
                    int x = (getWidth()-d)/2, y = (getHeight()-d)/2;
                    g2.setColor(new Color(0xD4A017));
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.drawOval(x, y, d, d);
                    g2.setColor(new Color(0xFFFBEA));
                    g2.fillOval(x+2, y+2, d-4, d-4);
                    int cx = getWidth()/2, cy = getHeight()/2;
                    g2.setColor(new Color(0xE74C3C));
                    int[] fx = {cx-5, cx, cx+5, cx+3, cx-3};
                    int[] fy = {cy-10, cy-22, cy-10, cy-5, cy-5};
                    g2.fillPolygon(fx, fy, 5);
                    g2.setColor(new Color(0xF39C12));
                    int[] fx2 = {cx-3, cx, cx+3};
                    int[] fy2 = {cy-10, cy-18, cy-10};
                    g2.fillPolygon(fx2, fy2, 3);
                    g2.setColor(new Color(0x7D6608));
                    g2.fillRect(cx-3, cy-5, 6, 16);
                    g2.fillRect(cx-5, cy+10, 10, 3);
                    g2.setColor(STRIP1);
                    g2.setFont(new Font("Arial", Font.BOLD, 8));
                    FontMetrics fm = g2.getFontMetrics();
                    String t = "UAC";
                    g2.drawString(t, cx-fm.stringWidth(t)/2, cy+26);
                }
            }
            @Override public Dimension getPreferredSize() { return new Dimension(64, 64); }
        };
        uac.setOpaque(false);

        p.add(block);
        p.add(uac);
        return p;
    }

    // ════════════════════════════════════════════════════════════
    //  SCROLLABLE BODY
    // ════════════════════════════════════════════════════════════
    private JScrollPane buildScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_PAGE);
        body.setBorder(new EmptyBorder(36, 60, 40, 60));

        body.add(buildHeroCard());
        body.add(Box.createVerticalStrut(28));
        body.add(buildActionRow());
        body.add(Box.createVerticalStrut(20));

        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setBackground(BG_PAGE);
        return sp;
    }

    // ════════════════════════════════════════════════════════════
    //  HERO CARD — dark blue background, white text
    // ════════════════════════════════════════════════════════════
    private JPanel buildHeroCard() {
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Strong dark-blue gradient — high contrast background
                GradientPaint gp = new GradientPaint(
                        0, 0,             HERO_TOP,
                        getWidth(), getHeight(), HERO_BOT);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

                // Soft white decorative circles
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f);
                g2.setComposite(ac);
                g2.setColor(Color.WHITE);
                g2.fillOval(-70, -70, 280, 280);
                g2.fillOval(getWidth()-180, getHeight()-120, 320, 320);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(60, 60, 60, 60));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);

        // Title — WHITE on dark blue
        JLabel title = new JLabel(
                "<html><div style='text-align:center;'>"
                + "Vos documents<br>académiques en quelques<br>clics"
                + "</div></html>");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtitle — soft light-blue, clearly readable
        JLabel subtitle = new JLabel(
                "<html><div style='text-align:center;'>"
                + "Demandez, suivez et recevez vos documents officiels en toute sécurité<br>"
                + "sur la plateforme numérique de l'IFRI."
                + "</div></html>");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(0xBFDBFE));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JButton loginBtn = buildWhiteButton("Se connecter");
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(title);
        inner.add(Box.createVerticalStrut(16));
        inner.add(subtitle);
        inner.add(Box.createVerticalStrut(32));
        inner.add(loginBtn);

        card.add(inner);
        return card;
    }

    // ════════════════════════════════════════════════════════════
    //  ACTION ROW
    // ════════════════════════════════════════════════════════════
    private JPanel buildActionRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        row.setAlignmentX(LEFT_ALIGNMENT);

        row.add(buildActionCard("📄", "Besoin d'une attestation ?",       "Faire une demande",                true));
        row.add(buildActionCard("📁", "Gérer les dossiers",               "Accéder à votre espace de travail", false));

        return row;
    }

    private JPanel buildActionCard(String icon, String label, String btnText, boolean primary) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        card.setOpaque(false);
        card.setBorder(new CompoundBorder(
                new RoundedBorder(BORDER_COLOR, 1, 16),
                new EmptyBorder(20, 22, 20, 22)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);

        JLabel icLbl = new JLabel(icon + "  ");
        icLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setForeground(TEXT_DARK);

        left.add(icLbl);
        left.add(lbl);

        JButton btn = primary ? buildBlueButton(btnText) : buildOutlineButton(btnText);

        card.add(left, BorderLayout.WEST);
        card.add(btn,  BorderLayout.EAST);
        return card;
    }

    // ════════════════════════════════════════════════════════════
    //  BUTTONS
    // ════════════════════════════════════════════════════════════

    /** White pill button — for use on dark hero card */
    private JButton buildWhiteButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xDBEAFE) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        btn.setForeground(BLUE_MAIN);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 40, 12, 40));
        return btn;
    }

    /** Solid blue button — for primary action cards */
    private JButton buildBlueButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BLUE_HOVER : BLUE_MAIN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(11, 22, 11, 22));
        return btn;
    }

    /** Outlined button — for secondary action cards */
    private JButton buildOutlineButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? SECONDARY_BG : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(SECONDARY_BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setForeground(SECONDARY_TEXT);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(11, 18, 11, 18));
        return btn;
    }

    // ════════════════════════════════════════════════════════════
    //  ROUNDED BORDER HELPER
    // ════════════════════════════════════════════════════════════
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int   thickness, radius;
        RoundedBorder(Color c, int t, int r) { color = c; thickness = t; radius = r; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }

    // ════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new HomePage().setVisible(true));
    }
}
