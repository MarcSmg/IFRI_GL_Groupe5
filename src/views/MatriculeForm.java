package views;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MatriculeForm {

    private JPanel panelPrincipal;
    private JTextField matriculeField;
    private JButton nextButton;
    private JLabel connexionLink;

    public MatriculeForm() {
        initComponents();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Composants réutilisables (partagés entre les 3 formulaires)
    // ════════════════════════════════════════════════════════════════════════

    /** Bordure arrondie dessinée manuellement (antialiasing). */
    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        private final int thickness;

        public RoundedBorder(Color color, int radius, int thickness) {
            this.color = color;
            this.radius = radius;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Float(x + .5f, y + .5f, w - 1, h - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 7, thickness + 14, thickness + 7, thickness + 14);
        }
    }

    /** Bouton plein avec coins arrondis et effet hover. */
    public static class RoundedButton extends JButton {
        private final Color bg;
        private final Color hover;
        private final int r;

        public RoundedButton(String text, Color bg, Color hover, int r) {
            super(text);
            this.bg = bg; this.hover = hover; this.r = r;
            setOpaque(false); setContentAreaFilled(false);
            setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getMousePosition() != null ? hover : bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), r, r));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Bouton contour avec coins arrondis et effet hover. */
    public static class OutlineButton extends JButton {
        private final Color border;
        private final Color hoverBg;
        private final int r;

        public OutlineButton(String text, Color border, Color hoverBg, int r) {
            super(text);
            this.border = border; this.hoverBg = hoverBg; this.r = r;
            setOpaque(false); setContentAreaFilled(false);
            setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getMousePosition() != null ? hoverBg : Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), r, r));
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, r, r));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Construction du formulaire
    // ════════════════════════════════════════════════════════════════════════

    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setPreferredSize(new Dimension(620, 380));
        panelPrincipal.setBorder(new EmptyBorder(52, 72, 44, 72));

        // ── Barre supérieure : lien connexion à droite ───────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(0, 0, 20, 0));

        connexionLink = new JLabel("Déjà un compte ? Se connecter →");
        connexionLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        connexionLink.setForeground(new Color(33, 119, 240));
        connexionLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        connexionLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                connexionLink.setText("<html><u>Déjà un compte ? Se connecter →</u></html>");
            }
            @Override public void mouseExited(MouseEvent e) {
                connexionLink.setText("Déjà un compte ? Se connecter →");
            }
        });

        topBar.add(connexionLink, BorderLayout.EAST);

        // ── Titre ────────────────────────────────────────────────────────
        JLabel title = new JLabel("Identification");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(18, 18, 18));

        JLabel subtitle = new JLabel("Entrez votre numéro matricule pour accéder à votre espace.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(145, 145, 145));

        // ── Champ matricule ──────────────────────────────────────────────
        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelRow.setBackground(Color.WHITE);
        JLabel star = new JLabel("* ");
        star.setFont(new Font("Segoe UI", Font.BOLD, 13));
        star.setForeground(new Color(220, 53, 69));
        JLabel fieldLabel = new JLabel("Numéro matricule");
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fieldLabel.setForeground(new Color(50, 50, 50));
        labelRow.add(star);
        labelRow.add(fieldLabel);

        matriculeField = new JTextField();
        matriculeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        matriculeField.setForeground(new Color(170, 170, 170));
        matriculeField.setBackground(new Color(250, 250, 250));
        matriculeField.setPreferredSize(new Dimension(476, 50));
        matriculeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        applyFieldBorder(matriculeField, new Color(215, 215, 215));

        final String ph = "Entrez votre numéro matricule";
        matriculeField.setText(ph);
        matriculeField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (matriculeField.getText().equals(ph)) {
                    matriculeField.setText("");
                    matriculeField.setForeground(new Color(20, 20, 20));
                    matriculeField.setBackground(Color.WHITE);
                }
                applyFieldBorder(matriculeField, new Color(33, 119, 240));
            }
            @Override public void focusLost(FocusEvent e) {
                if (matriculeField.getText().isEmpty()) {
                    matriculeField.setForeground(new Color(170, 170, 170));
                    matriculeField.setBackground(new Color(250, 250, 250));
                    matriculeField.setText(ph);
                }
                applyFieldBorder(matriculeField, new Color(215, 215, 215));
            }
        });

        // ── Bouton NEXT (coin bas-droite) ────────────────────────────────
        nextButton = new RoundedButton("NEXT  →",
                new Color(33, 119, 240), new Color(21, 96, 200), 12);
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nextButton.setForeground(Color.WHITE);
        nextButton.setPreferredSize(new Dimension(150, 48));
        nextButton.addActionListener(e -> {
            // TODO : ajouter ici la logique de traitement du matricule
        });

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.add(nextButton);

        // ── Assemblage centre ────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(topBar);
        body.add(title);
        body.add(Box.createVerticalStrut(6));
        body.add(subtitle);
        body.add(Box.createVerticalStrut(32));
        body.add(labelRow);
        body.add(Box.createVerticalStrut(7));
        body.add(matriculeField);

        panelPrincipal.add(body, BorderLayout.CENTER);
        panelPrincipal.add(bottomBar, BorderLayout.SOUTH);
    }

    private void applyFieldBorder(JTextField f, Color borderColor) {
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(borderColor, 10, 1),
                new EmptyBorder(0, 2, 0, 2)
        ));
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public JPanel getPanelPrincipal() { return panelPrincipal; }

    public String getMatricule() {
        String v = matriculeField.getText().trim();
        return v.equals("Entrez votre numéro matricule") ? "" : v;
    }

    public JButton getNextButton() { return nextButton; }

    public void addNextListener(ActionListener l) { nextButton.addActionListener(l); }

    public void addConnexionLinkListener(MouseAdapter a) { connexionLink.addMouseListener(a); }

    // ── Main de test ─────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            JFrame frame = new JFrame("Test – Numéro Matricule");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(new MatriculeForm().getPanelPrincipal());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
