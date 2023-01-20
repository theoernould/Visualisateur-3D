package UtilsClasses;

import java.time.LocalDate;

public class InfosElement {
    private String nom;
    private String description;
    private String auteur;
    private LocalDate date;
    private int nbFaces;
    private int nbPoints;

    public InfosElement(String nom, String description, String auteur, LocalDate date, int nbFaces, int nbPoints) {
        this.nom = nom;
        this.description = description;
        this.auteur = auteur;
        this.date = date;
        this.nbFaces = nbFaces;
        this.nbPoints = nbPoints;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getAuteur() {
        return auteur;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getNbFaces() {
        return nbFaces;
    }

    public int getNbPoints() {
        return nbPoints;
    }

}
