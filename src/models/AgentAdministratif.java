/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import models.enums.AgentFunction;
import models.enums.Role;

/**
 *
 * @author Héloïse
 */
public class AgentAdministratif extends User {
    private AgentFunction function;
   private byte[] signature;
   private byte[] cachet ;
    public AgentAdministratif(String nom, String prenom, String email,AgentFunction func,  String motDePasseTemporaire){
        super(nom, prenom, email, Role.AGENT_ADMINISTRATIF, motDePasseTemporaire, true);
        this.function = func;
        
    }
    
    public AgentFunction getFunction() {
    return function;
    }

    public void setFunction(AgentFunction func) {
        this.function = func;
    }
    
}
