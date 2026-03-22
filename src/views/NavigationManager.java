package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import controllers.*;


public class NavigationManager {
    private static final AuthController authController = new AuthController();

    public static void showConnexion() {
        ConnexionForm form = new ConnexionForm();
        configureAndShow(form.getPanelPrincipal(), "Connexion", 460, 540);
    }

    public static void showMatricule() {
        MatriculeForm form = new MatriculeForm();
        authController.controlerMatriculeForm(form);
        configureAndShow(form.getPanelPrincipal(), "Identification", 480, 360);
    }

    public static void showInscription(String nom, String prenom, String filiere, String niveau) {
        InscriptionForm form = new InscriptionForm(nom, prenom, filiere, niveau);
        configureAndShow(form.getPanelPrincipal(), "Inscription", 900, 620);
    }

    public static void showChangePassword(int userId) {
        ChangePasswordForm form = new ChangePasswordForm();
        authController.controlerChangePasswordForm(form, userId);
        configureAndShow(form.getPanelPrincipal(), "Changer le mot de passe", 460, 500);
    }

    public static void closeCurrent(JComponent component) {
        Window win = SwingUtilities.getWindowAncestor(component);
        if (win != null) win.dispose();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Méthode centrale
    //
    //  RÈGLE ABSOLUE :
    //   - On ne touche PAS à contentPanel.setPreferredSize()
    //   - On n'appelle PAS dialog.pack()
    //   - La taille est fixée UNE SEULE FOIS via dialog.setSize(w, h)
    //   - Les composants dans les formulaires utilisent setMaximumSize()
    //     + Integer.MAX_VALUE en largeur → ils s'étirent jusqu'à remplir
    //     la largeur disponible du dialog, sans jamais déborder.
    // ════════════════════════════════════════════════════════════════════════
    private static void configureAndShow(JPanel contentPanel,
                                         String title,
                                         int dialogWidth,
                                         int dialogHeight) {

        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        // Wrapper avec bordure légère
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(Color.WHITE);
        mainWrapper.setBorder(BorderFactory.createLineBorder(new Color(215, 215, 215), 1));

        // Barre de titre fine (bouton ✕)
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        titleBar.setBackground(Color.WHITE);

        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        closeBtn.setForeground(new Color(150, 150, 150));
        closeBtn.setBorder(new EmptyBorder(8, 10, 4, 10));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { closeBtn.setForeground(new Color(220, 53, 69)); }
            @Override public void mouseExited(MouseEvent e)  { closeBtn.setForeground(new Color(150, 150, 150)); }
        });
        closeBtn.addActionListener(e -> dialog.dispose());
        titleBar.add(closeBtn);

        // Déplacement de la fenêtre par drag sur la titleBar
        FrameDragListener drag = new FrameDragListener(dialog);
        titleBar.addMouseListener(drag);
        titleBar.addMouseMotionListener(drag);

        mainWrapper.add(titleBar,     BorderLayout.NORTH);
        mainWrapper.add(contentPanel, BorderLayout.CENTER);

        dialog.setContentPane(mainWrapper);

        // ── UNE SEULE source de vérité pour la taille ────────────────────
        dialog.setSize(dialogWidth, dialogHeight);
        dialog.setMinimumSize(new Dimension(dialogWidth, dialogHeight));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // ── Drag listener ─────────────────────────────────────────────────────────

    public static class FrameDragListener extends MouseAdapter {
        private final JDialog dialog;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JDialog dialog) { this.dialog = dialog; }

        @Override public void mouseReleased(MouseEvent e) { mouseDownCompCoords = null; }
        @Override public void mousePressed(MouseEvent e)  { mouseDownCompCoords = e.getPoint(); }

        @Override public void mouseDragged(MouseEvent e) {
            if (mouseDownCompCoords == null) return;
            Point curr = e.getLocationOnScreen();
            dialog.setLocation(curr.x - mouseDownCompCoords.x,
                               curr.y - mouseDownCompCoords.y);
        }
    }
}
