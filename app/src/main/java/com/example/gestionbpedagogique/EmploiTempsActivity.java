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
import com.example.gestionbpedagogique.database.DatabaseInitializer;
import com.example.gestionbpedagogique.database.dao.EmploiTempsDao;
import com.example.gestionbpedagogique.database.dao.ModuleDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.EmploiTemps;
import com.example.gestionbpedagogique.database.entities.Module;
import com.example.gestionbpedagogique.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class EmploiTempsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmploiTempsAdapter adapter;
    private EditText searchEditText;
    private TextView emptyStateText;
    private com.google.android.material.button.MaterialButton addButton;
    private long userId;
    private String userType;
    private List<EmploiTempsItem> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emploi_temps);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        
        // Ensure emploi temps data exists
        DatabaseInitializer.addEmploiTempsIfMissing(this);
        
        initializeViews();
        loadEmploiTemps();
        setupSearch();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning from edit activity
        loadEmploiTemps();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        emptyStateText = findViewById(R.id.empty_state_text);
        addButton = findViewById(R.id.add_button);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmploiTempsAdapter(new ArrayList<>(), null, userId, this);
        recyclerView.setAdapter(adapter);
    }
    
    private void setupAddButton() {
        if ("ADMIN".equals(userType) && addButton != null) {
            addButton.setVisibility(View.VISIBLE);
            addButton.setOnClickListener(v -> {
                Intent intent = new Intent(EmploiTempsActivity.this, EmploiTempsEditActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            });
        }
    }

    private void loadEmploiTemps() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            EmploiTempsDao emploiTempsDao = db.emploiTempsDao();
            ModuleDao moduleDao = db.moduleDao();
            UserDao userDao = db.userDao();
            
            User currentUser = userDao.getUserById(userId);
            if (currentUser != null) {
                userType = currentUser.userType;
            }

            List<EmploiTemps> emploiTempsList;
            
            // Admin can see all schedules, others see only their own
            if ("ADMIN".equals(userType)) {
                emploiTempsList = emploiTempsDao.getAllEmploiTemps();
            } else {
                emploiTempsList = emploiTempsDao.getEmploiTempsByProfesseur(userId);
            }

            allItems.clear();
            for (EmploiTemps et : emploiTempsList) {
                Module module = moduleDao.getModuleById(et.moduleId);
                User professeur = userDao.getUserById(et.professeurId);
                
                if (module != null && professeur != null) {
                    allItems.add(new EmploiTempsItem(
                        et.id,
                        professeur.fullName,
                        module.nom,
                        module.code,
                        et.jourSemaine,
                        et.heureDebut,
                        et.heureFin,
                        et.salle,
                        et.typeCours
                    ));
                }
            }

            runOnUiThread(() -> {
                // Update adapter with user type after loading
                adapter = new EmploiTempsAdapter(allItems, userType, userId, this);
                recyclerView.setAdapter(adapter);
                updateEmptyState();
                setupAddButton();
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
        List<EmploiTempsItem> filtered = new ArrayList<>();
        
        if (query.isEmpty()) {
            filtered.addAll(allItems);
        } else {
            String lowerQuery = query.toLowerCase();
            for (EmploiTempsItem item : allItems) {
                if (item.professeurName.toLowerCase().contains(lowerQuery) ||
                    item.moduleName.toLowerCase().contains(lowerQuery) ||
                    item.moduleCode.toLowerCase().contains(lowerQuery) ||
                    item.jourSemaine.toLowerCase().contains(lowerQuery) ||
                    item.salle.toLowerCase().contains(lowerQuery) ||
                    item.typeCours.toLowerCase().contains(lowerQuery)) {
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
    static class EmploiTempsItem {
        long id;
        String professeurName;
        String moduleName;
        String moduleCode;
        String jourSemaine;
        String heureDebut;
        String heureFin;
        String salle;
        String typeCours;

        EmploiTempsItem(long id, String professeurName, String moduleName, String moduleCode,
                       String jourSemaine, String heureDebut, String heureFin, String salle, String typeCours) {
            this.id = id;
            this.professeurName = professeurName;
            this.moduleName = moduleName;
            this.moduleCode = moduleCode;
            this.jourSemaine = jourSemaine;
            this.heureDebut = heureDebut;
            this.heureFin = heureFin;
            this.salle = salle;
            this.typeCours = typeCours;
        }
    }
}