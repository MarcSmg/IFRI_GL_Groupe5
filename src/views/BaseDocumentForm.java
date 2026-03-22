package views;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Classe abstraite parente de tous les formulaires de demande de documents.
 *
 * Elle fournit :
 *  - La palette de couleurs commune
 *  - Les composants réutilisables (RoundedBorder, RoundedButton, OutlineButton)
 *  - Les helpers de construction (buildLabel, buildTextField, buildPasswordField…)
 *  - La structure de base : header + form + scrollPane dans panelPrincipal
 *
 * Chaque sous-classe implémente :
 *  - buildFormContent(JPanel form)   → ajoute ses champs spécifiques
 *  - getFormTitle()                  → titre affiché en haut
 *  - getFormSubtitle()               → sous-titre descriptif
 */
public abstract class BaseDocumentForm {

    // ════════════════════════════════════════════════════════════════════════
    //  Palette partagée
    // ════════════════════════════════════════════════════════════════════════
    protected static final Color C_WHITE       = Color.WHITE;
    protected static final Color C_BG_FIELD    = new Color(250, 250, 250);
    protected static final Color C_BORDER      = new Color(215, 215, 215);
    protected static final Color C_BORDER_FOCUS= new Color(33,  119, 240);
    protected static final Color C_LABEL       = new Color(50,  50,  50);
    protected static final Color C_STAR        = new Color(220, 53,  69);
    protected static final Color C_PLACEHOLDER = new Color(170, 170, 170);
    protected static final Color C_TITLE       = new Color(18,  18,  18);
    protected static final Color C_SUBTITLE    = new Color(145, 145, 145);
    protected static final Color C_BTN_PRIMARY = new Color(33,  119, 240);
    protected static final Color C_BTN_HOVER   = new Color(21,  96,  200);
    protected static final Color C_BTN_OUTLINE = new Color(210, 210, 210);
    protected static final Color C_BTN_OL_HOVER= new Color(245, 245, 245);

    // ════════════════════════════════════════════════════════════════════════
    //  Champ principal
    // ════════════════════════════════════════════════════════════════════════
    protected JPanel panelPrincipal;
    protected JButton submitButton;

    // ════════════════════════════════════════════════════════════════════════
    //  Construction commune
    // ════════════════════════════════════════════════════════════════════════
    protected void init() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(C_WHITE);

