package views;

import javax.swing.*;
import java.util.Calendar;

/**
 * Formulaire de demande de certificat de scolarité.
 */
public class CertificatScolariteForm extends BaseDocumentForm {

    private JTextField        anneeAcademiqueField;
    private JComboBox<String> niveauCombo;
    private JTextField        filiereField;

    public CertificatScolariteForm() {
        init();
    }

    @Override
    protected String getFormTitle()    { return "Certificat de scolarité"; }

    @Override
    protected String getFormSubtitle() { return ""; }

    @Override
    protected void buildFormContent(JPanel form) {

        addSectionSeparator(form, "Informations académiques");

        int y = Calendar.getInstance().get(Calendar.YEAR);
        anneeAcademiqueField = addTextField(form,
                "Année académique", y + " - " + (y + 1));

        niveauCombo = addComboBox(form,
                "Niveau d'études",
                new String[]{"Licence 1", "Licence 2", "Licence 3",
                             "Master 1",  "Master 2",  "Doctorat"});

        filiereField = addTextField(form,
                "Filière / Spécialité", "Ex : Génie Logiciel");

        addSectionSeparator(form, "Options du document");



        
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public String getAnneeAcademique() { return anneeAcademiqueField.getText().trim(); }
    public String getNiveau()          { return (String) niveauCombo.getSelectedItem(); }
    public String getFiliere()         { return filiereField.getText().trim(); }
}
