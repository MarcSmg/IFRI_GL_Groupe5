package models;
import models.enums.AdministrativeActType;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdministrativeAct {
    private int id;
    private AdministrativeActType type;
    private String content;
    private int signatoryID;
    private boolean isSigned = false;
    private boolean isArchived = false;
    private String actUrl;

    public AdministrativeAct() {}

    public AdministrativeAct(AdministrativeActType type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public AdministrativeActType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getSignatoryID() {
        return signatoryID;
    }

    public boolean getIsArchived() {
        return isArchived;
    }

    public boolean getIsSigned() {
        return isSigned;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(AdministrativeActType type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSignatoryID(int signatoryID) {
        this.signatoryID = signatoryID;
    }

    public void setIsSigned(boolean signed) {
        this.isSigned = signed;
    }

    public void setIsArchived(boolean archived) {
        this.isArchived = archived;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public void archive() {
        setIsArchived(true);
    }

    public void signAct() {
        setIsSigned(true);
    }


    public boolean generateDocx() {
        try {
            XWPFDocument document = new XWPFDocument();
            addHeaderLogos(document, "assets/logo-ifri.png", "assets/logo-uac.png");
            generateFormattedDocument(document);

            LocalDate currentDate = LocalDate.now();
            String monthValue = currentDate.format(DateTimeFormatter.ofPattern("MM"));

            String folder = "generated-acts/" + currentDate.getYear() + "/" + monthValue + "/";
            File dir = new File(folder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = "doc_" + this.getId() + "_" + System.currentTimeMillis() + ".docx";

            actUrl = folder + fileName;

            FileOutputStream out = new FileOutputStream(actUrl);
            document.write(out);
            out.close();
            document.close();

            System.out.println("Document generated");

            return true;

        } catch (IOException e) {
            System.out.println("Document NOT generated !");
            e.printStackTrace();
        }
        return false;
    }

    private void generateFormattedDocument(XWPFDocument document) {
        switch (type) {

            case ATTESTATION_INSCRIPTION:
                generateAttestationInscriptionDoc(document);
                break;

            case RELEVE_NOTES:
                generateReleveNotesDoc(document);
                break;

            case CERTIFICAT_SCOLARITE:
                generateCertificatScolariteDoc(document);
                break;

            default:
                addParagraph(document, "Type non supporté");
        }
    }

    private void generateCertificatScolariteDoc(XWPFDocument document) {
    }

    private void generateAttestationInscriptionDoc(XWPFDocument doc) {

        addTitle(doc, type.getLabel());

        addParagraph(doc, "L'étudiant est inscrit.");

        addParagraph(doc, "Fait le : " + LocalDate.now());
    }

    private void generateReleveNotesDoc(XWPFDocument doc) {

        addTitle(doc, type.getLabel());

        addParagraph(doc, "Nom : John Doe");
        addParagraph(doc, "Année : 2025");

        addParagraph(doc, "Notes :");

        // later → table
    }

    private void addTitle(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setBold(true);
        run.setFontSize(16);
        run.setText(text);
    }

    private void addParagraph(XWPFDocument doc, String text) {
        XWPFParagraph p = doc.createParagraph();
        XWPFRun run = p.createRun();
        run.setFontSize(12);
        run.setText(text);
    }

    private void addHeaderLogos(XWPFDocument doc, String leftPath, String rightPath) {

        XWPFTable table = doc.createTable(1, 2);

        // remove borders
        table.removeBorders();

        try {
            // LEFT
            XWPFParagraph leftP = table.getRow(0).getCell(0).getParagraphs().get(0);
            leftP.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun leftRun = leftP.createRun();

            try (FileInputStream is = new FileInputStream(leftPath)) {
                leftRun.addPicture(is, Document.PICTURE_TYPE_PNG,
                        leftPath, Units.toEMU(40), Units.toEMU(40));
            }

            // RIGHT
            XWPFParagraph rightP = table.getRow(0).getCell(1).getParagraphs().get(0);
            rightP.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun rightRun = rightP.createRun();

            try (FileInputStream is = new FileInputStream(rightPath)) {
                rightRun.addPicture(is, Document.PICTURE_TYPE_PNG,
                        rightPath, Units.toEMU(40), Units.toEMU(40));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        XWPFParagraph heading1 = doc.createParagraph();
        XWPFRun run1 = heading1.createRun();
        run1.setFontSize(14);
        run1.setText("Université d'Abomey-Calavi");
        heading1.setAlignment(ParagraphAlignment.CENTER);

        heading1.setSpacingBefore(200);
        heading1.setSpacingAfter(200);

        XWPFParagraph heading2 = doc.createParagraph();
        XWPFRun run2 = heading2.createRun();
        run2.setFontSize(14);
        run2.setText("INSTITUT DE FORMATION ET DE RECHERCHE EN INFORMATIQUE");
        heading2.setAlignment(ParagraphAlignment.CENTER);

        heading2.setSpacingAfter(200);
    }
}
