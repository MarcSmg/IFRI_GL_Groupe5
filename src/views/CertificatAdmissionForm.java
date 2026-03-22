package views;

import javax.swing.*;
import java.util.Calendar;

/**
 * Formulaire de demande de certificat d'admission.
 */
public class CertificatAdmissionForm extends BaseDocumentForm {

    private JTextField        anneeAcademiqueField;
    private JComboBox<String> niveauAdmisCombo;
    private JTextField        filiereField;
    private JTextField        numConcourField;
    private JTextField        etablissementDestField;
    private JComboBox<String> langueCombo;

    public CertificatAdmissionForm() {
        init();
    }

    @Override
    protected String getFormTitle()    { return "Certificat d'admission"; }

    @Override
    protected String getFormSubtitle() { return "Justifiez votre admission pour une démarche administrative."; }

    @Override
    protected void buildFormContent(JPanel form) {

        addSectionSeparator(form, "Informations d'admission");

        int y = Calendar.getInstance().get(Calendar.YEAR);
        anneeAcademiqueField = addTextField(form,
                "Année d'admission", y + " - " + (y + 1));

        niveauAdmisCombo = addComboBox(form,
                "Niveau auquel vous êtes admis(e)",
                new String[]{"Licence 1", "Licence 2", "Licence 3",
                             "Master 1",  "Master 2",  "Doctorat"});

        filiereField = addTextField(form,
                "Filière / Spécialité", "Ex : Informatique, Génie Civil…");

        numConcourField = addOptionalTextField(form,
                "Numéro de concours / dossier (optionnel)",
                "Ex : INF2024-0042");

        addSectionSeparator(form, "Options du document");

        etablissementDestField = addOptionalTextField(form,
                "Établissement destinataire (optionnel)",
                "Ex : Service des visas, Ambassade…");

        langueCombo = addComboBox(form,
                "Langue du document",
                new String[]{"Français", "Anglais", "Bilingue (FR/EN)"});
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public String getAnneeAdmission()     { return anneeAcademiqueField.getText().trim(); }
    public String getNiveauAdmis()        { return (String) niveauAdmisCombo.getSelectedItem(); }
    public String getFiliere()            { return filiereField.getText().trim(); }
    public String getNumConcours()        { return numConcourField.getText().trim(); }
    public String getEtablissementDest()  { return etablissementDestField.getText().trim(); }
    public String getLangue()             { return (String) langueCombo.getSelectedItem(); }
}
