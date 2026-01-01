package com.example.gestionbpedagogique;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.entities.User;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private TextView userNameText;
    private TextView userTypeText;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get user ID from intent
        userId = getIntent().getLongExtra("USER_ID", -1);
        
        initializeViews();
        loadUserInfo();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcome_text);
        userNameText = findViewById(R.id.user_name_text);
        userTypeText = findViewById(R.id.user_type_text);
    }

    private void loadUserInfo() {
        if (userId == -1) {
            // If no user ID, go back to login
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getDatabase(this);
        User user = db.userDao().getUserById(userId);

        if (user != null) {
            // Display user information
            welcomeText.setText(getString(R.string.welcome_user));
            userNameText.setText(user.fullName);
            
            // Translate user type to French
            String userTypeFrench = translateUserType(user.userType);
            userTypeText.setText(userTypeFrench);
        } else {
            // User not found, go back to login
            finish();
        }
    }

    private String translateUserType(String userType) {
        switch (userType) {
            case "ADMIN":
                return getString(R.string.user_type_admin);
            case "PROFESSEUR_ASSISTANT":
                return getString(R.string.user_type_prof_assistant);
            case "PROFESSEUR_VACATAIRE":
                return getString(R.string.user_type_prof_vacataire);
            default:
                return userType;
        }
    }
}