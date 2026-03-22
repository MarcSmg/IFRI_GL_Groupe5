/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

/**
 *
 * @author Héloïse
 */
public class StringUtilities {
    public static String formaterNumero(String prefix, int nId){
        int year = java.time.Year.now().getValue();
        String format = String.format("%s%d-%04d", prefix, year, nId);
        return format;
        
    }
    
}
