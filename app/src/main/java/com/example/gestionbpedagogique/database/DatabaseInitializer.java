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
import java.util.List;

public class DatabaseInitializer {
    
    public static void initializeDatabase(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        
        // Check if database is already initialized
        boolean usersExist = db.userDao().getAllUsers().size() > 0;
        boolean emploiTempsExist = db.emploiTempsDao().getAllEmploiTemps().size() > 0;
        boolean reunionsExist = db.reunionDao().getAllReunions().size() > 0;
        boolean cahiersExist = db.cahierChargesDao().getAllCahierCharges().size() > 0;
        
        if (!usersExist) {
            // Initialize with sample data
            initializeUsers(db);
            initializeFormations(db);
            initializeModules(db);
        }
        
        // Always initialize emploi temps if they don't exist
        if (!emploiTempsExist) {
            initializeEmploiTemps(db);
        }
        
        // Initialize reunions if they don't exist
        if (!reunionsExist) {
            initializeReunions(db);
        }
        
        // Initialize cahiers de charges if they don't exist
        if (!cahiersExist) {
            initializeCahiersCharges(db);
        }
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
            "Cycle Préparatoire ENSA Tétouan",
            "Formation préparatoire intégrée pour les étudiants de première année",
            adminId
        );
        long prepId = db.formationDao().insertFormation(prepFormation);
        
        // Formation Initiale - Cycle Ingénieur - Génie Informatique
        Formation ingInfoFormation = new Formation(
            "INITIALE",
            "INGENIEUR",
            "Génie Informatique",
            "Formation d'ingénieur en informatique et systèmes d'information",
            adminId
        );
        long ingInfoId = db.formationDao().insertFormation(ingInfoFormation);
        
        // Formation Initiale - Cycle Ingénieur - Génie Industriel
        Formation ingIndFormation = new Formation(
            "INITIALE",
            "INGENIEUR",
            "Génie Industriel",
            "Formation d'ingénieur en génie industriel et logistique",
            adminId
        );
        long ingIndId = db.formationDao().insertFormation(ingIndFormation);
        
        // Formation Initiale - Cycle Ingénieur - Génie Civil
        Formation ingCivFormation = new Formation(
            "INITIALE",
            "INGENIEUR",
            "Génie Civil",
            "Formation d'ingénieur en génie civil et construction",
            adminId
        );
        long ingCivId = db.formationDao().insertFormation(ingCivFormation);
        
        // Formation Initiale - Cycle Master
        Formation masterFormation = new Formation(
            "INITIALE",
            "MASTER",
            "Master en Ingénierie",
            "Formation master spécialisée en ingénierie",
            adminId
        );
        long masterId = db.formationDao().insertFormation(masterFormation);
        
        // Formation Continue - DCA
        Formation dcaFormation = new Formation(
            "CONTINUE",
            "DCA",
            "Diplôme de Cycle d'Approfondissement",
            "Formation continue DCA pour professionnels",
            adminId
        );
        db.formationDao().insertFormation(dcaFormation);
        
        // Formation Continue - DCESS
        Formation dcessFormation = new Formation(
            "CONTINUE",
            "DCESS",
            "Diplôme de Cycle d'Études Supérieures Spécialisées",
            "Formation continue DCESS en management et ingénierie",
            adminId
        );
        db.formationDao().insertFormation(dcessFormation);
        
