package com.example.gestionbpedagogique.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "reunion_participants",
    foreignKeys = {
        @ForeignKey(
            entity = Reunion.class,
            parentColumns = "id",
            childColumns = "reunionId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "userId"
        )
    },
    indices = {@Index("reunionId"), @Index("userId")}
)
public class ReunionParticipant {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public long reunionId;
    public long userId;
    public boolean present; // Whether the participant attended
    
    public ReunionParticipant() {}
    
    public ReunionParticipant(long reunionId, long userId) {
        this.reunionId = reunionId;
        this.userId = userId;
        this.present = false;
    }
}