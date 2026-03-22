package views;

import javax.swing.*;
import java.util.Calendar;

/**
 * Formulaire de demande de relevé de notes.
 */
public class ReleveNotesForm extends BaseDocumentForm {

    private JTextField        anneeAcademiqueField;
    private JComboBox<String> sessionCombo;
    private JComboBox<String> niveauCombo;
    private JComboBox<String> semestreCombo;
    private JTextField        filiereField;
    private JComboBox<String> typeReleveCombo;

    public ReleveNotesForm() {
        init();
    }

    @Override
    protected String getFormTitle()    { return "Relevé de notes"; }

    @Override
    protected String getFormSubtitle() { return "Spécifiez la session et le semestre concernés par la demande."; }

    @Override
    protected void buildFormContent(JPanel form) {

        addSectionSeparator(form, "Période concernée");

        int y = Calendar.getInstance().get(Calendar.YEAR);
        anneeAcademiqueField = addTextField(form,
                "Année académique", y + " - " + (y + 1));

        sessionCombo = addComboBox(form,
                "Session d'examen",
                new String[]{"Session normale", "Session de rattrapage"});

        semestreCombo = addComboBox(form,
                "Semestre",
                new String[]{"Semestre 1", "Semestre 2",
                             "Semestres 1 & 2 (année complète)"});

        addSectionSeparator(form, "Niveau & Filière");

        niveauCombo = addComboBox(form,
                "Niveau d'études",
                new String[]{"Licence 1", "Licence 2", "Licence 3",
                             "Master 1",  "Master 2",  "Doctorat"});

        filiereField = addTextField(form,
                "Filière / Spécialité", "Ex : Informatique, Génie Civil…");

        addSectionSeparator(form, "Type de document");

        typeReleveCombo = addComboBox(form,
                "Format du relevé",
                new String[]{"Provisoire (signé électroniquement)",
                             "Officiel (cachet et signature)",
                             "Traduit en anglais"});
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public String getAnneeAcademique() { return anneeAcademiqueField.getText().trim(); }
    public String getSession()         { return (String) sessionCombo.getSelectedItem(); }
    public String getSemestre()        { return (String) semestreCombo.getSelectedItem(); }
    public String getNiveau()          { return (String) niveauCombo.getSelectedItem(); }
    public String getFiliere()         { return filiereField.getText().trim(); }
    public String getTypeReleve()      { return (String) typeReleveCombo.getSelectedItem(); }
}
