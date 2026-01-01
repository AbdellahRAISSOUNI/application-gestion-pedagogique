package com.example.gestionbpedagogique.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "emploi_temps",
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "professeurId"
        ),
        @ForeignKey(
            entity = Module.class,
            parentColumns = "id",
            childColumns = "moduleId"
        )
    },
    indices = {@Index("professeurId"), @Index("moduleId")}
)
public class EmploiTemps {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public long professeurId;
    public long moduleId;
    public String jourSemaine; // "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI"
    public String heureDebut; // Format: "HH:mm"
    public String heureFin; // Format: "HH:mm"
    public String salle;
    public String typeCours; // "CM", "TD", "TP"
    
    public EmploiTemps() {}
    
    public EmploiTemps(long professeurId, long moduleId, String jourSemaine, 
                      String heureDebut, String heureFin, String salle, String typeCours) {
        this.professeurId = professeurId;
        this.moduleId = moduleId;
        this.jourSemaine = jourSemaine;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.salle = salle;
        this.typeCours = typeCours;
    }
}