package com.example.gestionbpedagogique.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "cahier_charges",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "auteurId"
        ),
        @ForeignKey(
            entity = Formation.class,
            parentColumns = "id",
            childColumns = "formationId"
        )
    },
    indices = {@Index("auteurId"), @Index("formationId")}
)
public class CahierCharges {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String titre;
    public String type; // "FORMATION_INITIALE", "FORMATION_CONTINUE"
    public String filePath; // Path to the document file
    public long auteurId; // ID of the professeur assistant who created it
    public Long formationId; // Can be null if not yet linked to a formation
    public String statut; // "BROUILLON", "ENVOYE", "APPROUVE", "REFUSE"
    public long dateCreation;
    public Long dateValidation; // Can be null
    
    public CahierCharges() {}
    
    public CahierCharges(String titre, String type, long auteurId) {
        this.titre = titre;
        this.type = type;
        this.auteurId = auteurId;
        this.statut = "BROUILLON";
        this.dateCreation = System.currentTimeMillis();
    }
}