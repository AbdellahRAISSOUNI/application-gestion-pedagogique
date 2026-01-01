package com.example.gestionbpedagogique.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(
    tableName = "reunions",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "organisateurId"
        )
    }
)
public class Reunion {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String titre;
    public long dateHeure; // Timestamp
    public long organisateurId; // ID of the admin who organized it
    public String ordreDuJour;
    public String statut; // "PLANIFIEE", "EN_COURS", "TERMINEE"
    
    public Reunion() {}
    
    public Reunion(String titre, long dateHeure, long organisateurId, String ordreDuJour) {
        this.titre = titre;
        this.dateHeure = dateHeure;
        this.organisateurId = organisateurId;
        this.ordreDuJour = ordreDuJour;
        this.statut = "PLANIFIEE";
    }
}