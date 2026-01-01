package com.example.gestionbpedagogique.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.gestionbpedagogique.database.entities.Reunion;

import java.util.List;

@Dao
public interface ReunionDao {
    @Query("SELECT * FROM reunions")
    List<Reunion> getAllReunions();
    
    @Query("SELECT * FROM reunions WHERE id = :id")
    Reunion getReunionById(long id);
    
    @Query("SELECT * FROM reunions WHERE organisateurId = :organisateurId")
    List<Reunion> getReunionsByOrganisateur(long organisateurId);
    
    @Query("SELECT * FROM reunions WHERE statut = :statut")
    List<Reunion> getReunionsByStatut(String statut);
    
    @Query("SELECT * FROM reunions WHERE dateHeure >= :dateStart AND dateHeure <= :dateEnd")
    List<Reunion> getReunionsByDateRange(long dateStart, long dateEnd);
    
    @Insert
    long insertReunion(Reunion reunion);
    
    @Insert
    void insertReunions(Reunion... reunions);
    
    @Update
    void updateReunion(Reunion reunion);
    
    @Delete
    void deleteReunion(Reunion reunion);
    
    @Query("DELETE FROM reunions")
    void deleteAllReunions();
}