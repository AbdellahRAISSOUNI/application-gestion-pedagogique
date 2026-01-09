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
        // Determine color scheme based on menu item
        int iconBgColor = getIconColorForMenu(title);
        int accentColor = getAccentColorForMenu(title);
        
        // Outer card with accent border effect
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 28);
        card.setLayoutParams(cardParams);
        card.setCardElevation(12);
        card.setRadius(32);
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_background));
        card.setStrokeWidth(2);
        card.setStrokeColor(accentColor);
        card.setPadding(0, 0, 0, 0);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(onClickListener);

        // Inner container with gradient-like effect
        LinearLayout innerContainer = new LinearLayout(this);
        innerContainer.setOrientation(LinearLayout.VERTICAL);
        innerContainer.setPadding(0, 0, 0, 0);
        
        // Accent bar at top
        View accentBar = new View(this);
        LinearLayout.LayoutParams accentBarParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            4
        );
        accentBar.setLayoutParams(accentBarParams);
        accentBar.setBackgroundColor(accentColor);
        
        // Main content container
        LinearLayout mainContainer = new LinearLayout(this);
        mainContainer.setOrientation(LinearLayout.HORIZONTAL);
        mainContainer.setGravity(android.view.Gravity.CENTER_VERTICAL);
        mainContainer.setPadding(32, 32, 24, 32);
        mainContainer.setBackground(null);

        // Icon Container with Enhanced Design
        MaterialCardView iconContainer = new MaterialCardView(this);
        LinearLayout.LayoutParams iconContainerParams = new LinearLayout.LayoutParams(96, 96);
        iconContainerParams.setMargins(0, 0, 28, 0);
        iconContainer.setLayoutParams(iconContainerParams);
        iconContainer.setRadius(24);
        iconContainer.setCardElevation(8);
        iconContainer.setCardBackgroundColor(iconBgColor);
        iconContainer.setPadding(24, 24, 24, 24);
        
        // Add subtle inner shadow effect with a darker border
        iconContainer.setStrokeWidth(0);

        // Icon
        ImageView iconView = new ImageView(this);
        Drawable iconDrawable = ContextCompat.getDrawable(this, iconResId);
        if (iconDrawable != null) {
            iconDrawable.setTint(ContextCompat.getColor(this, R.color.white));
            iconView.setImageDrawable(iconDrawable);
        }
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        iconView.setLayoutParams(iconParams);

        iconContainer.addView(iconView);

        // Text Container
        LinearLayout textContainer = new LinearLayout(this);
        textContainer.setOrientation(LinearLayout.VERTICAL);
        textContainer.setLayoutParams(new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        ));

        // Title with enhanced styling
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(19);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        titleView.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        titleView.setLetterSpacing(0.015f);
        titleView.setLineSpacing(6, 1.1f);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, 4);
        titleView.setLayoutParams(titleParams);

        textContainer.addView(titleView);
        
        // Add subtle description/subtitle based on menu item
        TextView subtitleView = new TextView(this);
        String subtitle = getSubtitleForMenu(title);
        subtitleView.setText(subtitle);
        subtitleView.setTextSize(13);
        subtitleView.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        subtitleView.setLetterSpacing(0.01f);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        subtitleParams.setMargins(0, 2, 0, 0);
        subtitleView.setLayoutParams(subtitleParams);
        
        textContainer.addView(subtitleView);

        mainContainer.addView(iconContainer);
        mainContainer.addView(textContainer);

        // Arrow Icon with Enhanced Modern Style
        MaterialCardView arrowContainer = new MaterialCardView(this);
        LinearLayout.LayoutParams arrowContainerParams = new LinearLayout.LayoutParams(44, 44);
        arrowContainer.setLayoutParams(arrowContainerParams);
        arrowContainer.setRadius(22);
        arrowContainer.setCardElevation(2);
        arrowContainer.setCardBackgroundColor(accentColor);
        arrowContainer.setPadding(10, 10, 10, 10);
        
        ImageView arrowView = new ImageView(this);
        Drawable arrowDrawable = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_more);
        if (arrowDrawable != null) {
            arrowDrawable.setTint(ContextCompat.getColor(this, R.color.white));
            arrowView.setImageDrawable(arrowDrawable);
        }
        LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        arrowView.setLayoutParams(arrowParams);
        arrowView.setRotation(270); // Rotate to point right
        
        arrowContainer.addView(arrowView);
        mainContainer.addView(arrowContainer);
        
        innerContainer.addView(accentBar);
        innerContainer.addView(mainContainer);
        card.addView(innerContainer);

        menuContainer.addView(card);
    }
    
    private int getIconColorForMenu(String menuTitle) {
        // Return different colors based on menu item for visual distinction
        if (menuTitle.contains("réunion") || menuTitle.contains("Réunion")) {
            return ContextCompat.getColor(this, R.color.primary_blue);
        } else if (menuTitle.contains("cahier") || menuTitle.contains("Cahier")) {
            return ContextCompat.getColor(this, R.color.accent_orange);
        } else if (menuTitle.contains("formation") || menuTitle.contains("Formation")) {
            return ContextCompat.getColor(this, R.color.status_info);
        } else if (menuTitle.contains("emploi") || menuTitle.contains("Emploi")) {
            return ContextCompat.getColor(this, R.color.status_success);
        } else {
            return ContextCompat.getColor(this, R.color.primary_blue_light);
        }
    }
    
    private int getAccentColorForMenu(String menuTitle) {
        // Return accent color for border and arrow (slightly lighter than icon)
        if (menuTitle.contains("réunion") || menuTitle.contains("Réunion")) {
            return ContextCompat.getColor(this, R.color.primary_blue_light);
        } else if (menuTitle.contains("cahier") || menuTitle.contains("Cahier")) {
            return ContextCompat.getColor(this, R.color.accent_orange_light);
        } else if (menuTitle.contains("formation") || menuTitle.contains("Formation")) {
            return ContextCompat.getColor(this, R.color.primary_blue_lighter);
        } else if (menuTitle.contains("emploi") || menuTitle.contains("Emploi")) {
            return ContextCompat.getColor(this, R.color.status_success);
        } else {
            return ContextCompat.getColor(this, R.color.primary_blue_light);
        }
    }
    
    private String getSubtitleForMenu(String menuTitle) {
        // Return descriptive subtitle for each menu item
        if (menuTitle.contains("réunion") || menuTitle.contains("Réunion")) {
            return "Planifier et gérer les réunions pédagogiques";
        } else if (menuTitle.contains("cahier") || menuTitle.contains("Cahier")) {
            return "Envoyer et suivre les cahiers de charges";
        } else if (menuTitle.contains("formation") || menuTitle.contains("Formation")) {
            return "Gérer les formations et modules";
        } else if (menuTitle.contains("emploi") || menuTitle.contains("Emploi")) {
            if (menuTitle.contains("Élaborer") || menuTitle.contains("élaborer")) {
                return "Créer et modifier les emplois du temps";
            } else {
                return "Consulter votre emploi du temps";
            }
        } else {
            return "Accéder à cette fonctionnalité";
        }
    }
}