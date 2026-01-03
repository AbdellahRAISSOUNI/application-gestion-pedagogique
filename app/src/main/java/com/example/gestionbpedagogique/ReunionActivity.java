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
import com.example.gestionbpedagogique.database.dao.ReunionDao;
import com.example.gestionbpedagogique.database.dao.ReunionParticipantDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.Reunion;
import com.example.gestionbpedagogique.database.entities.ReunionParticipant;
import com.example.gestionbpedagogique.database.entities.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReunionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReunionAdapter adapter;
    private EditText searchEditText;
    private TextView emptyStateText;
    private com.google.android.material.button.MaterialButton addButton;
    private long userId;
    private List<ReunionItem> allItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reunion);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        
        initializeViews();
        loadReunions();
        setupSearch();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning from edit activity
        loadReunions();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
        emptyStateText = findViewById(R.id.empty_state_text);
        addButton = findViewById(R.id.add_button);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReunionAdapter(new ArrayList<>(), userId, this);
        recyclerView.setAdapter(adapter);
        
        // Setup add button
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReunionActivity.this, ReunionEditActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    private void loadReunions() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            ReunionDao reunionDao = db.reunionDao();
            ReunionParticipantDao participantDao = db.reunionParticipantDao();
            UserDao userDao = db.userDao();
            
            List<Reunion> reunions = reunionDao.getAllReunions();

            allItems.clear();
            for (Reunion reunion : reunions) {
                User organisateur = userDao.getUserById(reunion.organisateurId);
                List<ReunionParticipant> participants = participantDao.getParticipantsByReunion(reunion.id);
                
                List<String> participantNames = new ArrayList<>();
                for (ReunionParticipant p : participants) {
                    User user = userDao.getUserById(p.userId);
                    if (user != null) {
                        participantNames.add(user.fullName);
                    }
                }
                
                if (organisateur != null) {
                    allItems.add(new ReunionItem(
                        reunion.id,
                        reunion.titre,
                        reunion.dateHeure,
                        organisateur.fullName,
                        reunion.ordreDuJour,
                        reunion.statut,
                        participantNames
                    ));
                }
            }

            runOnUiThread(() -> {
                adapter.updateItems(allItems);
                updateEmptyState();
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
        List<ReunionItem> filtered = new ArrayList<>();
        
        if (query.isEmpty()) {
            filtered.addAll(allItems);
        } else {
            String lowerQuery = query.toLowerCase();
            for (ReunionItem item : allItems) {
                if (item.titre.toLowerCase().contains(lowerQuery) ||
                    item.organisateurName.toLowerCase().contains(lowerQuery) ||
                    item.statut.toLowerCase().contains(lowerQuery)) {
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
    static class ReunionItem {
        long id;
        String titre;
        long dateHeure;
        String organisateurName;
        String ordreDuJour;
        String statut;
        List<String> participantNames;

        ReunionItem(long id, String titre, long dateHeure, String organisateurName,
                   String ordreDuJour, String statut, List<String> participantNames) {
            this.id = id;
            this.titre = titre;
            this.dateHeure = dateHeure;
            this.organisateurName = organisateurName;
            this.ordreDuJour = ordreDuJour;
            this.statut = statut;
            this.participantNames = participantNames;
        }
    }
}

