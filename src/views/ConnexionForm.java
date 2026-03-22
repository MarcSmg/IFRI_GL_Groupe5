package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import controllers.*;
import models.*;

public class ConnexionForm {

    private User authenticatedUser;
    private JPanel panelPrincipal;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton connexionButton;
    private JLabel inscriptionLink;
    private AuthController authController;
    private MainFrame mainFrame;

    public ConnexionForm() {
        authController = new AuthController();
        mainFrame = new MainFrame();
        initComponents();
    }

    private void initComponents() {

        // ─────────────────────────────────────────────────────────────────
        // panelPrincipal : conteneur neutre — PAS de setPreferredSize,
        // PAS de setBorder. La taille vient de NavigationManager.setSize().
        // ─────────────────────────────────────────────────────────────────
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // ── En-tête ──────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Connexion");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(18, 18, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Déverrouillez votre espace personnel.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(145, 145, 145));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(28));

        // ── Formulaire ───────────────────────────────────────────────────
        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        // Email
        form.add(buildLabelRow("Email / Identifiant"));
        form.add(Box.createVerticalStrut(6));

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailField.setForeground(new Color(170, 170, 170));
        emailField.setBackground(new Color(250, 250, 250));
        // ── CLÉ : setMaximumSize uniquement, PAS de setPreferredSize fixe ──
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        applyFieldBorder(emailField, new Color(215, 215, 215));

        final String emailPh = "Entrez votre email ou identifiant";
        emailField.setText(emailPh);
        emailField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (emailField.getText().equals(emailPh)) {
                    emailField.setText("");
                    emailField.setForeground(new Color(20, 20, 20));
                    emailField.setBackground(Color.WHITE);
                }
                applyFieldBorder(emailField, new Color(33, 119, 240));
            }
            @Override public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setForeground(new Color(170, 170, 170));
                    emailField.setBackground(new Color(250, 250, 250));
                    emailField.setText(emailPh);
                }
                applyFieldBorder(emailField, new Color(215, 215, 215));
            }
        });
        form.add(emailField);
        form.add(Box.createVerticalStrut(16));

        // Mot de passe
        form.add(buildLabelRow("Mot de passe"));
        form.add(Box.createVerticalStrut(6));

        JPanel pwWrapper = new JPanel(new BorderLayout());
        pwWrapper.setBackground(new Color(250, 250, 250));
        // ── CLÉ : setMaximumSize uniquement ──
        pwWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        pwWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwWrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatriculeForm.RoundedBorder(new Color(215, 215, 215), 10, 1),
                new EmptyBorder(2, 14, 2, 6)
        ));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setForeground(new Color(170, 170, 170));
        passwordField.setBackground(new Color(250, 250, 250));
        passwordField.setBorder(null);
        passwordField.setEchoChar((char) 0);
        final String pwPh = "Entrez votre mot de passe";
        passwordField.setText(pwPh);

        JButton eye = new JButton("⊘");
        eye.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eye.setForeground(new Color(180, 180, 180));
        eye.setBackground(new Color(250, 250, 250));
        eye.setBorderPainted(false); eye.setFocusPainted(false); eye.setContentAreaFilled(false);
        eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eye.setPreferredSize(new Dimension(36, 36));

        final boolean[] pwVis = {false};
        eye.addActionListener(ev -> {
            pwVis[0] = !pwVis[0];
            if (String.valueOf(passwordField.getPassword()).equals(pwPh)) return;
            passwordField.setEchoChar(pwVis[0] ? (char) 0 : '●');
            eye.setText(pwVis[0] ? "◎" : "⊘");
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(pwPh)) {
                    passwordField.setText(""); passwordField.setForeground(new Color(20, 20, 20));
                    passwordField.setBackground(Color.WHITE); pwWrapper.setBackground(Color.WHITE); eye.setBackground(Color.WHITE);
                    passwordField.setEchoChar(pwVis[0] ? (char) 0 : '●');
                }
                pwWrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(33, 119, 240), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setForeground(new Color(170, 170, 170)); passwordField.setBackground(new Color(250, 250, 250));
                    pwWrapper.setBackground(new Color(250, 250, 250)); eye.setBackground(new Color(250, 250, 250));
                    passwordField.setEchoChar((char) 0); passwordField.setText(pwPh);
                }
                pwWrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(215, 215, 215), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
        });

        pwWrapper.add(passwordField, BorderLayout.CENTER);
        pwWrapper.add(eye, BorderLayout.EAST);
        form.add(pwWrapper);
        form.add(Box.createVerticalStrut(24));

        // ── Bouton Connexion ─────────────────────────────────────────────
        connexionButton = new MatriculeForm.RoundedButton(
                "Se connecter", new Color(33, 119, 240), new Color(21, 96, 200), 12);
        connexionButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        connexionButton.setForeground(Color.WHITE);
        connexionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        // ── CLÉ : setMaximumSize uniquement ──
        connexionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        connexionButton.addActionListener(e -> {
            String emailOrIdentifier = emailField.getText().trim();
            char[] passwordChar = passwordField.getPassword();
            String password = new String(passwordChar).trim();

            if (emailOrIdentifier.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Veuillez remplir tous les champs obligatoires.",
                        "Champs vides", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (emailOrIdentifier.contains("@") && !emailOrIdentifier.matches(emailRegex)) {
                JOptionPane.showMessageDialog(panelPrincipal,
                        "Le format de l'adresse email est incorrect.",
                        "Format invalide", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var authStatus = authController.tryConnection(emailOrIdentifier, password);
            User user = authController.getAuthenticatedUser(emailOrIdentifier);
            String messageAuth = authStatus.name();

            switch (messageAuth) {
                case "SUCCESS" -> {
                    if (user != null) NavigationManager.closeCurrent(panelPrincipal);
                    JOptionPane.showMessageDialog(panelPrincipal, "Bienvenue ! Connexion réussie.");
                }
                case "NOT_FOUND" -> JOptionPane.showMessageDialog(panelPrincipal,
                        "Identifiants incorrects.", "Utilisateur inconnu", JOptionPane.ERROR_MESSAGE);
                case "WRONG_PASSWORD" -> {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "Mot de passe incorrect. Veuillez réessayer.",
                            "Erreur de mot de passe", JOptionPane.WARNING_MESSAGE);
                    passwordField.setText("");
                    passwordField.requestFocus();
                }
                case "MUST_CHANGE_PASSWORD" -> {
                    JOptionPane.showMessageDialog(panelPrincipal,
                            "Première connexion : veuillez modifier votre mot de passe.",
                            "Sécurité", JOptionPane.INFORMATION_MESSAGE);
                    NavigationManager.closeCurrent(panelPrincipal);
                    NavigationManager.showChangePassword();
                }
                default -> JOptionPane.showMessageDialog(panelPrincipal,
                        "Erreur inattendue : " + messageAuth, "Erreur Système", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(connexionButton);
        form.add(Box.createVerticalStrut(20));

        // ── Séparateur ───────────────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(230, 230, 230));
        form.add(sep);
        form.add(Box.createVerticalStrut(16));

        // Ligne "Pas de compte"
        JPanel noAccountRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        noAccountRow.setBackground(Color.WHITE);
        noAccountRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel noAccountLbl = new JLabel("Vous n'avez pas encore de compte ?");
        noAccountLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccountLbl.setForeground(new Color(100, 100, 100));
        inscriptionLink = new JLabel("S'inscrire");
        inscriptionLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inscriptionLink.setForeground(new Color(33, 119, 240));
        inscriptionLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        inscriptionLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { inscriptionLink.setText("<html><u>S'inscrire</u></html>"); }
            @Override public void mouseExited(MouseEvent e)  { inscriptionLink.setText("S'inscrire"); }
        });
        noAccountRow.add(noAccountLbl);
        noAccountRow.add(inscriptionLink);
        form.add(noAccountRow);
        form.add(Box.createVerticalStrut(10));

        // Bouton Créer un compte
        MatriculeForm.OutlineButton createBtn = new MatriculeForm.OutlineButton(
                "Créer un compte", new Color(210, 210, 210), new Color(245, 245, 245), 12);
        createBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        createBtn.setForeground(new Color(50, 50, 50));
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        // ── CLÉ : setMaximumSize uniquement ──
        createBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        createBtn.addActionListener(e -> {
            NavigationManager.closeCurrent(panelPrincipal);
            NavigationManager.showMatricule();
        });
        form.add(createBtn);

        // ── Assemblage — marge sur body, PAS sur panelPrincipal ──────────
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(28, 32, 28, 32)); // ← padding ICI
        body.add(header, BorderLayout.NORTH);
        body.add(form,   BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panelPrincipal.add(scroll, BorderLayout.CENTER);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private JPanel buildLabelRow(String labelTxt) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel star = new JLabel("* ");
        star.setFont(new Font("Segoe UI", Font.BOLD, 13));
        star.setForeground(new Color(220, 53, 69));
        row.add(star);
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
        return v.equals("Entrez votre email ou identifiant") ? "" : v;
    }

    public String getPassword() {
        String v = String.valueOf(passwordField.getPassword());
        return v.equals("Entrez votre mot de passe") ? "" : v;
    }

    public JButton getConnexionButton() { return connexionButton; }
    public void addConnexionListener(ActionListener l)    { connexionButton.addActionListener(l); }
    public void addInscriptionLinkListener(MouseAdapter a){ inscriptionLink.addMouseListener(a); }
}
