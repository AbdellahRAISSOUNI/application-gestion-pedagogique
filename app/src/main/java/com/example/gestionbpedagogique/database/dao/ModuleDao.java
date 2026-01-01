package com.example.gestionbpedagogique.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.gestionbpedagogique.database.entities.Module;

import java.util.List;

@Dao
public interface ModuleDao {
    @Query("SELECT * FROM modules")
    List<Module> getAllModules();
    
    @Query("SELECT * FROM modules WHERE id = :id")
    Module getModuleById(long id);
    
    @Query("SELECT * FROM modules WHERE formationId = :formationId")
    List<Module> getModulesByFormation(long formationId);
    
    @Query("SELECT * FROM modules WHERE code LIKE '%' || :search || '%' OR nom LIKE '%' || :search || '%'")
    List<Module> searchModules(String search);
    
    @Query("SELECT * FROM modules WHERE professeurId = :professeurId")
    List<Module> getModulesByProfesseur(long professeurId);
    
    @Insert
    long insertModule(Module module);
    
    @Insert
    void insertModules(Module... modules);
    
    @Update
    void updateModule(Module module);
    
    @Delete
    void deleteModule(Module module);
    
    @Query("DELETE FROM modules")
    void deleteAllModules();
}