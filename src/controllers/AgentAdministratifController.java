/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import dao.*;
import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import org.apache.poi.util.Units;

/**
 *
 * @author Héloïse
 */
public class AgentAdministratifController {
    private AdministrativeActDAO acteDAO;
    public boolean deleteActe(int id){
        try{
            acteDAO.delete(id);
            return true;
        }catch(Exception e){
            System.out.println("Erreur lors de la suppression du document");
            return false;
        }        
    }
    
    public boolean signAct(XWPFDocument document, byte[] signatureBytes){
        try{
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            InputStream is = new ByteArrayInputStream(signatureBytes);

            // Ajout de l'image (Format PNG ou JPEG)
            // Les dimensions sont en "EMU" (English Metric Units). 
            // Utilise Units.toEMU(pixel) pour convertir facilement.
            run.addPicture(is, 
            XWPFDocument.PICTURE_TYPE_PNG, 
            "signature.png", 
            Units.toEMU(100),
            Units.toEMU(50));// Largeur en pixels
            return true;
        }catch(Exception e){
            System.err.println("Erreur lors de la signature de l'acte");
            return false;
        }
    }
}
