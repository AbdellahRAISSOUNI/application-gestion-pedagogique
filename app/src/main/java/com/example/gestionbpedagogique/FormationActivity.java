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
import com.example.gestionbpedagogique.database.dao.FormationDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.Formation;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FormationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FormationAdapter adapter;
    private EditText searchEditText;
    private TextView emptyStateText;
    private FloatingActionButton addButton;
    private long userId;
    private String userType;
    private List<FormationItem> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formation);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        
        initializeViews();
        loadUserTypeAndFormations();
        setupSearch();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadUserTypeAndFormations();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        emptyStateText = findViewById(R.id.empty_state_text);
        addButton = findViewById(R.id.add_button);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FormationAdapter(new ArrayList<>(), userId, userType, this);
        recyclerView.setAdapter(adapter);
        
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(FormationActivity.this, FormationEditActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }
    
    private void loadUserTypeAndFormations() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            FormationDao formationDao = db.formationDao();
            UserDao userDao = db.userDao();
            
            User currentUser = userDao.getUserById(userId);
            if (currentUser != null) {
                userType = currentUser.userType;
            }
            
            // Only Admin can access this feature
            if (!"ADMIN".equals(userType)) {
                runOnUiThread(() -> {
                    finish();
                });
                return;
            }
            
            List<Formation> formationsList = formationDao.getAllFormations();
            
            allItems.clear();
            for (Formation formation : formationsList) {
                User createdBy = userDao.getUserById(formation.createdByUserId);
                allItems.add(new FormationItem(
                    formation.id,
                    formation.title,
                    formation.type,
                    formation.cycle,
                    formation.description,
                    formation.status,
                    formation.createdDate,
                    formation.validatedDate,
                    createdBy != null ? createdBy.fullName : "Inconnu"
                ));
            }
            
            runOnUiThread(() -> {
                adapter = new FormationAdapter(allItems, userId, userType, this);
                recyclerView.setAdapter(adapter);
                updateEmptyState();
                
                // Show add button for Admin
                if ("ADMIN".equals(userType) && addButton != null) {
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
        List<FormationItem> filtered = new ArrayList<>();

        if (query.isEmpty()) {
            filtered.addAll(allItems);
        } else {
            String lowerQuery = query.toLowerCase();
            for (FormationItem item : allItems) {
                if (item.title.toLowerCase().contains(lowerQuery) ||
                    item.type.toLowerCase().contains(lowerQuery) ||
                    item.cycle.toLowerCase().contains(lowerQuery) ||
                    item.status.toLowerCase().contains(lowerQuery) ||
                    (item.description != null && item.description.toLowerCase().contains(lowerQuery)) ||
                    item.createdByName.toLowerCase().contains(lowerQuery)) {
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
    static class FormationItem {
        long id;
        String title;
        String type;
        String cycle;
        String description;
        String status;
        long createdDate;
        Long validatedDate;
        String createdByName;

        FormationItem(long id, String title, String type, String cycle, String description,
                     String status, long createdDate, Long validatedDate, String createdByName) {
            this.id = id;
            this.title = title;
            this.type = type;
            this.cycle = cycle;
            this.description = description;
            this.status = status;
            this.createdDate = createdDate;
            this.validatedDate = validatedDate;
            this.createdByName = createdByName;
        }
    }
}
