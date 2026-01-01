package com.example.gestionbpedagogique.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "formations")
public class Formation {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String type; // "INITIALE" or "CONTINUE"
    public String cycle; // For INITIALE: "PREPARATOIRE", "INGENIEUR", "MASTER"
                       // For CONTINUE: "DCA", "DCESS"
    public String title;
    public String description;
    public String status; // "EN_ATTENTE", "APPROUVEE", "REFUSEE"
    public long createdDate;
    public long validatedDate;
    public long createdByUserId; // ID of the user who created it
    
    public Formation() {}
    
    public Formation(String type, String cycle, String title, String description, long createdByUserId) {
        this.type = type;
        this.cycle = cycle;
        this.title = title;
        this.description = description;
        this.status = "EN_ATTENTE";
        this.createdDate = System.currentTimeMillis();
        this.createdByUserId = createdByUserId;
    }
}