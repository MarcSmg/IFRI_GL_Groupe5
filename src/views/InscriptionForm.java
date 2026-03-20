package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Formulaire d'inscription – disposition bureau 2 colonnes.
 *   Colonne gauche  : informations étudiant (lecture seule, préremplies)
 *   Colonne droite  : email + mots de passe (éditables)
 */
public class InscriptionForm {

    private JPanel panelPrincipal;

    // Champs lecture seule
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField filiereField;
    private JTextField niveauField;

    // Champs éditables
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton inscriptionButton;

    private final String nom;
    private final String prenom;
    private final String filiere;
    private final String niveau;

    public InscriptionForm(String nom, String prenom, String filiere, String niveau) {
        this.nom = nom;
        this.prenom = prenom;
        this.filiere = filiere;
        this.niveau = niveau;
        initComponents();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Construction
    // ════════════════════════════════════════════════════════════════════════

    private void initComponents() {

        // Panel principal : blanc, taille bureau large
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setPreferredSize(new Dimension(980, 640));
        panelPrincipal.setBorder(new EmptyBorder(48, 64, 48, 64));

        // ── En-tête global ───────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Créer un compte");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(18, 18, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Complétez vos informations de connexion pour finaliser votre inscription.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(145, 145, 145));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(36));

        // ── Corps : 2 colonnes ───────────────────────────────────────────
        JPanel columns = new JPanel(new GridLayout(1, 2, 48, 0));
        columns.setBackground(Color.WHITE);

        // ── Colonne gauche : infos étudiant ──────────────────────────────
        JPanel left = new JPanel();
        left.setBackground(Color.WHITE);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel leftTitle = new JLabel("Informations étudiant");
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        leftTitle.setForeground(new Color(50, 50, 50));
        leftTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep1.setForeground(new Color(230, 230, 230));

        left.add(leftTitle);
        left.add(Box.createVerticalStrut(12));
        left.add(sep1);
        left.add(Box.createVerticalStrut(20));

        nomField    = addReadOnlyField(left, "Nom",             nom);
        prenomField = addReadOnlyField(left, "Prénom",          prenom);
        filiereField= addReadOnlyField(left, "Filière",         filiere);
        niveauField = addReadOnlyField(left, "Niveau d'études", niveau);

        left.add(Box.createVerticalGlue());

        // ── Colonne droite : identifiants de connexion ───────────────────
        JPanel right = new JPanel();
        right.setBackground(Color.WHITE);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel rightTitle = new JLabel("Identifiants de connexion");
        rightTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rightTitle.setForeground(new Color(50, 50, 50));
        rightTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep2.setForeground(new Color(230, 230, 230));

        // Bandeau d'avertissement
        JPanel notice = new JPanel(new BorderLayout(10, 0));
        notice.setBackground(new Color(255, 248, 230));
        notice.setBorder(BorderFactory.createCompoundBorder(
                new MatriculeForm.RoundedBorder(new Color(255, 200, 80), 10, 1),
                new EmptyBorder(10, 14, 10, 14)
        ));
        notice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel noticeIcon = new JLabel("ℹ");
        noticeIcon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        noticeIcon.setForeground(new Color(200, 130, 0));

        JLabel noticeText = new JLabel(
                "<html><b style='color:#7a5000'>Vous ne pouvez pas modifier vos informations d'étudiant.</b>"
                + "<br><span style='color:#7a5000'>S'il y a une quelconque erreur, rapprochez-vous de l'administration.</span></html>"
        );
        noticeText.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        notice.add(noticeIcon, BorderLayout.WEST);
        notice.add(noticeText, BorderLayout.CENTER);

        right.add(rightTitle);
        right.add(Box.createVerticalStrut(12));
        right.add(sep2);
        right.add(Box.createVerticalStrut(16));
        right.add(notice);
        right.add(Box.createVerticalStrut(20));

        emailField          = addEditableTextField(right, "Email",                "Entrez votre email");
        passwordField       = addPasswordField    (right, "Mot de passe",         "Entrez votre mot de passe");
        confirmPasswordField= addPasswordField    (right, "Confirmer mot de passe","Confirmez votre mot de passe");

        right.add(Box.createVerticalStrut(16));

        // Bouton S'inscrire
        inscriptionButton = new MatriculeForm.RoundedButton(
                "S'inscrire", new Color(33, 119, 240), new Color(21, 96, 200), 12);
        inscriptionButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inscriptionButton.setForeground(Color.WHITE);
        inscriptionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        inscriptionButton.setPreferredSize(new Dimension(200, 48));
        inscriptionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        inscriptionButton.addActionListener(e -> {
            // TODO : ajouter ici la logique d'inscription
        });

        right.add(inscriptionButton);
        right.add(Box.createVerticalGlue());

        columns.add(left);

        // Séparateur vertical entre colonnes
        JPanel dividerWrapper = new JPanel(new BorderLayout());
        dividerWrapper.setBackground(Color.WHITE);
        JSeparator vSep = new JSeparator(SwingConstants.VERTICAL);
        vSep.setForeground(new Color(230, 230, 230));
        dividerWrapper.add(vSep, BorderLayout.CENTER);

        // On utilise un panel à 3 colonnes pour insérer le séparateur
        JPanel threeCol = new JPanel(new GridBagLayout());
        threeCol.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        gbc.gridx = 0; gbc.weightx = 1.0;
        threeCol.add(left, gbc);

        gbc.gridx = 1; gbc.weightx = 0; gbc.insets = new Insets(0, 24, 0, 24);
        JSeparator vs = new JSeparator(SwingConstants.VERTICAL);
        vs.setForeground(new Color(230, 230, 230));
        vs.setPreferredSize(new Dimension(1, 1));
        threeCol.add(vs, gbc);

        gbc.gridx = 2; gbc.weightx = 1.0; gbc.insets = new Insets(0, 0, 0, 0);
        threeCol.add(right, gbc);

        // ── Assemblage global ────────────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.add(header, BorderLayout.NORTH);
        body.add(threeCol, BorderLayout.CENTER);

        // ScrollPane invisible (pas de bordure, pas de fond)
        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));    // barre invisible
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        // Vitesse défilement
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panelPrincipal.add(scroll, BorderLayout.CENTER);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════════════════════════════

