package com.example.myisidorapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "table_livres") // Indique à Room que cette classe devient une table SQL
public class Livre implements Serializable {

    @PrimaryKey(autoGenerate = true) // L'ID sera auto-incrémenté automatiquement (1, 2, 3...)
    private int id;

    private String titre;
    private String auteur;
    private String isbn;
    private boolean disponible;

    // Constructeur complet
    public Livre(int id, String titre, String auteur, String isbn, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.disponible = disponible;
    }

    // Getters et Setters (Indispensables pour que Room puisse lire et écrire)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}