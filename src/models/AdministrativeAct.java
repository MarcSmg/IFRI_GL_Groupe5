package models;
import models.enums.*;
import org.apache.poi.xwpf.usermodel.*;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdministrativeAct {
    private int id;
    private AdministrativeActType type;
    private String content;
    private String signatory;
    private boolean signed = false;
    private boolean archived = false;
    private String actUrl;

    public int getId() {
        return id;
    }

    public AdministrativeActType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getSignatory() {
        return signatory;
    }

    public boolean getArchived() {
        return archived;
    }

    public boolean getSigned() {
        return signed;
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

    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public void archive() {
        setArchived(true);
    }

    public void signAct() {
        setSigned(true);
    }

    public boolean generateDocx() {
        try {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph docxContent = document.createParagraph();
            XWPFRun contentRun = docxContent.createRun();
            contentRun.setFontSize(14);
            contentRun.setText(content);

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
}
