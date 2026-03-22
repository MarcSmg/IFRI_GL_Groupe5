package views;

import models.Demand;

import javax.swing.*;
import java.awt.*;

public class AgentDemandDetailsView extends JPanel {

    private JLabel idLabel = new JLabel();
    private JLabel typeLabel = new JLabel();
    private JLabel statusLabel = new JLabel();

    private JButton generateActBtn = new JButton("Générer acte");
    private JButton openActBtn = new JButton("Ouvrir acte");

    public AgentDemandDetailsView() {
        setLayout(new BorderLayout(10, 10));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(idLabel);
        content.add(typeLabel);
        content.add(statusLabel);

        content.add(Box.createVerticalStrut(20));
        content.add(generateActBtn);
        content.add(openActBtn);

        add(content, BorderLayout.CENTER);

        openActBtn.setVisible(false); // hidden by default
    }

    public void setDemandData(Demand d) {
        idLabel.setText("ID: " + d.getId());
        typeLabel.setText("Type: " + d.getActType());
        statusLabel.setText("Statut: " + d.getStatus());
    }

    public void showActAvailable() {
        openActBtn.setVisible(true);
        generateActBtn.setEnabled(false);
    }

    public void onGenerateAct(Runnable action) {
        generateActBtn.addActionListener(e -> action.run());
    }

    public void onOpenAct(Runnable action) {
        openActBtn.addActionListener(e -> action.run());
    }
}