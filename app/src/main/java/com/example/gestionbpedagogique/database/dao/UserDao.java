package com.example.gestionbpedagogique.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.gestionbpedagogique.database.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAllUsers();
    
    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(long id);
    
    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);
    
    @Query("SELECT * FROM users WHERE userType = :userType")
    List<User> getUsersByType(String userType);
    
    @Insert
    long insertUser(User user);
    
    @Insert
    void insertUsers(User... users);
    
    @Update
    void updateUser(User user);
    
    @Delete
    void deleteUser(User user);
    
    @Query("DELETE FROM users")
    void deleteAllUsers();
}