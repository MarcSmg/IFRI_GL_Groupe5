package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import controllers.*;

public class ConnexionForm {

    private JPanel panelPrincipal;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton connexionButton;
    private JLabel inscriptionLink;
    private AuthController authController ; 

    public ConnexionForm() {
        authController = new AuthController();
        initComponents();
    }

  
    private void initComponents() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setPreferredSize(new Dimension(580, 520));
        panelPrincipal.setBorder(new EmptyBorder(60, 80, 56, 80));

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Connexion");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(18, 18, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Déverrouillez votre espace personnel.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(145, 145, 145));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(36));

        // ── Formulaire ───────────────────────────────────────────────────
        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        // Email / Identifiant
        form.add(buildLabelRow("Email / Identifiant"));
        form.add(Box.createVerticalStrut(7));

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setForeground(new Color(170, 170, 170));
        emailField.setBackground(new Color(250, 250, 250));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        emailField.setPreferredSize(new Dimension(420, 50));
        applyFieldBorder(emailField, new Color(215, 215, 215));

        final String emailPh = "Entrez votre email ou identifiant";
        emailField.setText(emailPh);
        emailField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (emailField.getText().equals(emailPh)) {
                    emailField.setText(""); emailField.setForeground(new Color(20,20,20)); emailField.setBackground(Color.WHITE);
                }
                applyFieldBorder(emailField, new Color(33, 119, 240));
            }
            @Override public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setForeground(new Color(170,170,170)); emailField.setBackground(new Color(250,250,250)); emailField.setText(emailPh);
                }
                applyFieldBorder(emailField, new Color(215, 215, 215));
            }
        });

        form.add(emailField);
        form.add(Box.createVerticalStrut(18));

        // Mot de passe
        form.add(buildLabelRow("Mot de passe"));
        form.add(Box.createVerticalStrut(7));

        JPanel pwWrapper = new JPanel(new BorderLayout());
        pwWrapper.setBackground(new Color(250, 250, 250));
        pwWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pwWrapper.setPreferredSize(new Dimension(420, 50));
        pwWrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatriculeForm.RoundedBorder(new Color(215, 215, 215), 10, 1),
                new EmptyBorder(2, 14, 2, 6)
        ));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(new Color(170, 170, 170));
        passwordField.setBackground(new Color(250, 250, 250));
        passwordField.setBorder(null);
        passwordField.setEchoChar((char) 0);
        final String pwPh = "Entrez votre mot de passe";
        passwordField.setText(pwPh);

        JButton eye = new JButton("⊘");
        eye.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        eye.setForeground(new Color(180, 180, 180));
        eye.setBackground(new Color(250, 250, 250));
        eye.setBorderPainted(false); eye.setFocusPainted(false); eye.setContentAreaFilled(false);
        eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eye.setPreferredSize(new Dimension(38, 38));

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
                    passwordField.setText(""); passwordField.setForeground(new Color(20,20,20));
                    passwordField.setBackground(Color.WHITE); pwWrapper.setBackground(Color.WHITE); eye.setBackground(Color.WHITE);
                    passwordField.setEchoChar(pwVis[0] ? (char) 0 : '●');
                }
                pwWrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(33,119,240), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setForeground(new Color(170,170,170)); passwordField.setBackground(new Color(250,250,250));
                    pwWrapper.setBackground(new Color(250,250,250)); eye.setBackground(new Color(250,250,250));
                    passwordField.setEchoChar((char) 0); passwordField.setText(pwPh);
                }
                pwWrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(215,215,215), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
        });

        pwWrapper.add(passwordField, BorderLayout.CENTER);
        pwWrapper.add(eye, BorderLayout.EAST);
        form.add(pwWrapper);
        form.add(Box.createVerticalStrut(28));

        // ── Bouton Connexion ─────────────────────────────────────────────
        connexionButton = new MatriculeForm.RoundedButton(
                "Se connecter", new Color(33, 119, 240), new Color(21, 96, 200), 12);
        connexionButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        connexionButton.setForeground(Color.WHITE);
        connexionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        connexionButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        connexionButton.setPreferredSize(new Dimension(420, 50));
        connexionButton.addActionListener(e -> {
            String emailOrIdentifier = emailField.getText();
            char[] passwordChar = passwordField.getPassword();
            String password = new String(passwordChar);
            if (authController.tryConnection(emailOrIdentifier, password)){
                // on récupere le role pour savoir ou redirigé 
            }
            
        });

        form.add(connexionButton);
        form.add(Box.createVerticalStrut(22));

        // ── Séparateur + lien inscription ────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(230, 230, 230));
        form.add(sep);
        form.add(Box.createVerticalStrut(20));

        JPanel noAccountRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        noAccountRow.setBackground(Color.WHITE);

        JLabel noAccountLbl = new JLabel("Vous n'avez pas encore de compte ?");
        noAccountLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccountLbl.setForeground(new Color(100, 100, 100));

        inscriptionLink = new JLabel("S'inscrire");
        inscriptionLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inscriptionLink.setForeground(new Color(33, 119, 240));
        inscriptionLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        inscriptionLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                inscriptionLink.setText("<html><u>S'inscrire</u></html>");
            }
            @Override public void mouseExited(MouseEvent e) {
                inscriptionLink.setText("S'inscrire");
            }
        });

        noAccountRow.add(noAccountLbl);
        noAccountRow.add(inscriptionLink);
        form.add(noAccountRow);
        form.add(Box.createVerticalStrut(12));

        // Bouton "Créer un compte" outline
        MatriculeForm.OutlineButton createBtn = new MatriculeForm.OutlineButton(
                "Créer un compte", new Color(210, 210, 210), new Color(245, 245, 245), 12);
        createBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        createBtn.setForeground(new Color(50, 50, 50));
        createBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        createBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        createBtn.setPreferredSize(new Dimension(420, 48));
        createBtn.addActionListener(e -> {
            // TODO : naviguer vers MatriculeForm / InscriptionForm
        });

        form.add(createBtn);

        // ── Assemblage global ────────────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.add(header, BorderLayout.NORTH);
        body.add(form, BorderLayout.CENTER);

        // ScrollPane invisible
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

    public void addConnexionListener(ActionListener l) { connexionButton.addActionListener(l); }

    public void addInscriptionLinkListener(MouseAdapter a) { inscriptionLink.addMouseListener(a); }

    // ── Main de test ─────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            JFrame frame = new JFrame("Test – Connexion");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(new ConnexionForm().getPanelPrincipal());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
