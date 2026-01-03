package com.example.gestionbpedagogique;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.dao.CahierChargesDao;
import com.example.gestionbpedagogique.database.dao.FormationDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.CahierCharges;
import com.example.gestionbpedagogique.database.entities.Formation;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CahierChargesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CahierChargesAdapter adapter;
    private EditText searchEditText;
    private TextView emptyStateText;
    private FloatingActionButton addButton;
    private long userId;
    private String userType;
    private List<CahierChargesItem> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cahier_charges);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        
        initializeViews();
        loadUserTypeAndCahiers();
        setupSearch();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadUserTypeAndCahiers();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        emptyStateText = findViewById(R.id.empty_state_text);
        addButton = findViewById(R.id.add_button);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CahierChargesAdapter(new ArrayList<>(), userId, userType, this);
        recyclerView.setAdapter(adapter);
        
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(CahierChargesActivity.this, CahierChargesEditActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }
    
    private void loadUserTypeAndCahiers() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            CahierChargesDao cahierChargesDao = db.cahierChargesDao();
            UserDao userDao = db.userDao();
            FormationDao formationDao = db.formationDao();
            
            User currentUser = userDao.getUserById(userId);
            if (currentUser != null) {
                userType = currentUser.userType;
            }
            
            List<CahierCharges> cahiersList;
            
            // Admin voit tous les cahiers, Professeur Assistant voit seulement les siens
            if ("ADMIN".equals(userType)) {
                cahiersList = cahierChargesDao.getAllCahierCharges();
            } else {
                cahiersList = cahierChargesDao.getCahierChargesByAuteur(userId);
            }
            
            allItems.clear();
            for (CahierCharges cc : cahiersList) {
                User auteur = userDao.getUserById(cc.auteurId);
                Formation formation = null;
                if (cc.formationId != null) {
                    formation = formationDao.getFormationById(cc.formationId);
                }
                
                if (auteur != null) {
                    allItems.add(new CahierChargesItem(
                        cc.id,
                        cc.titre,
                        cc.type,
                        auteur.fullName,
                        formation != null ? formation.title : null,
                        cc.statut,
                        cc.dateCreation,
                        cc.dateValidation
                    ));
                }
            }
            
            runOnUiThread(() -> {
                adapter = new CahierChargesAdapter(allItems, userId, userType, this);
                recyclerView.setAdapter(adapter);
                updateEmptyState();
                
                // Show add button only for Professeur Assistant (they create cahiers)
                // Admin can view and approve/refuse, but creation is done by assistants
                if ("PROFESSEUR_ASSISTANT".equals(userType) && addButton != null) {
                    addButton.setVisibility(View.VISIBLE);
                } else if (addButton != null) {
                    addButton.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterItems(String query) {
        List<CahierChargesItem> filtered = new ArrayList<>();

        if (query.isEmpty()) {
            filtered.addAll(allItems);
        } else {
            String lowerQuery = query.toLowerCase();
            for (CahierChargesItem item : allItems) {
                if (item.titre.toLowerCase().contains(lowerQuery) ||
                    item.type.toLowerCase().contains(lowerQuery) ||
                    item.auteurName.toLowerCase().contains(lowerQuery) ||
                    item.statut.toLowerCase().contains(lowerQuery) ||
                    (item.formationTitle != null && item.formationTitle.toLowerCase().contains(lowerQuery))) {
                    filtered.add(item);
                }
            }
        }

        adapter.updateItems(filtered);
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // Inner class for display items
    static class CahierChargesItem {
        long id;
        String titre;
        String type;
        String auteurName;
        String formationTitle;
        String statut;
        long dateCreation;
        Long dateValidation;

        CahierChargesItem(long id, String titre, String type, String auteurName, 
                         String formationTitle, String statut, long dateCreation, Long dateValidation) {
            this.id = id;
            this.titre = titre;
            this.type = type;
            this.auteurName = auteurName;
            this.formationTitle = formationTitle;
            this.statut = statut;
            this.dateCreation = dateCreation;
            this.dateValidation = dateValidation;
        }
    }
}
