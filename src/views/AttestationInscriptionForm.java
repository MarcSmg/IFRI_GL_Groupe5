package views;

import javax.swing.*;
import java.util.Calendar;

/**
 * Formulaire de demande d'attestation d'inscription.
 */
public class AttestationInscriptionForm extends BaseDocumentForm {

    private JTextField    anneeAcademiqueField;
    private JComboBox<String> semestreCombo;
    private JTextField    etablissementDestField;
    private JTextArea     motifArea;

    public AttestationInscriptionForm() {
        init();
    }

    @Override
    protected String getFormTitle()    { return "Attestation d'inscription"; }

    @Override
    protected String getFormSubtitle() { return "Renseignez les informations pour votre demande."; }

    @Override
    protected void buildFormContent(JPanel form) {

        addSectionSeparator(form, "Informations académiques");

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String defaultYear = currentYear + " - " + (currentYear + 1);

        anneeAcademiqueField = addTextField(form,
                "Année académique", defaultYear);

        semestreCombo = addComboBox(form,
                "Semestre / Période",
                new String[]{"Semestre 1", "Semestre 2", "Année complète"});

        addSectionSeparator(form, "Destination");

        etablissementDestField = addOptionalTextField(form,
                "Établissement destinataire (optionnel)",
                "Ex : Ambassade, Banque, Entreprise…");

        addSectionSeparator(form, "Justification");

        motifArea = addTextArea(form,
                "Motif de la demande (optionnel)",
                "Décrivez brièvement l'utilisation prévue de ce document…", 3);
    }

    // ── Accesseurs ───────────────────────────────────────────────────────────

    public String getAnneeAcademique()   { return anneeAcademiqueField.getText().trim(); }
    public String getSemestre()          { return (String) semestreCombo.getSelectedItem(); }
    public String getEtablissementDest() { return etablissementDestField.getText().trim(); }
    public String getMotif()             { return motifArea.getText().trim(); }
}
