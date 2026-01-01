package com.example.gestionbpedagogique.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.gestionbpedagogique.database.entities.CahierCharges;

import java.util.List;

@Dao
public interface CahierChargesDao {
    @Query("SELECT * FROM cahier_charges")
    List<CahierCharges> getAllCahierCharges();
    
    @Query("SELECT * FROM cahier_charges WHERE id = :id")
    CahierCharges getCahierChargesById(long id);
    
    @Query("SELECT * FROM cahier_charges WHERE auteurId = :auteurId")
    List<CahierCharges> getCahierChargesByAuteur(long auteurId);
    
    @Query("SELECT * FROM cahier_charges WHERE statut = :statut")
    List<CahierCharges> getCahierChargesByStatut(String statut);
    
    @Query("SELECT * FROM cahier_charges WHERE type = :type")
    List<CahierCharges> getCahierChargesByType(String type);
    
    @Insert
    long insertCahierCharges(CahierCharges cahierCharges);
    
    @Insert
    void insertCahierCharges(CahierCharges... cahierCharges);
    
    @Update
    void updateCahierCharges(CahierCharges cahierCharges);
    
    @Delete
    void deleteCahierCharges(CahierCharges cahierCharges);
    
    @Query("DELETE FROM cahier_charges")
    void deleteAllCahierCharges();
}