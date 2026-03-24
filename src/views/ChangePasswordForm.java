package views;

import dao.UserDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ChangePasswordForm {

    private JPanel panelPrincipal;

    private JPasswordField ancienPasswordField;
    private JPasswordField nouveauPasswordField;
    private JPasswordField confirmPasswordField;
    private UserDAO userDAO;

    private JButton confirmerButton;

    public ChangePasswordForm() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {

        // ═══════════════════════════════════════════════════════════════════
        // RÈGLE : panelPrincipal = conteneur neutre
        //   → PAS de setPreferredSize()
        //   → PAS de setBorder()  (la marge est sur "body", dans le scroll)
        // ═══════════════════════════════════════════════════════════════════
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // ── En-tête ──────────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Changer le mot de passe");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(18, 18, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Choisissez un nouveau mot de passe sécurisé.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(145, 145, 145));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(30));

        // ── Formulaire ───────────────────────────────────────────────────
        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        ancienPasswordField  = addPasswordField(form,
                "Ancien mot de passe",           "Entrez votre ancien mot de passe");
        nouveauPasswordField = addPasswordField(form,
                "Nouveau mot de passe",           "Entrez votre nouveau mot de passe");
        confirmPasswordField = addPasswordField(form,
                "Confirmer nouveau mot de passe", "Confirmez votre nouveau mot de passe");

        form.add(Box.createVerticalStrut(24));

        // ── Bouton Confirmer ─────────────────────────────────────────────
        confirmerButton = new MatriculeForm.RoundedButton(
                "Confirmer", new Color(33, 119, 240), new Color(21, 96, 200), 12);
        confirmerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmerButton.setForeground(Color.WHITE);
        confirmerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        // ── CLÉ : setMaximumSize uniquement, PAS de setPreferredSize fixe ──
        confirmerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        confirmerButton.addActionListener(e -> {
            // TODO : logique de changement de mot de passe
        });

        form.add(confirmerButton);
        form.add(Box.createVerticalStrut(14));

        // ── Bouton Annuler ───────────────────────────────────────────────
        MatriculeForm.OutlineButton annulerButton = new MatriculeForm.OutlineButton(
                "Annuler", new Color(210, 210, 210), new Color(245, 245, 245), 12);
        annulerButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        annulerButton.setForeground(new Color(80, 80, 80));
        annulerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        // ── CLÉ : setMaximumSize uniquement ──
        annulerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        annulerButton.addActionListener(e -> {
            NavigationManager.closeCurrent(panelPrincipal);
        });

        form.add(annulerButton);

        // ── Assemblage — marge sur body, PAS sur panelPrincipal ──────────
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(32, 36, 32, 36)); // ← padding ICI
        body.add(header, BorderLayout.NORTH);
        body.add(form,   BorderLayout.CENTER);

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

    // ════════════════════════════════════════════════════════════════════════
    //  Helper — champ mot de passe
    // ════════════════════════════════════════════════════════════════════════

    private JPasswordField addPasswordField(JPanel parent,
                                            String labelTxt,
                                            String placeholder) {
        // Label
        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelRow.setBackground(Color.WHITE);
        labelRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel star = new JLabel("* ");
        star.setFont(new Font("Segoe UI", Font.BOLD, 13));
        star.setForeground(new Color(220, 53, 69));
        JLabel lbl = new JLabel(labelTxt);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(50, 50, 50));
        labelRow.add(star);
        labelRow.add(lbl);
        parent.add(labelRow);
        parent.add(Box.createVerticalStrut(6));

        // Wrapper
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(250, 250, 250));
        // ── CLÉ : setMaximumSize uniquement ──
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                new MatriculeForm.RoundedBorder(new Color(215, 215, 215), 10, 1),
                new EmptyBorder(2, 14, 2, 6)
        ));

        // Champ
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setForeground(new Color(170, 170, 170));
        pf.setBackground(new Color(250, 250, 250));
        pf.setBorder(null);
        pf.setEchoChar((char) 0);
        pf.setText(placeholder);

        // Bouton œil
        JButton eye = new JButton("⊘");
        eye.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eye.setForeground(new Color(180, 180, 180));
        eye.setBackground(new Color(250, 250, 250));
        eye.setBorderPainted(false);
        eye.setFocusPainted(false);
        eye.setContentAreaFilled(false);
        eye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        eye.setPreferredSize(new Dimension(36, 36));

        final boolean[] visible = {false};

        eye.addActionListener(ev -> {
            visible[0] = !visible[0];
            if (String.valueOf(pf.getPassword()).equals(placeholder)) return;
            pf.setEchoChar(visible[0] ? (char) 0 : '●');
            eye.setText(visible[0] ? "◎" : "⊘");
        });

        pf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (String.valueOf(pf.getPassword()).equals(placeholder)) {
                    pf.setText("");
                    pf.setForeground(new Color(20, 20, 20));
                    pf.setBackground(Color.WHITE);
                    wrapper.setBackground(Color.WHITE);
                    eye.setBackground(Color.WHITE);
                    pf.setEchoChar(visible[0] ? (char) 0 : '●');
                }
                wrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(33, 119, 240), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (pf.getPassword().length == 0) {
                    pf.setForeground(new Color(170, 170, 170));
                    pf.setBackground(new Color(250, 250, 250));
                    wrapper.setBackground(new Color(250, 250, 250));
                    eye.setBackground(new Color(250, 250, 250));
                    pf.setEchoChar((char) 0);
                    pf.setText(placeholder);
                }
                wrapper.setBorder(BorderFactory.createCompoundBorder(
                        new MatriculeForm.RoundedBorder(new Color(215, 215, 215), 10, 1),
                        new EmptyBorder(2, 14, 2, 6)));
            }
        });

        wrapper.add(pf,  BorderLayout.CENTER);
        wrapper.add(eye, BorderLayout.EAST);

        parent.add(wrapper);
        parent.add(Box.createVerticalStrut(16));
        return pf;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Accesseurs
    // ════════════════════════════════════════════════════════════════════════

    public JPanel getPanelPrincipal() { return panelPrincipal; }

    public String getAncienPassword() {
        String v = String.valueOf(ancienPasswordField.getPassword());
        return v.equals("Entrez votre ancien mot de passe") ? "" : v;
    }

    public String getNouveauPassword() {
        String v = String.valueOf(nouveauPasswordField.getPassword());
        return v.equals("Entrez votre nouveau mot de passe") ? "" : v;
    }

    public String getConfirmPassword() {
        String v = String.valueOf(confirmPasswordField.getPassword());
        return v.equals("Confirmez votre nouveau mot de passe") ? "" : v;
    }

    public JButton getConfirmerButton() { return confirmerButton; }

    public void addConfirmerListener(ActionListener l) {
        confirmerButton.addActionListener(l);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Main de test
    // ════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            JFrame frame = new JFrame("Test – Changement de mot de passe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(new ChangePasswordForm().getPanelPrincipal());

            // ── CLÉ : setSize() au lieu de pack() ──
            // pack() recalcule depuis le contenu naturel du JScrollPane
            // et ignore nos contraintes → débordement garanti.
            // setSize() impose UNE seule taille, le scroll s'adapte.
            frame.setSize(460, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