        // Formation Continue - Spécialisation
        Formation specFormation = new Formation(
            "CONTINUE",
            "DCA",
            "Spécialisation en Intelligence Artificielle",
            "Formation continue en IA et machine learning",
            adminId
        );
        db.formationDao().insertFormation(specFormation);
    }
    
    private static void initializeModules(AppDatabase db) {
        // Get formations
        List<Formation> formations = db.formationDao().getAllFormations();
        Formation prepFormation = null;
        Formation ingInfoFormation = null;
        Formation ingIndFormation = null;
        Formation ingCivFormation = null;
        Formation masterFormation = null;
        
        for (Formation f : formations) {
            if ("Cycle Préparatoire ENSA Tétouan".equals(f.title)) prepFormation = f;
            if ("Génie Informatique".equals(f.title)) ingInfoFormation = f;
            if ("Génie Industriel".equals(f.title)) ingIndFormation = f;
            if ("Génie Civil".equals(f.title)) ingCivFormation = f;
            if ("Master en Ingénierie".equals(f.title)) masterFormation = f;
        }
        
        // Modules for Cycle Préparatoire
        if (prepFormation != null) {
            db.moduleDao().insertModules(
                new Module("MATH101", "Mathématiques Générales", 60, prepFormation.id),
                new Module("PHYS101", "Physique Fondamentale", 60, prepFormation.id),
                new Module("INFO101", "Initiation à l'Informatique", 40, prepFormation.id),
                new Module("CHIM101", "Chimie Générale", 40, prepFormation.id),
                new Module("ANG101", "Anglais Technique", 30, prepFormation.id)
            );
        }
        
        // Modules for Génie Informatique
        if (ingInfoFormation != null) {
            db.moduleDao().insertModules(
                new Module("ALGO201", "Algorithmique et Structures de Données", 50, ingInfoFormation.id),
                new Module("BD201", "Bases de Données", 40, ingInfoFormation.id),
                new Module("RESEAU201", "Réseaux et Télécommunications", 40, ingInfoFormation.id),
                new Module("POO201", "Programmation Orientée Objet", 50, ingInfoFormation.id),
                new Module("WEB201", "Développement Web", 40, ingInfoFormation.id),
                new Module("IA201", "Intelligence Artificielle", 40, ingInfoFormation.id),
                new Module("SEC201", "Sécurité Informatique", 30, ingInfoFormation.id),
                new Module("ARCH201", "Architecture des Ordinateurs", 40, ingInfoFormation.id)
            );
        }
        
        // Modules for Génie Industriel
        if (ingIndFormation != null) {
            db.moduleDao().insertModules(
                new Module("GEST301", "Gestion de Production", 50, ingIndFormation.id),
                new Module("LOG301", "Logistique et Supply Chain", 40, ingIndFormation.id),
                new Module("QUAL301", "Qualité et Management", 40, ingIndFormation.id),
                new Module("PROJ301", "Gestion de Projets", 40, ingIndFormation.id),
                new Module("AUTO301", "Automatisation Industrielle", 50, ingIndFormation.id)
            );
        }
        
        // Modules for Génie Civil
        if (ingCivFormation != null) {
            db.moduleDao().insertModules(
                new Module("STR401", "Structures et Résistance des Matériaux", 60, ingCivFormation.id),
                new Module("BET401", "Béton Armé", 50, ingCivFormation.id),
                new Module("ROUTE401", "Routes et Ouvrages d'Art", 40, ingCivFormation.id),
                new Module("HYD401", "Hydraulique et Assainissement", 40, ingCivFormation.id),
                new Module("GEOT401", "Géotechnique", 40, ingCivFormation.id)
            );
        }
        
        // Modules for Master
        if (masterFormation != null) {
            db.moduleDao().insertModules(
                new Module("RECH501", "Méthodologie de Recherche", 30, masterFormation.id),
                new Module("INNOV501", "Innovation et Entrepreneuriat", 40, masterFormation.id)
            );
        }
    }
    
    private static void initializeEmploiTemps(AppDatabase db) {
        // Get users
        User profAssistant1 = db.userDao().getUserByUsername("prof.assistant1");
        User profAssistant2 = db.userDao().getUserByUsername("prof.assistant2");
        User profVacataire = db.userDao().getUserByUsername("prof.vacataire");
        
        // Get modules by code
        List<Module> allModules = db.moduleDao().getAllModules();
        Module algoModule = null;
        Module bdModule = null;
        Module mathModule = null;
        Module pooModule = null;
        Module reseauModule = null;
        Module webModule = null;
        Module physModule = null;
        Module infoModule = null;
        Module gestModule = null;
        Module qualModule = null;
        
        for (Module m : allModules) {
            if ("ALGO201".equals(m.code)) algoModule = m;
            if ("BD201".equals(m.code)) bdModule = m;
            if ("MATH101".equals(m.code)) mathModule = m;
            if ("POO201".equals(m.code)) pooModule = m;
            if ("RESEAU201".equals(m.code)) reseauModule = m;
            if ("WEB201".equals(m.code)) webModule = m;
            if ("PHYS101".equals(m.code)) physModule = m;
            if ("INFO101".equals(m.code)) infoModule = m;
            if ("GEST301".equals(m.code)) gestModule = m;
            if ("QUAL301".equals(m.code)) qualModule = m;
        }
        
        // Emploi du temps for prof.assistant1 (Génie Informatique)
        if (profAssistant1 != null) {
            if (algoModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profAssistant1.id, algoModule.id, "LUNDI", "08:00", "10:00", "Salle 101", "CM"),
                    new EmploiTemps(profAssistant1.id, algoModule.id, "MERCREDI", "14:00", "16:00", "Salle 102", "TD")
                );
            }
            if (pooModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profAssistant1.id, pooModule.id, "MARDI", "10:00", "12:00", "Salle 103", "CM"),
                    new EmploiTemps(profAssistant1.id, pooModule.id, "JEUDI", "14:00", "16:00", "Lab Info 1", "TP")
                );
            }
            if (webModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profAssistant1.id, webModule.id, "VENDREDI", "08:00", "10:00", "Lab Info 2", "TP")
                );
            }
        }
        
        // Emploi du temps for prof.assistant2
        if (profAssistant2 != null) {
            if (bdModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profAssistant2.id, bdModule.id, "MARDI", "10:00", "12:00", "Salle 201", "CM"),
                    new EmploiTemps(profAssistant2.id, bdModule.id, "JEUDI", "14:00", "16:00", "Salle 202", "TP")
                );
            }
            if (reseauModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profAssistant2.id, reseauModule.id, "LUNDI", "14:00", "16:00", "Salle 203", "CM"),
                    new EmploiTemps(profAssistant2.id, reseauModule.id, "MERCREDI", "10:00", "12:00", "Lab Réseaux", "TP")
                );
            }
            if (gestModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profAssistant2.id, gestModule.id, "VENDREDI", "10:00", "12:00", "Salle 301", "CM")
                );
            }
        }
        
        // Emploi du temps for prof.vacataire
        if (profVacataire != null) {
            if (mathModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profVacataire.id, mathModule.id, "LUNDI", "08:00", "10:00", "Salle 301", "CM"),
                    new EmploiTemps(profVacataire.id, mathModule.id, "MERCREDI", "10:00", "12:00", "Salle 302", "TD")
                );
            }
            if (physModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profVacataire.id, physModule.id, "MARDI", "14:00", "16:00", "Salle 303", "CM")
                );
            }
            if (infoModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profVacataire.id, infoModule.id, "JEUDI", "08:00", "10:00", "Lab Info 3", "TP")
                );
            }
            if (qualModule != null) {
                db.emploiTempsDao().insertEmploiTemps(
                    new EmploiTemps(profVacataire.id, qualModule.id, "VENDREDI", "14:00", "16:00", "Salle 304", "CM")
                );
            }
        }
    }
    
    private static void initializeReunions(AppDatabase db) {
        // Get admin user ID
        User admin = db.userDao().getUserByUsername("admin");
        User profAssistant1 = db.userDao().getUserByUsername("prof.assistant1");
        User profAssistant2 = db.userDao().getUserByUsername("prof.assistant2");
        
        if (admin == null) return;
        
        Calendar cal = Calendar.getInstance();
        
        // Réunion 1: Conseil Pédagogique
        cal.set(2026, Calendar.JANUARY, 15, 10, 0);
        Reunion reunion1 = new Reunion(
            "Conseil Pédagogique - Planification Semestre",
            cal.getTimeInMillis(),
            admin.id,
            "1. Bilan du semestre précédent\n2. Planification des examens\n3. Organisation des stages\n4. Projets pédagogiques"
        );
        long reunion1Id = db.reunionDao().insertReunion(reunion1);
        if (profAssistant1 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion1Id, profAssistant1.id));
        }
        if (profAssistant2 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion1Id, profAssistant2.id));
        }
        
        // Réunion 2: Coordination Génie Informatique
        cal.set(2026, Calendar.JANUARY, 20, 14, 30);
        Reunion reunion2 = new Reunion(
            "Coordination Génie Informatique",
            cal.getTimeInMillis(),
            admin.id,
            "1. Révision des programmes\n2. Équipements laboratoires\n3. Stages étudiants\n4. Projets de fin d'études"
        );
        long reunion2Id = db.reunionDao().insertReunion(reunion2);
        if (profAssistant1 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion2Id, profAssistant1.id));
        }
        
        // Réunion 3: Commission Pédagogique
        cal.set(2026, Calendar.FEBRUARY, 5, 9, 0);
        Reunion reunion3 = new Reunion(
            "Commission Pédagogique - Évaluation",
            cal.getTimeInMillis(),
            admin.id,
            "1. Évaluation des formations\n2. Amélioration continue\n3. Retour étudiants\n4. Actions correctives"
        );
        long reunion3Id = db.reunionDao().insertReunion(reunion3);
        if (profAssistant1 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion3Id, profAssistant1.id));
        }
        if (profAssistant2 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion3Id, profAssistant2.id));
        }
        
        // Réunion 4: Réunion Département
        cal.set(2026, Calendar.FEBRUARY, 12, 15, 0);
        Reunion reunion4 = new Reunion(
            "Réunion Département Génie Industriel",
            cal.getTimeInMillis(),
            admin.id,
            "1. Organisation des cours\n2. Planning des examens\n3. Coordination des modules\n4. Ressources pédagogiques"
        );
        long reunion4Id = db.reunionDao().insertReunion(reunion4);
        if (profAssistant2 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion4Id, profAssistant2.id));
        }
        
        // Réunion 5: Conseil d'École
        cal.set(2026, Calendar.FEBRUARY, 18, 10, 0);
        Reunion reunion5 = new Reunion(
            "Conseil d'École - Stratégie ENSA",
            cal.getTimeInMillis(),
            admin.id,
            "1. Stratégie de développement\n2. Partenariats industriels\n3. Recherche et innovation\n4. Budget et ressources"
        );
        long reunion5Id = db.reunionDao().insertReunion(reunion5);
        if (profAssistant1 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion5Id, profAssistant1.id));
        }
        if (profAssistant2 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion5Id, profAssistant2.id));
        }
        
        // Réunion 6: Coordination Stages
        cal.set(2026, Calendar.MARCH, 1, 14, 0);
        Reunion reunion6 = new Reunion(
            "Coordination Stages Étudiants",
            cal.getTimeInMillis(),
            admin.id,
            "1. Attribution des stages\n2. Suivi des stagiaires\n3. Évaluation des stages\n4. Relations entreprises"
        );
        long reunion6Id = db.reunionDao().insertReunion(reunion6);
        if (profAssistant1 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion6Id, profAssistant1.id));
        }
        
        // Réunion 7: Réunion PFE
        cal.set(2026, Calendar.MARCH, 10, 9, 30);
        Reunion reunion7 = new Reunion(
            "Organisation Projets de Fin d'Études",
            cal.getTimeInMillis(),
            admin.id,
            "1. Attribution des sujets PFE\n2. Planning des soutenances\n3. Jury et évaluation\n4. Calendrier académique"
        );
        long reunion7Id = db.reunionDao().insertReunion(reunion7);
        if (profAssistant1 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion7Id, profAssistant1.id));
        }
        if (profAssistant2 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion7Id, profAssistant2.id));
        }
        
        // Réunion 8: Réunion Qualité
        cal.set(2026, Calendar.MARCH, 15, 11, 0);
        Reunion reunion8 = new Reunion(
            "Réunion Qualité et Accréditation",
            cal.getTimeInMillis(),
            admin.id,
            "1. Processus qualité\n2. Accréditation des formations\n3. Indicateurs de performance\n4. Amélioration continue"
        );
        long reunion8Id = db.reunionDao().insertReunion(reunion8);
        if (profAssistant2 != null) {
            db.reunionParticipantDao().insertParticipant(new ReunionParticipant(reunion8Id, profAssistant2.id));
        }
    }
    
    private static void initializeCahiersCharges(AppDatabase db) {
        // Get users and formations
        User profAssistant1 = db.userDao().getUserByUsername("prof.assistant1");
        User profAssistant2 = db.userDao().getUserByUsername("prof.assistant2");
        
        List<Formation> formations = db.formationDao().getAllFormations();
        Formation ingInfoFormation = null;
        Formation ingIndFormation = null;
        Formation ingCivFormation = null;
        
        for (Formation f : formations) {
            if ("Génie Informatique".equals(f.title)) ingInfoFormation = f;
            if ("Génie Industriel".equals(f.title)) ingIndFormation = f;
            if ("Génie Civil".equals(f.title)) ingCivFormation = f;
        }
        
        if (profAssistant1 == null) return;
        
        // Cahier 1: Génie Informatique - Brouillon
        CahierCharges cahier1 = new CahierCharges(
            "Cahier de Charges - Génie Informatique 2026",
            "FORMATION_INITIALE",
            profAssistant1.id
        );
        if (ingInfoFormation != null) {
            cahier1.formationId = ingInfoFormation.id;
        }
        cahier1.statut = "BROUILLON";
        cahier1.dateCreation = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L); // 7 days ago
        db.cahierChargesDao().insertCahierCharges(cahier1);
        
        // Cahier 2: Génie Informatique - Envoyé
        CahierCharges cahier2 = new CahierCharges(
            "Cahier de Charges - Spécialisation IA",
            "FORMATION_INITIALE",
            profAssistant1.id
        );
        if (ingInfoFormation != null) {
            cahier2.formationId = ingInfoFormation.id;
        }
        cahier2.statut = "ENVOYE";
        cahier2.dateCreation = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000L);
        db.cahierChargesDao().insertCahierCharges(cahier2);
        
        // Cahier 3: Génie Industriel - Approuvé
        CahierCharges cahier3 = new CahierCharges(
            "Cahier de Charges - Génie Industriel",
            "FORMATION_INITIALE",
            profAssistant1.id
        );
        if (ingIndFormation != null) {
            cahier3.formationId = ingIndFormation.id;
        }
        cahier3.statut = "APPROUVE";
        cahier3.dateCreation = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L);
        cahier3.dateValidation = System.currentTimeMillis() - (20 * 24 * 60 * 60 * 1000L);
        db.cahierChargesDao().insertCahierCharges(cahier3);
        
        // Cahier 4: Génie Civil - Brouillon
        if (profAssistant2 != null) {
            CahierCharges cahier4 = new CahierCharges(
                "Cahier de Charges - Génie Civil",
                "FORMATION_INITIALE",
                profAssistant2.id
            );
            if (ingCivFormation != null) {
                cahier4.formationId = ingCivFormation.id;
            }
            cahier4.statut = "BROUILLON";
            cahier4.dateCreation = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000L);
            db.cahierChargesDao().insertCahierCharges(cahier4);
        }
        
        // Cahier 5: Formation Continue - Envoyé
        CahierCharges cahier5 = new CahierCharges(
            "Cahier de Charges - Formation Continue DCA",
            "FORMATION_CONTINUE",
            profAssistant1.id
        );
        cahier5.statut = "ENVOYE";
        cahier5.dateCreation = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000L);
        db.cahierChargesDao().insertCahierCharges(cahier5);
        
        // Cahier 6: Génie Informatique - Refusé
        CahierCharges cahier6 = new CahierCharges(
            "Cahier de Charges - Module Sécurité",
            "FORMATION_INITIALE",
            profAssistant1.id
        );
        if (ingInfoFormation != null) {
            cahier6.formationId = ingInfoFormation.id;
        }
        cahier6.statut = "REFUSE";
        cahier6.dateCreation = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000L);
        cahier6.dateValidation = System.currentTimeMillis() - (12 * 24 * 60 * 60 * 1000L);
        db.cahierChargesDao().insertCahierCharges(cahier6);
        
        // Cahier 7: Génie Industriel - Brouillon
        CahierCharges cahier7 = new CahierCharges(
            "Cahier de Charges - Logistique Avancée",
            "FORMATION_INITIALE",
            profAssistant1.id
        );
        if (ingIndFormation != null) {
            cahier7.formationId = ingIndFormation.id;
        }
        cahier7.statut = "BROUILLON";
        cahier7.dateCreation = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L);
        db.cahierChargesDao().insertCahierCharges(cahier7);
        
        // Cahier 8: Formation Continue - Approuvé
        CahierCharges cahier8 = new CahierCharges(
            "Cahier de Charges - DCESS Management",
            "FORMATION_CONTINUE",
            profAssistant1.id
        );
        cahier8.statut = "APPROUVE";
        cahier8.dateCreation = System.currentTimeMillis() - (45 * 24 * 60 * 60 * 1000L);
        cahier8.dateValidation = System.currentTimeMillis() - (35 * 24 * 60 * 60 * 1000L);
        db.cahierChargesDao().insertCahierCharges(cahier8);
        
        // Cahier 9: Génie Informatique - Envoyé
        CahierCharges cahier9 = new CahierCharges(
            "Cahier de Charges - Développement Mobile",
            "FORMATION_INITIALE",
            profAssistant1.id
        );
        if (ingInfoFormation != null) {
            cahier9.formationId = ingInfoFormation.id;
        }
        cahier9.statut = "ENVOYE";
        cahier9.dateCreation = System.currentTimeMillis() - (8 * 24 * 60 * 60 * 1000L);
        db.cahierChargesDao().insertCahierCharges(cahier9);
        
        // Cahier 10: Génie Civil - Envoyé
        if (profAssistant2 != null && ingCivFormation != null) {
            CahierCharges cahier10 = new CahierCharges(
                "Cahier de Charges - Structures Métalliques",
                "FORMATION_INITIALE",
                profAssistant2.id
            );
            cahier10.formationId = ingCivFormation.id;
            cahier10.statut = "ENVOYE";
            cahier10.dateCreation = System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000L);
            db.cahierChargesDao().insertCahierCharges(cahier10);
        }
    }
    
    // Method to force add emploi temps data (useful if database was created before this feature)
    public static void addEmploiTempsIfMissing(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        if (db.emploiTempsDao().getAllEmploiTemps().size() == 0) {
            initializeEmploiTemps(db);
        }
    }
}