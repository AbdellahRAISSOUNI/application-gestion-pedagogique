package com.example.gestionbpedagogique.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.gestionbpedagogique.database.entities.ReunionParticipant;

import java.util.List;

@Dao
public interface ReunionParticipantDao {
    @Query("SELECT * FROM reunion_participants")
    List<ReunionParticipant> getAllParticipants();
    
    @Query("SELECT * FROM reunion_participants WHERE reunionId = :reunionId")
    List<ReunionParticipant> getParticipantsByReunion(long reunionId);
    
    @Query("SELECT * FROM reunion_participants WHERE userId = :userId")
    List<ReunionParticipant> getReunionsByUser(long userId);
    
    @Insert
    long insertParticipant(ReunionParticipant participant);
    
    @Insert
    void insertParticipants(ReunionParticipant... participants);
    
    @Update
    void updateParticipant(ReunionParticipant participant);
    
    @Delete
    void deleteParticipant(ReunionParticipant participant);
    
    @Query("DELETE FROM reunion_participants WHERE reunionId = :reunionId")
    void deleteParticipantsByReunion(long reunionId);
    
    @Query("DELETE FROM reunion_participants")
    void deleteAllParticipants();
}