/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import models.enums.Role;

import models.enums.Role;

import java.time.LocalDate;

/**
 *
 * @author Héloïse
 */
public class Usager extends User {
    private String  matricule;
    private String fieldOfStudy;
    private LocalDate birthDate;
    private String birthPlace;
    private String gender;
    private String nationalite;
    public Usager(String matricule, String nom, String prenom, String email, Role role, String mdp){
       
        super(nom, prenom, email,Role.USAGER, mdp, false);
        this.matricule = matricule;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getGender() {
        return gender;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
}
