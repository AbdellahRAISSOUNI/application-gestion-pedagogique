package com.example.gestionbpedagogique;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.dao.ReunionDao;
import com.example.gestionbpedagogique.database.dao.ReunionParticipantDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.Reunion;
import com.example.gestionbpedagogique.database.entities.ReunionParticipant;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReunionEditActivity extends AppCompatActivity {

    private TextInputEditText titreInput;
    private TextInputEditText dateHeureInput;
    private TextInputEditText ordreDuJourInput;
    private TextInputLayout titreLayout;
    private TextInputLayout dateHeureLayout;
    private TextInputLayout ordreDuJourLayout;
    private MaterialButton saveButton;
    private TextView titleText;
    private LinearLayout participantsContainer;
    
    private long userId;
    private long reunionId = -1; // -1 for new, >0 for edit
    private List<User> allProfesseurs = new ArrayList<>();
    private List<CheckBox> participantCheckboxes = new ArrayList<>();
    private Calendar selectedDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reunion_edit);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        reunionId = getIntent().getLongExtra("REUNION_ID", -1);
        
        initializeViews();
        loadData();
        setupDatePicker();
        setupSaveButton();
        
        // Set title based on mode
        if (reunionId == -1) {
            titleText.setText(getString(R.string.add_reunion));
        } else {
            titleText.setText(getString(R.string.edit_reunion));
        }
    }

    private void initializeViews() {
        titleText = findViewById(R.id.title_text);
        titreInput = findViewById(R.id.titre_input);
        dateHeureInput = findViewById(R.id.date_heure_input);
        ordreDuJourInput = findViewById(R.id.ordre_du_jour_input);
        titreLayout = findViewById(R.id.titre_layout);
        dateHeureLayout = findViewById(R.id.date_heure_layout);
        ordreDuJourLayout = findViewById(R.id.ordre_du_jour_layout);
        saveButton = findViewById(R.id.save_button);
        participantsContainer = findViewById(R.id.participants_container);
        
        // Make date/heure input clickable but not editable
        dateHeureInput.setFocusable(false);
        dateHeureInput.setClickable(true);
    }
    
    private void setupDatePicker() {
        dateHeureInput.setOnClickListener(v -> showDateTimePicker());
        dateHeureLayout.setEndIconOnClickListener(v -> showDateTimePicker());
    }
    
    private void showDateTimePicker() {
        // First show date picker
        Calendar calendar = selectedDateTime;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // Then show time picker
                showTimePicker(calendar);
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    
    private void showTimePicker(Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            this,
            (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                selectedDateTime = calendar;
                updateDateTimeDisplay();
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24-hour format
        );
        timePickerDialog.setTitle(getString(R.string.reunion_date_heure_hint));
        timePickerDialog.show();
    }
    
    private void updateDateTimeDisplay() {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.FRENCH);
        dateHeureInput.setText(dateFormat.format(selectedDateTime.getTime()));
        dateHeureLayout.setError(null);
    }

    private void loadData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            UserDao userDao = db.userDao();
            
            // Get all professors (assistants and vacataires)
            List<User> assistants = userDao.getUsersByType("PROFESSEUR_ASSISTANT");
            List<User> vacataires = userDao.getUsersByType("PROFESSEUR_VACATAIRE");
            
            allProfesseurs.clear();
            allProfesseurs.addAll(assistants);
            allProfesseurs.addAll(vacataires);
            
            runOnUiThread(() -> {
                setupParticipantsCheckboxes();
                
                // If editing, load existing data
                if (reunionId != -1) {
                    loadReunionData();
                }
            });
        }).start();
    }
    
    private void setupParticipantsCheckboxes() {
        participantsContainer.removeAllViews();
        participantCheckboxes.clear();
        
        for (User professeur : allProfesseurs) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(professeur.fullName);
            checkBox.setTag(professeur.id);
            checkBox.setTextSize(16);
            checkBox.setPadding(8, 8, 8, 8);
            participantCheckboxes.add(checkBox);
            participantsContainer.addView(checkBox);
        }
    }

    private void loadReunionData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            ReunionDao reunionDao = db.reunionDao();
            ReunionParticipantDao participantDao = db.reunionParticipantDao();
            
            Reunion reunion = reunionDao.getReunionById(reunionId);
            if (reunion != null) {
                List<ReunionParticipant> participants = participantDao.getParticipantsByReunion(reunionId);
                
                runOnUiThread(() -> {
                    titreInput.setText(reunion.titre);
                    ordreDuJourInput.setText(reunion.ordreDuJour);
                    
                    // Set date/time
                    selectedDateTime.setTimeInMillis(reunion.dateHeure);
                    updateDateTimeDisplay();
                    
                    // Check selected participants
                    for (ReunionParticipant p : participants) {
                        for (CheckBox checkBox : participantCheckboxes) {
                            if ((Long) checkBox.getTag() == p.userId) {
                                checkBox.setChecked(true);
                                break;
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveReunion());
    }

    private void saveReunion() {
        // Clear errors
        titreLayout.setError(null);
        dateHeureLayout.setError(null);
        ordreDuJourLayout.setError(null);
        
        // Validate
        String titre = titreInput.getText() != null ? titreInput.getText().toString().trim() : "";
        String ordreDuJour = ordreDuJourInput.getText() != null ? ordreDuJourInput.getText().toString().trim() : "";
        String dateHeureStr = dateHeureInput.getText() != null ? dateHeureInput.getText().toString().trim() : "";
        
        boolean isValid = true;
        
        if (TextUtils.isEmpty(titre)) {
            titreLayout.setError(getString(R.string.reunion_titre_required));
            isValid = false;
        }
        
        if (TextUtils.isEmpty(dateHeureStr)) {
            dateHeureLayout.setError(getString(R.string.reunion_date_required));
            isValid = false;
        }
        
        if (TextUtils.isEmpty(ordreDuJour)) {
            ordreDuJourLayout.setError(getString(R.string.reunion_ordre_du_jour_required));
            isValid = false;
        }
        
        // Check if at least one participant is selected
        boolean hasSelectedParticipant = false;
        for (CheckBox checkBox : participantCheckboxes) {
            if (checkBox.isChecked()) {
                hasSelectedParticipant = true;
                break;
            }
        }
        
        if (!hasSelectedParticipant) {
            Toast.makeText(this, getString(R.string.reunion_participants_required), Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        
        if (!isValid) {
            return;
        }
        
        // Get selected participants
        List<Long> selectedParticipantIds = new ArrayList<>();
        for (CheckBox checkBox : participantCheckboxes) {
            if (checkBox.isChecked()) {
                selectedParticipantIds.add((Long) checkBox.getTag());
            }
        }
        
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            ReunionDao reunionDao = db.reunionDao();
            ReunionParticipantDao participantDao = db.reunionParticipantDao();
            
            long timestamp = selectedDateTime.getTimeInMillis();
            
            boolean isNew = (reunionId == -1);
            Reunion reunion;
            if (isNew) {
                // Create new
                reunion = new Reunion(titre, timestamp, userId, ordreDuJour);
                reunionId = reunionDao.insertReunion(reunion);
                reunion.id = reunionId;
            } else {
                // Update existing
                reunion = reunionDao.getReunionById(reunionId);
                if (reunion != null) {
                    reunion.titre = titre;
                    reunion.dateHeure = timestamp;
                    reunion.ordreDuJour = ordreDuJour;
                    reunionDao.updateReunion(reunion);
                }
                
                // Delete existing participants
                participantDao.deleteParticipantsByReunion(reunionId);
            }
            
            // Insert new participants (avoid duplicates)
            java.util.Set<Long> insertedParticipantIds = new java.util.HashSet<>();
            for (Long participantId : selectedParticipantIds) {
                // Avoid inserting duplicate participants
                if (!insertedParticipantIds.contains(participantId)) {
                    ReunionParticipant participant = new ReunionParticipant(reunionId, participantId);
                    participantDao.insertParticipant(participant);
                    insertedParticipantIds.add(participantId);
                }
            }
            
            final boolean isNewFinal = isNew;
            runOnUiThread(() -> {
                Toast.makeText(this, 
                    isNewFinal ? getString(R.string.reunion_created_success) : getString(R.string.reunion_updated_success),
                    Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}

