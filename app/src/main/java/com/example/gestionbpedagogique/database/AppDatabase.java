package com.example.gestionbpedagogique.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gestionbpedagogique.database.dao.CahierChargesDao;
import com.example.gestionbpedagogique.database.dao.EmploiTempsDao;
import com.example.gestionbpedagogique.database.dao.FormationDao;
import com.example.gestionbpedagogique.database.dao.ModuleDao;
import com.example.gestionbpedagogique.database.dao.ReunionDao;
import com.example.gestionbpedagogique.database.dao.ReunionParticipantDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.CahierCharges;
import com.example.gestionbpedagogique.database.entities.EmploiTemps;
import com.example.gestionbpedagogique.database.entities.Formation;
import com.example.gestionbpedagogique.database.entities.Module;
import com.example.gestionbpedagogique.database.entities.Reunion;
import com.example.gestionbpedagogique.database.entities.ReunionParticipant;
import com.example.gestionbpedagogique.database.entities.User;

@Database(
    entities = {
        User.class,
        Formation.class,
        Module.class,
        CahierCharges.class,
        Reunion.class,
        ReunionParticipant.class,
        EmploiTemps.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    
    public abstract UserDao userDao();
    public abstract FormationDao formationDao();
    public abstract ModuleDao moduleDao();
    public abstract CahierChargesDao cahierChargesDao();
    public abstract ReunionDao reunionDao();
    public abstract ReunionParticipantDao reunionParticipantDao();
    public abstract EmploiTempsDao emploiTempsDao();
    
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "gestion_pedagogique_db"
                    )
                    .fallbackToDestructiveMigration() // For development only
                    .allowMainThreadQueries() // For development only - should use background threads in production
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    public static void destroyInstance() {
        INSTANCE = null;
    }
}