        // En-tête
        JPanel header = new JPanel();
        header.setBackground(C_WHITE);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(getFormTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(C_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(getFormSubtitle());
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(C_SUBTITLE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(26));

        // Zone de champs
        JPanel form = new JPanel();
        form.setBackground(C_WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        buildFormContent(form);

        form.add(Box.createVerticalStrut(22));

        // Bouton de soumission
        submitButton = new RoundedButton("Soumettre la demande", C_BTN_PRIMARY, C_BTN_HOVER, 12);
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setForeground(C_WHITE);
        submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        submitButton.addActionListener(e -> {
            // TODO : ajouter la logique métier ici
        });
        form.add(submitButton);

        // Body avec padding (à l'intérieur du scroll)
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(C_WHITE);
        body.setBorder(new EmptyBorder(28, 32, 28, 32));
        body.add(header, BorderLayout.NORTH);
        body.add(form,   BorderLayout.CENTER);

        // ScrollPane invisible
        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setBackground(C_WHITE);
        scroll.getViewport().setBackground(C_WHITE);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panelPrincipal.add(scroll, BorderLayout.CENTER);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  À implémenter par chaque formulaire
    // ════════════════════════════════════════════════════════════════════════
    protected abstract void   buildFormContent(JPanel form);
    protected abstract String getFormTitle();
    protected abstract String getFormSubtitle();

    // ════════════════════════════════════════════════════════════════════════
    //  Accesseur public
    // ════════════════════════════════════════════════════════════════════════
    public JPanel getPanelPrincipal() { return panelPrincipal; }
    public JButton getSubmitButton()  { return submitButton; }

    // ════════════════════════════════════════════════════════════════════════
    //  Helpers de construction de champs
    // ════════════════════════════════════════════════════════════════════════

    /** Ajoute un champ texte avec label et placeholder dans le panel parent. */
    protected JTextField addTextField(JPanel parent, String labelTxt, String placeholder) {
        parent.add(buildLabelRow(labelTxt, true));
        parent.add(Box.createVerticalStrut(6));

        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(C_PLACEHOLDER);
        f.setBackground(C_BG_FIELD);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyFieldBorder(f, C_BORDER);
        f.setText(placeholder);

        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) {
                    f.setText(""); f.setForeground(new Color(20, 20, 20)); f.setBackground(C_WHITE);
                }
                applyFieldBorder(f, C_BORDER_FOCUS);
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) {
                    f.setForeground(C_PLACEHOLDER); f.setBackground(C_BG_FIELD); f.setText(placeholder);
                }
                applyFieldBorder(f, C_BORDER);
            }
        });

        parent.add(f);
        parent.add(Box.createVerticalStrut(14));
        return f;
    }

    /** Ajoute un champ texte optionnel (sans astérisque). */
    protected JTextField addOptionalTextField(JPanel parent, String labelTxt, String placeholder) {
        parent.add(buildLabelRow(labelTxt, false));
        parent.add(Box.createVerticalStrut(6));

        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(C_PLACEHOLDER);
        f.setBackground(C_BG_FIELD);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyFieldBorder(f, C_BORDER);
        f.setText(placeholder);

        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) {
                    f.setText(""); f.setForeground(new Color(20, 20, 20)); f.setBackground(C_WHITE);
                }
                applyFieldBorder(f, C_BORDER_FOCUS);
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) {
                    f.setForeground(C_PLACEHOLDER); f.setBackground(C_BG_FIELD); f.setText(placeholder);
                }
                applyFieldBorder(f, C_BORDER);
            }
        });

        parent.add(f);
        parent.add(Box.createVerticalStrut(14));
        return f;
    }

    /** Ajoute un JComboBox stylisé avec label. */
    protected JComboBox<String> addComboBox(JPanel parent, String labelTxt, String[] options) {
        parent.add(buildLabelRow(labelTxt, true));
        parent.add(Box.createVerticalStrut(6));

        JComboBox<String> combo = new JComboBox<>(options);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(C_BG_FIELD);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(C_BORDER, 10, 1),
                new EmptyBorder(0, 6, 0, 6)
        ));

        parent.add(combo);
        parent.add(Box.createVerticalStrut(14));
        return combo;
    }

    /** Ajoute un JSpinner (pour les années, numéros) avec label. */
    protected JSpinner addSpinner(JPanel parent, String labelTxt, int min, int max, int value) {
        parent.add(buildLabelRow(labelTxt, true));
        parent.add(Box.createVerticalStrut(6));

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinner.setBackground(C_BG_FIELD);
        spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        spinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinner.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(C_BORDER, 10, 1),
                new EmptyBorder(0, 8, 0, 8)
        ));

        parent.add(spinner);
        parent.add(Box.createVerticalStrut(14));
        return spinner;
    }

    /** Ajoute une zone de texte multiligne avec label. */
    protected JTextArea addTextArea(JPanel parent, String labelTxt, String placeholder, int rows) {
        parent.add(buildLabelRow(labelTxt, false));
        parent.add(Box.createVerticalStrut(6));

        JTextArea area = new JTextArea(rows, 1);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setForeground(C_PLACEHOLDER);
        area.setBackground(C_BG_FIELD);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(placeholder);
        area.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(C_BORDER, 10, 1),
                new EmptyBorder(10, 14, 10, 14)
        ));

        area.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (area.getText().equals(placeholder)) {
                    area.setText(""); area.setForeground(new Color(20, 20, 20)); area.setBackground(C_WHITE);
                }
                area.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(C_BORDER_FOCUS, 10, 1), new EmptyBorder(10, 14, 10, 14)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setForeground(C_PLACEHOLDER); area.setBackground(C_BG_FIELD); area.setText(placeholder);
                }
                area.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(C_BORDER, 10, 1), new EmptyBorder(10, 14, 10, 14)));
            }
        });

        JScrollPane areaScroll = new JScrollPane(area);
        areaScroll.setBorder(null);
        areaScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, rows * 22 + 24));

        parent.add(areaScroll);
        parent.add(Box.createVerticalStrut(14));
        return area;
    }

    /** Ajoute un séparateur avec texte de section. */
    protected void addSectionSeparator(JPanel parent, String sectionTitle) {
        parent.add(Box.createVerticalStrut(8));
        JLabel secLbl = new JLabel(sectionTitle.toUpperCase());
        secLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        secLbl.setForeground(new Color(160, 160, 170));
        secLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(secLbl);
        parent.add(Box.createVerticalStrut(4));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(235, 235, 238));
        parent.add(sep);
        parent.add(Box.createVerticalStrut(14));
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Utilitaires privés
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildLabelRow(String labelTxt, boolean required) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(C_WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (required) {
            JLabel star = new JLabel("* ");
            star.setFont(new Font("Segoe UI", Font.BOLD, 13));
            star.setForeground(C_STAR);
            row.add(star);
        }
        JLabel lbl = new JLabel(labelTxt);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(C_LABEL);
        row.add(lbl);
        return row;
    }

    protected void applyFieldBorder(JTextField f, Color c) {
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(c, 10, 1),
                new EmptyBorder(0, 2, 0, 2)
        ));
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Composants graphiques réutilisables (statiques)
    // ════════════════════════════════════════════════════════════════════════

    /** Bordure arrondie avec antialiasing. */
    public static class RoundedBorder extends AbstractBorder {
        private final Color color; private final int r, t;
        public RoundedBorder(Color c, int r, int t) { color=c; this.r=r; this.t=t; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.setStroke(new BasicStroke(t));
            g2.draw(new RoundRectangle2D.Float(x+.5f,y+.5f,w-1,h-1,r,r));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(t+7,t+14,t+7,t+14); }
    }

    /** Bouton plein arrondi avec hover. */
    public static class RoundedButton extends JButton {
        private final Color bg, hover; private final int r;
        public RoundedButton(String txt, Color bg, Color hover, int r) {
            super(txt); this.bg=bg; this.hover=hover; this.r=r;
            setOpaque(false); setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getMousePosition() != null ? hover : bg);
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),r,r));
            g2.dispose(); super.paintComponent(g);
        }
    }

    /** Bouton contour arrondi avec hover. */
    public static class OutlineButton extends JButton {
        private final Color border, hoverBg; private final int r;
        public OutlineButton(String txt, Color border, Color hoverBg, int r) {
            super(txt); this.border=border; this.hoverBg=hoverBg; this.r=r;
            setOpaque(false); setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getMousePosition() != null ? hoverBg : Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),r,r));
            g2.setColor(border); g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(1,1,getWidth()-2,getHeight()-2,r,r));
            g2.dispose(); super.paintComponent(g);
        }
    }
}
