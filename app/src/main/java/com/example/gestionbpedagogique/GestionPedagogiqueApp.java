package com.example.gestionbpedagogique;

import android.app.Application;

import com.example.gestionbpedagogique.database.DatabaseInitializer;

public class GestionPedagogiqueApp extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize database with sample data
        DatabaseInitializer.initializeDatabase(this);
    }
}