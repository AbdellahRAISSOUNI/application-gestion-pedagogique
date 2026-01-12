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
import com.example.gestionbpedagogique.database.entities.Reunion;
import com.example.gestionbpedagogique.database.entities.ReunionParticipant;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView userNameText;
    private TextView userTypeText;
    private LinearLayout menuContainer;
    private LinearLayout statisticsContainer;
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
        loadStatistics();
        setupMenu();
    }

    private void initializeViews() {
        userNameText = findViewById(R.id.user_name_text);
        userTypeText = findViewById(R.id.user_type_text);
        menuContainer = findViewById(R.id.menu_container);
        statisticsContainer = findViewById(R.id.statistics_container);
        
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

    private void loadStatistics() {
        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(this);
                
                int totalFormations = 0;
                int pendingCahiers = 0;
                int upcomingReunions = 0;
                int totalModules = 0;
                int mySchedule = 0;
                
                if ("ADMIN".equals(userType)) {
                    totalFormations = db.formationDao().getAllFormations().size();
                    pendingCahiers = db.cahierChargesDao().getCahierChargesByStatut("ENVOYE").size();
                    upcomingReunions = getUpcomingReunionsCount(db);
                    totalModules = db.moduleDao().getAllModules().size();
                } else {
                    // For non-admin users, show their own statistics
                    mySchedule = db.emploiTempsDao().getEmploiTempsByProfesseur(userId).size();
                    pendingCahiers = db.cahierChargesDao().getCahierChargesByAuteur(userId).size();
                    upcomingReunions = getMyUpcomingReunionsCount(db, userId);
                }
                
                final int finalTotalFormations = totalFormations;
                final int finalPendingCahiers = pendingCahiers;
                final int finalUpcomingReunions = upcomingReunions;
                final int finalTotalModules = totalModules;
                final int finalMySchedule = mySchedule;
                
                runOnUiThread(() -> {
                    statisticsContainer.removeAllViews();
                    
                    if ("ADMIN".equals(userType)) {
                        addStatCard(getString(R.string.stat_total_formations), finalTotalFormations, 
                            R.color.status_info, android.R.drawable.ic_menu_recent_history);
                        addStatCard(getString(R.string.stat_pending_cahiers), finalPendingCahiers, 
                            R.color.accent_orange, android.R.drawable.ic_menu_myplaces);
                        addStatCard(getString(R.string.stat_upcoming_reunions), finalUpcomingReunions, 
                            R.color.primary_blue, android.R.drawable.ic_menu_agenda);
                        addStatCard(getString(R.string.stat_total_modules), finalTotalModules, 
                            R.color.status_success, android.R.drawable.ic_menu_view);
                    } else {
                        addStatCard(getString(R.string.stat_my_schedule), finalMySchedule, 
                            R.color.status_success, android.R.drawable.ic_menu_today);
                        addStatCard(getString(R.string.stat_pending_cahiers), finalPendingCahiers, 
                            R.color.accent_orange, android.R.drawable.ic_menu_myplaces);
                        addStatCard(getString(R.string.stat_upcoming_reunions), finalUpcomingReunions, 
                            R.color.primary_blue, android.R.drawable.ic_menu_agenda);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int getUpcomingReunionsCount(AppDatabase db) {
        long currentTime = System.currentTimeMillis();
        long nextWeek = currentTime + (7 * 24 * 60 * 60 * 1000L);
        List<Reunion> reunions = 
            db.reunionDao().getReunionsByDateRange(currentTime, nextWeek);
        return reunions.size();
    }

    private int getMyUpcomingReunionsCount(AppDatabase db, long userId) {
        long currentTime = System.currentTimeMillis();
        long nextWeek = currentTime + (7 * 24 * 60 * 60 * 1000L);
        List<Reunion> allReunions = 
            db.reunionDao().getReunionsByDateRange(currentTime, nextWeek);
        
        // Check if user is a participant
        int count = 0;
        for (Reunion reunion : allReunions) {
            List<ReunionParticipant> participants = 
                db.reunionParticipantDao().getParticipantsByReunion(reunion.id);
            for (ReunionParticipant participant : participants) {
                if (participant.userId == userId) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    private void addStatCard(String title, int value, int colorResId, int iconResId) {
        // Create horizontal container for 2 cards per row
        LinearLayout rowContainer = null;
        if (statisticsContainer.getChildCount() % 2 == 0) {
            rowContainer = new LinearLayout(this);
            rowContainer.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, 16);
            rowContainer.setLayoutParams(rowParams);
            statisticsContainer.addView(rowContainer);
        } else {
            rowContainer = (LinearLayout) statisticsContainer.getChildAt(statisticsContainer.getChildCount() - 1);
        }

        // Create stat card
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        );
        cardParams.setMargins(0, 0, 8, 0);
        card.setLayoutParams(cardParams);
        card.setCardElevation(6);
        card.setRadius(20);
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_background));
        card.setStrokeWidth(2);
        card.setStrokeColor(ContextCompat.getColor(this, colorResId));
        card.setPadding(0, 0, 0, 0);

        // Inner container
        LinearLayout innerContainer = new LinearLayout(this);
        innerContainer.setOrientation(LinearLayout.VERTICAL);
        innerContainer.setPadding(20, 20, 20, 20);

        // Icon and number row
        LinearLayout topRow = new LinearLayout(this);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Icon
        MaterialCardView iconCard = new MaterialCardView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(48, 48);
        iconParams.setMargins(0, 0, 12, 0);
        iconCard.setLayoutParams(iconParams);
        iconCard.setRadius(12);
        iconCard.setCardElevation(0);
        iconCard.setCardBackgroundColor(ContextCompat.getColor(this, colorResId));
        iconCard.setPadding(12, 12, 12, 12);

        ImageView iconView = new ImageView(this);
        Drawable iconDrawable = ContextCompat.getDrawable(this, iconResId);
        if (iconDrawable != null) {
            iconDrawable.setTint(ContextCompat.getColor(this, R.color.white));
            iconView.setImageDrawable(iconDrawable);
        }
        LinearLayout.LayoutParams iconViewParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        iconView.setLayoutParams(iconViewParams);
        iconCard.addView(iconView);
        topRow.addView(iconCard);

        // Number
        TextView numberText = new TextView(this);
        numberText.setText("0");
        numberText.setTextSize(28);
        numberText.setTypeface(null, android.graphics.Typeface.BOLD);
        numberText.setTextColor(ContextCompat.getColor(this, colorResId));
        numberText.setId(android.R.id.text1); // Use a unique ID for animation
        topRow.addView(numberText);

        innerContainer.addView(topRow);

        // Title
        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(13);
        titleText.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        titleText.setPadding(0, 8, 0, 0);
        innerContainer.addView(titleText);

        card.addView(innerContainer);
        rowContainer.addView(card);

        // Animate number counting up
        animateNumber(numberText, 0, value, 1000);
    }

    private void animateNumber(TextView textView, int from, int to, long duration) {
        android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofInt(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            textView.setText(String.valueOf(value));
        });
        animator.start();
    }
}