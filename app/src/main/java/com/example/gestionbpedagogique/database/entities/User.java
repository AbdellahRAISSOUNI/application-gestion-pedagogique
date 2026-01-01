package com.example.gestionbpedagogique.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(tableName = "users", indices = {@Index(value = {"username"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String username;
    public String password; // Should be hashed in production
    public String userType; // "ADMIN", "PROFESSEUR_ASSISTANT", "PROFESSEUR_VACATAIRE"
    public String fullName;
    public String email;
    public String phone;
    
    public User() {}
    
    public User(String username, String password, String userType, String fullName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
}