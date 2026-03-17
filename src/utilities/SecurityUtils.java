/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Héloïse
 */
public class SecurityUtils {
    public static String generateTempPassword(){
        String prefixe = "IFRI-";
        String randomLetters = java.util.UUID.randomUUID().toString().substring(0, 8);
        String mdpTemp = prefixe + randomLetters;
        return mdpTemp;
    }
    
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}
