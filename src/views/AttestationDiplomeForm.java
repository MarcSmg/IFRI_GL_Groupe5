package views;

import javax.swing.*;
import java.util.Calendar;

/**
 * Formulaire de demande d'attestation de diplôme.
 */
public class AttestationDiplomeForm extends BaseDocumentForm {

    private JTextField        anneeObtentionField;
    private JComboBox<String> diplomeCombo;
    private JTextField        filiereField;
    private JTextField        mentionField;
    private JTextField        numDiplomeField;
    private JComboBox<String> langueCombo;
    private JTextField        organismeDestField;

    public AttestationDiplomeForm() {
        init();
    }

    @Override
    protected String getFormTitle()    { return "Attestation de diplôme"; }

    @Override
    protected String getFormSubtitle() { return "Document provisoire en attente de l'émission du diplôme officiel."; }

    @Override
    protected void buildFormContent(JPanel form) {

        addSectionSeparator(form, "Diplôme concerné");

        int y = Calendar.getInstance().get(Calendar.YEAR);
        anneeObtentionField = addTextField(form,
                "Année d'obtention", String.valueOf(y));

        diplomeCombo = addComboBox(form,
                "Type de diplôme",
                new String[]{"Licence (Bac+3)",
                             "Master (Bac+5)",
                             "Doctorat (Bac+8)",
                             "DUT / BTS",
                             "Autre"});

        filiereField = addTextField(form,
                "Filière / Spécialité", "Ex : Informatique, Gestion…");

        addSectionSeparator(form, "Détails supplémentaires");

        mentionField = addOptionalTextField(form,
                "Mention obtenue (optionnel)",
                "Ex : Bien, Très Bien, Félicitations du jury…");

        numDiplomeField = addOptionalTextField(form,
                "Numéro de diplôme (si connu)",
                "Ex : DIP-2024-0987");

        addSectionSeparator(form, "Options du document");

        langueCombo = addComboBox(form,
                "Langue du document",
                new String[]{"Français", "Anglais", "Bilingue (FR/EN)"});

        organismeDestField = addOptionalTextField(form,
                "Organisme destinataire (optionnel)",
                "Ex : Employeur, Administration, Ambassade…");
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public String getAnneeObtention()  { return anneeObtentionField.getText().trim(); }
    public String getTypeDiplome()     { return (String) diplomeCombo.getSelectedItem(); }
    public String getFiliere()         { return filiereField.getText().trim(); }
    public String getMention()         { return mentionField.getText().trim(); }
    public String getNumDiplome()      { return numDiplomeField.getText().trim(); }
    public String getLangue()          { return (String) langueCombo.getSelectedItem(); }
    public String getOrganismeDest()   { return organismeDestField.getText().trim(); }
}
