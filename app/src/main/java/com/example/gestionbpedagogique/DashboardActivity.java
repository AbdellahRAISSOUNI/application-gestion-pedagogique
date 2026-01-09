package com.example.gestionbpedagogique;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    private TextView userNameText;
    private TextView userTypeText;
    private LinearLayout menuContainer;
    private long userId;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get user ID from intent
        userId = getIntent().getLongExtra("USER_ID", -1);
        
        initializeViews();
        loadUserInfo();
        setupMenu();
    }

    private void initializeViews() {
        userNameText = findViewById(R.id.user_name_text);
        userTypeText = findViewById(R.id.user_type_text);
        menuContainer = findViewById(R.id.menu_container);
        
        // Logout button
        findViewById(R.id.logout_button).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserInfo() {
        if (userId == -1) {
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getDatabase(this);
        User user = db.userDao().getUserById(userId);

        if (user != null) {
            userNameText.setText(user.fullName);
            userType = user.userType;
            String userTypeFrench = translateUserType(user.userType);
            userTypeText.setText(userTypeFrench);
        } else {
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

    private void setupMenu() {
        menuContainer.removeAllViews();

        switch (userType) {
            case "ADMIN":
                addMenuCard(getString(R.string.menu_planifier_reunion), android.R.drawable.ic_menu_agenda, v -> {
                    Intent intent = new Intent(DashboardActivity.this, ReunionActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                addMenuCard(getString(R.string.menu_envoyer_cahier), android.R.drawable.ic_menu_myplaces, v -> {
                    Intent intent = new Intent(DashboardActivity.this, CahierChargesActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                addMenuCard(getString(R.string.menu_traiter_formation), android.R.drawable.ic_menu_recent_history, v -> {
                    Intent intent = new Intent(DashboardActivity.this, FormationActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                addMenuCard(getString(R.string.menu_elaborer_emploi), android.R.drawable.ic_menu_today, v -> {
                    Intent intent = new Intent(DashboardActivity.this, EmploiTempsActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                break;

            case "PROFESSEUR_ASSISTANT":
                addMenuCard(getString(R.string.menu_envoyer_cahier), android.R.drawable.ic_menu_myplaces, v -> {
                    Intent intent = new Intent(DashboardActivity.this, CahierChargesActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                addMenuCard(getString(R.string.menu_consulter_emploi), android.R.drawable.ic_menu_today, v -> {
                    Intent intent = new Intent(DashboardActivity.this, EmploiTempsActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                addMenuCard(getString(R.string.menu_consulter_reunions), android.R.drawable.ic_menu_agenda, v -> {
                    Intent intent = new Intent(DashboardActivity.this, ReunionActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                break;

            case "PROFESSEUR_VACATAIRE":
                addMenuCard(getString(R.string.menu_consulter_emploi), android.R.drawable.ic_menu_today, v -> {
                    Intent intent = new Intent(DashboardActivity.this, EmploiTempsActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                addMenuCard(getString(R.string.menu_consulter_reunions), android.R.drawable.ic_menu_agenda, v -> {
                    Intent intent = new Intent(DashboardActivity.this, ReunionActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                });
                break;
        }
    }

    private void addMenuCard(String title, int iconResId, View.OnClickListener onClickListener) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);
        card.setCardElevation(4);
        card.setRadius(16);
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_background));
        card.setPadding(24, 24, 24, 24);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(onClickListener);

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.HORIZONTAL);
        cardContent.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Icon
        ImageView iconView = new ImageView(this);
        Drawable iconDrawable = ContextCompat.getDrawable(this, iconResId);
        if (iconDrawable != null) {
            iconDrawable.setTint(ContextCompat.getColor(this, R.color.primary_blue));
            iconView.setImageDrawable(iconDrawable);
        }
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(48, 48);
        iconParams.setMargins(0, 0, 16, 0);
        iconView.setLayoutParams(iconParams);

        // Title
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(16);
        titleView.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        titleView.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        ));

        cardContent.addView(iconView);
        cardContent.addView(titleView);
        card.addView(cardContent);

        menuContainer.addView(card);
    }
}