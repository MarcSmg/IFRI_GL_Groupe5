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
    private JTextField        organismeDestField;
    private JComboBox<String> nombreCopiesCombo;

    public CertificatScolariteForm() {
        init();
    }

    @Override
    protected String getFormTitle()    { return "Certificat de scolarité"; }

    @Override
    protected String getFormSubtitle() { return "Précisez l'année et le niveau pour la génération du certificat."; }

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

        organismeDestField = addOptionalTextField(form,
                "Organisme destinataire (optionnel)",
                "Ex : CNSS, Mairie, Administration…");

        nombreCopiesCombo = addComboBox(form,
                "Nombre de copies demandées",
                new String[]{"1 copie", "2 copies", "3 copies", "5 copies"});
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public String getAnneeAcademique() { return anneeAcademiqueField.getText().trim(); }
    public String getNiveau()          { return (String) niveauCombo.getSelectedItem(); }
    public String getFiliere()         { return filiereField.getText().trim(); }
    public String getOrganismeDest()   { return organismeDestField.getText().trim(); }
    public String getNombreCopies()    { return (String) nombreCopiesCombo.getSelectedItem(); }
}
