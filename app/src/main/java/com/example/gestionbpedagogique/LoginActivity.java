package com.example.gestionbpedagogique;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private MaterialButton loginButton;
    private MaterialCheckBox rememberMeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        usernameLayout = findViewById(R.id.username_layout);
        passwordLayout = findViewById(R.id.password_layout);
        loginButton = findViewById(R.id.login_button);
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);

        // Back button
        findViewById(R.id.back_button).setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Set click listener for forgot password
        findViewById(R.id.forgot_password_text).setOnClickListener(v -> {
            Toast.makeText(this, "Fonctionnalité à venir", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        // Clear previous errors
        usernameLayout.setError(null);
        passwordLayout.setError(null);

        // Get input values
        String username = usernameInput.getText() != null ? usernameInput.getText().toString().trim() : "";
        String password = passwordInput.getText() != null ? passwordInput.getText().toString() : "";

        // Validate inputs
        boolean isValid = true;

        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Le nom d'utilisateur est requis");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Le mot de passe est requis");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Le mot de passe doit contenir au moins 6 caractères");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Disable button during login
        loginButton.setEnabled(false);
        loginButton.setText("Connexion en cours...");

        // Perform authentication using database
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            User user = db.userDao().getUserByUsername(username);

            runOnUiThread(() -> {
                if (user != null && user.password.equals(password)) {
                    // Login successful
                    if (rememberMeCheckbox.isChecked()) {
                        // TODO: Save credentials securely using SharedPreferences or DataStore
                    }

                    Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to home activity
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("USER_ID", user.id);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    // Login failed
                    usernameLayout.setError(getString(R.string.login_error));
                    passwordLayout.setError(getString(R.string.login_error));
                    loginButton.setEnabled(true);
                    loginButton.setText(getString(R.string.login_button));
                }
            });
        }).start();
    }
}