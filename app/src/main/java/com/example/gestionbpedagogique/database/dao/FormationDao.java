package com.example.gestionbpedagogique.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.gestionbpedagogique.database.entities.Formation;

import java.util.List;

@Dao
public interface FormationDao {
    @Query("SELECT * FROM formations")
    List<Formation> getAllFormations();
    
    @Query("SELECT * FROM formations WHERE id = :id")
    Formation getFormationById(long id);
    
    @Query("SELECT * FROM formations WHERE type = :type")
    List<Formation> getFormationsByType(String type);
    
    @Query("SELECT * FROM formations WHERE type = :type AND cycle = :cycle")
    List<Formation> getFormationsByTypeAndCycle(String type, String cycle);
    
    @Query("SELECT * FROM formations WHERE status = :status")
    List<Formation> getFormationsByStatus(String status);
    
    @Insert
    long insertFormation(Formation formation);
    
    @Insert
    void insertFormations(Formation... formations);
    
    @Update
    void updateFormation(Formation formation);
    
    @Delete
    void deleteFormation(Formation formation);
    
    @Query("DELETE FROM formations")
    void deleteAllFormations();
}