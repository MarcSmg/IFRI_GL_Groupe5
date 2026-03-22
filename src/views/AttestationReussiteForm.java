package views;

import javax.swing.*;
import java.util.Calendar;

/**
 * Formulaire de demande d'attestation de réussite.
 */
public class AttestationReussiteForm extends BaseDocumentForm {

    private JTextField        anneeAcademiqueField;
    private JComboBox<String> sessionCombo;
    private JComboBox<String> niveauCombo;
    private JTextField        filiereField;
    private JTextField        mentionField;

    public AttestationReussiteForm() {
        init();
    }

    @Override
    protected String getFormTitle()    { return "Attestation de réussite"; }

    @Override
    protected String getFormSubtitle() { return "Confirmez l'année et la session de votre réussite."; }

    @Override
    protected void buildFormContent(JPanel form) {

        addSectionSeparator(form, "Résultats concernés");

        int y = Calendar.getInstance().get(Calendar.YEAR);
        anneeAcademiqueField = addTextField(form,
                "Année académique", y + " - " + (y + 1));

        sessionCombo = addComboBox(form,
                "Session de réussite",
                new String[]{"Session normale", "Session de rattrapage"});

        addSectionSeparator(form, "Niveau & Filière");

        niveauCombo = addComboBox(form,
                "Niveau validé",
                new String[]{"Licence 1", "Licence 2", "Licence 3",
                             "Master 1",  "Master 2",  "Doctorat"});

        filiereField = addTextField(form,
                "Filière / Spécialité", "Ex : Génie Logiciel, Réseaux…");

        mentionField = addOptionalTextField(form,
                "Mention obtenue (optionnel)",
                "Ex : Passable, Assez Bien, Bien, Très Bien…");

        addSectionSeparator(form, "Destination");

    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public String getAnneeAcademique() { return anneeAcademiqueField.getText().trim(); }
    public String getSession()         { return (String) sessionCombo.getSelectedItem(); }
    public String getNiveau()          { return (String) niveauCombo.getSelectedItem(); }
    public String getFiliere()         { return filiereField.getText().trim(); }
    public String getMention()         { return mentionField.getText().trim(); }
}
