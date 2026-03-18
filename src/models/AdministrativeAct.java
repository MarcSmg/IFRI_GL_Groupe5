package models;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdministrativeAct {
    private int id;
    private String type;
    private String content;
    private int signatoryID;
    private boolean isSigned = false;
    private boolean isArchived = false;
    private String actUrl;

    public AdministrativeAct() {}

    public AdministrativeAct(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getType() {
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

    public void setType(String type) {
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
