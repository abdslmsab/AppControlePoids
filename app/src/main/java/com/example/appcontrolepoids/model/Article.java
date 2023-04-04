package com.example.appcontrolepoids.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "CP_Articles")
public class Article {
    //Clé primaire : Id_Article
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id_Article")
    private int id;

    //Code de l'article ne pouvant pas être nul et contenant 13 caractères maximum
    @ColumnInfo(name = "Code_Article")
    private String code;

    //Nom de l'article ne pouvant pas être nul et contenant 69 caractères maximmum
    @ColumnInfo(name = "Nom_Article")
    private String nom;

    //EAN de l'article ne pouvant pas être nul et contenant 13 caractères maximum
    @ColumnInfo(name = "EAN_Article")
    private String ean;

    //Poids net de l'article ne pouvant pas être nul et contenant 3 chiffres maximum
    @ColumnInfo(name = "Poids_Net_Article")
    private int poidsNet;

    //Poids brut de l'article ne pouvant pas être nul et contenant 3 chiffres maximum
    @ColumnInfo(name = "Poids_Brut_Article")
    private int poidsBrut;

    //Rendement de l'article ne pouvant pas être nul et contenant 3 chiffres maximum
    @ColumnInfo(name = "Rendement_Article")
    private int rendement;

    //Matricule ne pouvant pas être nul et contenant 4 chiffres
    @ColumnInfo(name = "Code_Operateur")
    private String codeOperateur;

    // Constructeur par défaut
    public Article() {
    }

    // Constructeur avec paramètres
    @Ignore
    public Article(String code, String nom, String ean, int poidsNet, int poidsBrut, int rendement, String codeOperateur) {
        this.code = code;
        this.nom = nom;
        this.ean = ean;
        this.poidsNet = poidsNet;
        this.poidsBrut = poidsBrut;
        this.rendement = rendement;
        this.codeOperateur = codeOperateur;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public int getPoidsNet() {
        return poidsNet;
    }

    public void setPoidsNet(int poidsNet) {
        this.poidsNet = poidsNet;
    }

    public int getPoidsBrut() {
        return poidsBrut;
    }

    public void setPoidsBrut(int poidsBrut) {
        this.poidsBrut = poidsBrut;
    }

    public int getRendement() {
        return rendement;
    }

    public void setRendement(int rendement) {
        this.rendement = rendement;
    }

    public String getCodeOperateur() {
        return codeOperateur;
    }

    public void setCodeOperateur(String codeOperateur) {
        this.codeOperateur = codeOperateur;
    }
}
