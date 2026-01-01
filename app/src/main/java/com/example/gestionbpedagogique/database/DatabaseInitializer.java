package com.example.gestionbpedagogique.database;

import android.content.Context;

import com.example.gestionbpedagogique.database.entities.User;
import com.example.gestionbpedagogique.database.entities.Formation;
import com.example.gestionbpedagogique.database.entities.Module;
import com.example.gestionbpedagogique.database.entities.CahierCharges;
import com.example.gestionbpedagogique.database.entities.Reunion;
import com.example.gestionbpedagogique.database.entities.ReunionParticipant;
import com.example.gestionbpedagogique.database.entities.EmploiTemps;

import java.util.Calendar;

public class DatabaseInitializer {
    
    public static void initializeDatabase(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        
        // Check if database is already initialized
        if (db.userDao().getAllUsers().size() > 0) {
            return; // Database already initialized
        }
        
        // Initialize with sample data
        initializeUsers(db);
        initializeFormations(db);
        initializeModules(db);
    }
    
    private static void initializeUsers(AppDatabase db) {
        // Admin user (Directeur Adjoint)
        User admin = new User(
            "admin",
            "admin123", // In production, this should be hashed
            "ADMIN",
            "Directeur Adjoint",
            "admin@ensa.ma",
            "+212 6XX XXX XXX"
        );
        long adminId = db.userDao().insertUser(admin);
        
        // Professeur Assistant
        User profAssistant1 = new User(
            "prof.assistant1",
            "prof123",
            "PROFESSEUR_ASSISTANT",
            "Professeur Assistant 1",
            "prof1@ensa.ma",
            "+212 6XX XXX XXX"
        );
        long profAssistantId1 = db.userDao().insertUser(profAssistant1);
        
        // Professeur Assistant 2
        User profAssistant2 = new User(
            "prof.assistant2",
            "prof123",
            "PROFESSEUR_ASSISTANT",
            "Professeur Assistant 2",
            "prof2@ensa.ma",
            "+212 6XX XXX XXX"
        );
        long profAssistantId2 = db.userDao().insertUser(profAssistant2);
        
        // Professeur Vacataire
        User profVacataire = new User(
            "prof.vacataire",
            "prof123",
            "PROFESSEUR_VACATAIRE",
            "Professeur Vacataire",
            "vacataire@ensa.ma",
            "+212 6XX XXX XXX"
        );
        db.userDao().insertUser(profVacataire);
    }
    
    private static void initializeFormations(AppDatabase db) {
        // Get admin user ID (assuming first user is admin)
        long adminId = db.userDao().getUserByUsername("admin").id;
        
        // Formation Initiale - Cycle Préparatoire
        Formation prepFormation = new Formation(
            "INITIALE",
            "PREPARATOIRE",
            "Cycle Préparatoire",
            "Formation préparatoire pour les étudiants",
            adminId
        );
        long prepId = db.formationDao().insertFormation(prepFormation);
        
        // Formation Initiale - Cycle Ingénieur
        Formation ingFormation = new Formation(
            "INITIALE",
            "INGENIEUR",
            "Cycle Ingénieur",
            "Formation d'ingénieur",
            adminId
        );
        long ingId = db.formationDao().insertFormation(ingFormation);
        
        // Formation Initiale - Cycle Master
        Formation masterFormation = new Formation(
            "INITIALE",
            "MASTER",
            "Cycle Master",
            "Formation master",
            adminId
        );
        long masterId = db.formationDao().insertFormation(masterFormation);
        
        // Formation Continue - DCA
        Formation dcaFormation = new Formation(
            "CONTINUE",
            "DCA",
            "Diplôme de Cycle d'Approfondissement",
            "Formation continue DCA",
            adminId
        );
        db.formationDao().insertFormation(dcaFormation);
        
        // Formation Continue - DCESS
        Formation dcessFormation = new Formation(
            "CONTINUE",
            "DCESS",
            "Diplôme de Cycle d'Études Supérieures Spécialisées",
            "Formation continue DCESS",
            adminId
        );
        db.formationDao().insertFormation(dcessFormation);
    }
    
    private static void initializeModules(AppDatabase db) {
        // Get formations
        Formation prepFormation = db.formationDao().getFormationsByTypeAndCycle("INITIALE", "PREPARATOIRE").get(0);
        Formation ingFormation = db.formationDao().getFormationsByTypeAndCycle("INITIALE", "INGENIEUR").get(0);
        
        // Sample modules for Cycle Préparatoire
        Module module1 = new Module("MATH101", "Mathématiques", 60, prepFormation.id);
        Module module2 = new Module("PHYS101", "Physique", 60, prepFormation.id);
        Module module3 = new Module("INFO101", "Informatique", 40, prepFormation.id);
        
        db.moduleDao().insertModules(module1, module2, module3);
        
        // Sample modules for Cycle Ingénieur
        Module module4 = new Module("ALGO201", "Algorithmique", 50, ingFormation.id);
        Module module5 = new Module("BD201", "Bases de Données", 40, ingFormation.id);
        Module module6 = new Module("RESEAU201", "Réseaux", 40, ingFormation.id);
        
        db.moduleDao().insertModules(module4, module5, module6);
    }
}