    private JTextField addReadOnlyField(JPanel parent, String labelTxt, String value) {
        parent.add(buildLabelRow(labelTxt, true));
        parent.add(Box.createVerticalStrut(6));

        JTextField f = new JTextField(value);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(new Color(110, 110, 110));
        f.setBackground(new Color(246, 246, 246));
        f.setEditable(false);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setPreferredSize(new Dimension(300, 46));
        f.setBorder(BorderFactory.createCompoundBorder(
                new MatriculeForm.RoundedBorder(new Color(220, 220, 220), 10, 1),
                new EmptyBorder(0, 2, 0, 2)
        ));

        parent.add(f);
        parent.add(Box.createVerticalStrut(14));
        return f;
    }

    private JTextField addEditableTextField(JPanel parent, String labelTxt, String placeholder) {
        parent.add(buildLabelRow(labelTxt, true));
        parent.add(Box.createVerticalStrut(6));

        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(new Color(170, 170, 170));
        f.setBackground(new Color(250, 250, 250));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setPreferredSize(new Dimension(300, 46));
        applyFieldBorder(f, new Color(215, 215, 215));
        f.setText(placeholder);

        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(new Color(20,20,20)); f.setBackground(Color.WHITE); }
                applyFieldBorder(f, new Color(33, 119, 240));
            }
            @Override public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setForeground(new Color(170,170,170)); f.setBackground(new Color(250,250,250)); f.setText(placeholder); }
                applyFieldBorder(f, new Color(215, 215, 215));
            }
        });

        parent.add(f);
        parent.add(Box.createVerticalStrut(14));
        return f;
    }

    private JPasswordField addPasswordField(JPanel parent, String labelTxt, String placeholder) {
        parent.add(buildLabelRow(labelTxt, true));
        parent.add(Box.createVerticalStrut(6));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(250, 250, 250));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        wrapper.setPreferredSize(new Dimension(300, 46));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatriculeForm.RoundedBorder(new Color(215, 215, 215), 10, 1),
                new EmptyBorder(2, 14, 2, 6)
        ));

        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setForeground(new Color(170, 170, 170));
        pf.setBackground(new Color(250, 250, 250));
        pf.setBorder(null);
        pf.setEchoChar((char) 0);
        pf.setText(placeholder);

        JButton eye = new JButton("⊘");
        eye.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eye.setForeground(new Color(180, 180, 180));
        eye.setBackground(new Color(250, 250, 250));
        eye.setBorderPainted(false); eye.setFocusPainted(false); eye.setContentAreaFilled(false);
        eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eye.setPreferredSize(new Dimension(36, 36));

        final boolean[] vis = {false};
        eye.addActionListener(ev -> {
            vis[0] = !vis[0];
            if (String.valueOf(pf.getPassword()).equals(placeholder)) return;
            pf.setEchoChar(vis[0] ? (char) 0 : '●');
            eye.setText(vis[0] ? "◎" : "⊘");
        });

        pf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (String.valueOf(pf.getPassword()).equals(placeholder)) {
                    pf.setText(""); pf.setForeground(new Color(20,20,20));
                    pf.setBackground(Color.WHITE); wrapper.setBackground(Color.WHITE); eye.setBackground(Color.WHITE);
                    pf.setEchoChar(vis[0] ? (char) 0 : '●');
                }
                wrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(33,119,240), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (pf.getPassword().length == 0) {
                    pf.setForeground(new Color(170,170,170)); pf.setBackground(new Color(250,250,250));
                    wrapper.setBackground(new Color(250,250,250)); eye.setBackground(new Color(250,250,250));
                    pf.setEchoChar((char) 0); pf.setText(placeholder);
                }
                wrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(215,215,215), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
        });

        wrapper.add(pf, BorderLayout.CENTER);
        wrapper.add(eye, BorderLayout.EAST);

        parent.add(wrapper);
        parent.add(Box.createVerticalStrut(14));
        return pf;
    }

    private JPanel buildLabelRow(String labelTxt, boolean required) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        if (required) {
            JLabel star = new JLabel("* ");
            star.setFont(new Font("Segoe UI", Font.BOLD, 13));
            star.setForeground(new Color(220, 53, 69));
            row.add(star);
        }
        JLabel lbl = new JLabel(labelTxt);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(50, 50, 50));
        row.add(lbl);
        return row;
    }

    private void applyFieldBorder(JTextField f, Color c) {
        f.setBorder(BorderFactory.createCompoundBorder(
                new MatriculeForm.RoundedBorder(c, 10, 1),
                new EmptyBorder(0, 2, 0, 2)
        ));
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public JPanel getPanelPrincipal() { return panelPrincipal; }

    public String getEmail() {
        String v = emailField.getText().trim();
        return v.equals("Entrez votre email") ? "" : v;
    }

    public String getPassword() {
        String v = String.valueOf(passwordField.getPassword());
        return v.equals("Entrez votre mot de passe") ? "" : v;
    }

    public String getConfirmPassword() {
        String v = String.valueOf(confirmPasswordField.getPassword());
        return v.equals("Confirmez votre mot de passe") ? "" : v;
    }

    public JButton getInscriptionButton() { return inscriptionButton; }

    public void addInscriptionListener(java.awt.event.ActionListener l) {
        inscriptionButton.addActionListener(l);
    }

    // ── Main de test ─────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            JFrame frame = new JFrame("Test – Inscription");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            InscriptionForm form = new InscriptionForm("Dupont", "Jean", "Informatique", "Licence 3");
            frame.setContentPane(form.getPanelPrincipal());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
