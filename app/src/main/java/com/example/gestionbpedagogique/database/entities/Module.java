package com.example.gestionbpedagogique.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "modules",
    foreignKeys = {
        @ForeignKey(
            entity = Formation.class,
            parentColumns = "id",
            childColumns = "formationId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "professeurId"
        )
    },
    indices = {@Index("formationId"), @Index("professeurId")}
)
public class Module {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String code;
    public String nom;
    public int volumeHoraire;
    public long formationId;
    public Long professeurId; // Can be null
    
    public Module() {}
    
    public Module(String code, String nom, int volumeHoraire, long formationId) {
        this.code = code;
        this.nom = nom;
        this.volumeHoraire = volumeHoraire;
        this.formationId = formationId;
    }
}