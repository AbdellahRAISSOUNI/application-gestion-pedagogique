package com.example.gestionbpedagogique.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.gestionbpedagogique.database.entities.EmploiTemps;

import java.util.List;

@Dao
public interface EmploiTempsDao {
    @Query("SELECT * FROM emploi_temps")
    List<EmploiTemps> getAllEmploiTemps();
    
    @Query("SELECT * FROM emploi_temps WHERE id = :id")
    EmploiTemps getEmploiTempsById(long id);
    
    @Query("SELECT * FROM emploi_temps WHERE professeurId = :professeurId")
    List<EmploiTemps> getEmploiTempsByProfesseur(long professeurId);
    
    @Query("SELECT * FROM emploi_temps WHERE moduleId = :moduleId")
    List<EmploiTemps> getEmploiTempsByModule(long moduleId);
    
    @Query("SELECT * FROM emploi_temps WHERE jourSemaine = :jourSemaine")
    List<EmploiTemps> getEmploiTempsByJour(String jourSemaine);
    
    @Query("SELECT * FROM emploi_temps WHERE professeurId = :professeurId AND jourSemaine = :jourSemaine")
    List<EmploiTemps> getEmploiTempsByProfesseurAndJour(long professeurId, String jourSemaine);
    
    @Insert
    long insertEmploiTemps(EmploiTemps emploiTemps);
    
    @Insert
    void insertEmploiTemps(EmploiTemps... emploiTemps);
    
    @Update
    void updateEmploiTemps(EmploiTemps emploiTemps);
    
    @Delete
    void deleteEmploiTemps(EmploiTemps emploiTemps);
    
    @Query("DELETE FROM emploi_temps WHERE professeurId = :professeurId")
    void deleteEmploiTempsByProfesseur(long professeurId);
    
    @Query("DELETE FROM emploi_temps")
    void deleteAllEmploiTemps();
}