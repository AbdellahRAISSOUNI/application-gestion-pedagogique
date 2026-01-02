package com.example.gestionbpedagogique;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.dao.EmploiTempsDao;
import com.example.gestionbpedagogique.database.dao.ModuleDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.EmploiTemps;
import com.example.gestionbpedagogique.database.entities.Module;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class EmploiTempsEditActivity extends AppCompatActivity {

    private Spinner professeurSpinner;
    private Spinner moduleSpinner;
    private Spinner jourSpinner;
    private TextInputEditText heureDebutInput;
    private TextInputEditText heureFinInput;
    private TextInputEditText salleInput;
    private Spinner typeCoursSpinner;
    private MaterialButton saveButton;
    private TextView titleText;
    
    private TextInputLayout heureDebutLayout;
    private TextInputLayout heureFinLayout;
    private TextInputLayout salleLayout;
    
    private long userId;
    private long emploiTempsId = -1; // -1 for new, >0 for edit
    private List<User> professeurs = new ArrayList<>();
    private List<Module> modules = new ArrayList<>();
    private UserSpinnerAdapter professeurAdapter;
    private ModuleSpinnerAdapter moduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emploi_temps_edit);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        emploiTempsId = getIntent().getLongExtra("EMPLOI_TEMPS_ID", -1);
        
        initializeViews();
        loadData();
        setupSaveButton();
        
        // Set title based on mode
        if (emploiTempsId == -1) {
            titleText.setText(getString(R.string.add_emploi_temps));
        } else {
            titleText.setText(getString(R.string.edit_emploi_temps));
        }
    }

    private void initializeViews() {
        titleText = findViewById(R.id.title_text);
        professeurSpinner = findViewById(R.id.professeur_spinner);
        moduleSpinner = findViewById(R.id.module_spinner);
        jourSpinner = findViewById(R.id.jour_spinner);
        heureDebutInput = findViewById(R.id.heure_debut_input);
        heureFinInput = findViewById(R.id.heure_fin_input);
        salleInput = findViewById(R.id.salle_input);
        typeCoursSpinner = findViewById(R.id.type_cours_spinner);
        saveButton = findViewById(R.id.save_button);
        
        heureDebutLayout = findViewById(R.id.heure_debut_layout);
        heureFinLayout = findViewById(R.id.heure_fin_layout);
        salleLayout = findViewById(R.id.salle_layout);
        
        // Setup jour spinner
        ArrayAdapter<CharSequence> jourAdapter = ArrayAdapter.createFromResource(
            this, R.array.jours_semaine, android.R.layout.simple_spinner_item);
        jourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jourSpinner.setAdapter(jourAdapter);
        
        // Setup type cours spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
            this, R.array.types_cours, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCoursSpinner.setAdapter(typeAdapter);
        
        // Setup time pickers
        setupTimePickers();
        
        // If editing, load existing data
        if (emploiTempsId != -1) {
            loadEmploiTempsData();
        }
    }
    
    private void setupTimePickers() {
        // Setup heure début picker
        heureDebutInput.setOnClickListener(v -> showTimePicker(true));
        heureDebutLayout.setEndIconOnClickListener(v -> showTimePicker(true));
        
        // Setup heure fin picker
        heureFinInput.setOnClickListener(v -> showTimePicker(false));
        heureFinLayout.setEndIconOnClickListener(v -> showTimePicker(false));
    }
    
    private void showTimePicker(boolean isHeureDebut) {
        // Parse current time or use default (8:00)
        int hour = 8;
        int minute = 0;
        
        String currentTime = isHeureDebut ? 
            (heureDebutInput.getText() != null ? heureDebutInput.getText().toString().trim() : "") :
            (heureFinInput.getText() != null ? heureFinInput.getText().toString().trim() : "");
        
        if (!TextUtils.isEmpty(currentTime) && currentTime.matches("\\d{2}:\\d{2}")) {
            String[] parts = currentTime.split(":");
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
        }
        
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            this,
            (view, selectedHour, selectedMinute) -> {
                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                if (isHeureDebut) {
                    heureDebutInput.setText(time);
                    heureDebutLayout.setError(null); // Clear any previous errors
                } else {
                    heureFinInput.setText(time);
                    heureFinLayout.setError(null); // Clear any previous errors
                }
            },
            hour,
            minute,
            true // 24-hour format
        );
        
        timePickerDialog.setTitle(isHeureDebut ? getString(R.string.heure_debut_hint) : getString(R.string.heure_fin_hint));
        timePickerDialog.show();
    }

    private void loadData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            UserDao userDao = db.userDao();
            ModuleDao moduleDao = db.moduleDao();
            
            // Get all professors (assistants and vacataires)
            professeurs = userDao.getUsersByType("PROFESSEUR_ASSISTANT");
            List<User> vacataires = userDao.getUsersByType("PROFESSEUR_VACATAIRE");
            professeurs.addAll(vacataires);
            
            // Get all modules
            modules = moduleDao.getAllModules();
            
            runOnUiThread(() -> {
                // Setup professeur spinner with custom adapter
                professeurAdapter = new UserSpinnerAdapter(this, 
                    android.R.layout.simple_spinner_item, professeurs);
                professeurAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                professeurSpinner.setAdapter(professeurAdapter);
                
                // Setup module spinner with custom adapter
                moduleAdapter = new ModuleSpinnerAdapter(this, 
                    android.R.layout.simple_spinner_item, modules);
                moduleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                moduleSpinner.setAdapter(moduleAdapter);
            });
        }).start();
    }

    private void loadEmploiTempsData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            EmploiTempsDao emploiTempsDao = db.emploiTempsDao();
            ModuleDao moduleDao = db.moduleDao();
            UserDao userDao = db.userDao();
            
            EmploiTemps et = emploiTempsDao.getEmploiTempsById(emploiTempsId);
            if (et != null) {
                Module module = moduleDao.getModuleById(et.moduleId);
                User professeur = userDao.getUserById(et.professeurId);
                
                runOnUiThread(() -> {
                    // Set professeur
                    for (int i = 0; i < professeurs.size(); i++) {
                        if (professeurs.get(i).id == et.professeurId) {
                            professeurSpinner.setSelection(i);
                            break;
                        }
                    }
                    
                    // Set module
                    for (int i = 0; i < modules.size(); i++) {
                        if (modules.get(i).id == et.moduleId) {
                            moduleSpinner.setSelection(i);
                            break;
                        }
                    }
                    
                    // Set jour
                    String[] jours = getResources().getStringArray(R.array.jours_semaine);
                    for (int i = 0; i < jours.length; i++) {
                        if (jours[i].equalsIgnoreCase(translateDay(et.jourSemaine))) {
                            jourSpinner.setSelection(i);
                            break;
                        }
                    }
                    
                    // Set heures
                    heureDebutInput.setText(et.heureDebut);
                    heureFinInput.setText(et.heureFin);
                    salleInput.setText(et.salle);
                    
                    // Set type cours
                    String[] types = getResources().getStringArray(R.array.types_cours);
                    for (int i = 0; i < types.length; i++) {
                        if (types[i].equals(et.typeCours)) {
                            typeCoursSpinner.setSelection(i);
                            break;
                        }
                    }
                });
            }
        }).start();
    }

    private String translateDay(String day) {
        switch (day.toUpperCase()) {
            case "LUNDI": return "Lundi";
            case "MARDI": return "Mardi";
            case "MERCREDI": return "Mercredi";
            case "JEUDI": return "Jeudi";
            case "VENDREDI": return "Vendredi";
            case "SAMEDI": return "Samedi";
            default: return day;
        }
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveEmploiTemps());
    }

    private void saveEmploiTemps() {
        // Clear errors
        heureDebutLayout.setError(null);
        heureFinLayout.setError(null);
        salleLayout.setError(null);
        
        // Validate
        if (professeurSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Veuillez sélectionner un professeur", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (moduleSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Veuillez sélectionner un module", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String heureDebut = heureDebutInput.getText() != null ? 
            heureDebutInput.getText().toString().trim() : "";
        String heureFin = heureFinInput.getText() != null ? 
            heureFinInput.getText().toString().trim() : "";
        String salle = salleInput.getText() != null ? 
            salleInput.getText().toString().trim() : "";
        
        if (TextUtils.isEmpty(heureDebut) || !heureDebut.matches("\\d{2}:\\d{2}")) {
            heureDebutLayout.setError("Format invalide (HH:mm)");
            return;
        }
        
        if (TextUtils.isEmpty(heureFin) || !heureFin.matches("\\d{2}:\\d{2}")) {
            heureFinLayout.setError("Format invalide (HH:mm)");
            return;
        }
        
        if (TextUtils.isEmpty(salle)) {
            salleLayout.setError("La salle est requise");
            return;
        }
        
        User selectedProfesseur = (User) professeurSpinner.getSelectedItem();
        Module selectedModule = (Module) moduleSpinner.getSelectedItem();
        String jour = getDayFromSpinner();
        String typeCours = (String) typeCoursSpinner.getSelectedItem();
        
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            EmploiTempsDao emploiTempsDao = db.emploiTempsDao();
            
            EmploiTemps et;
            if (emploiTempsId == -1) {
                // Create new
                et = new EmploiTemps(
                    selectedProfesseur.id,
                    selectedModule.id,
                    jour,
                    heureDebut,
                    heureFin,
                    salle,
                    typeCours
                );
                emploiTempsDao.insertEmploiTemps(et);
            } else {
                // Update existing
                et = emploiTempsDao.getEmploiTempsById(emploiTempsId);
                if (et != null) {
                    et.professeurId = selectedProfesseur.id;
                    et.moduleId = selectedModule.id;
                    et.jourSemaine = jour;
                    et.heureDebut = heureDebut;
                    et.heureFin = heureFin;
                    et.salle = salle;
                    et.typeCours = typeCours;
                    emploiTempsDao.updateEmploiTemps(et);
                }
            }
            
            runOnUiThread(() -> {
                Toast.makeText(this, 
                    emploiTempsId == -1 ? "Emploi du temps créé avec succès" : "Emploi du temps modifié avec succès",
                    Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    private String getDayFromSpinner() {
        String selected = (String) jourSpinner.getSelectedItem();
        switch (selected) {
            case "Lundi": return "LUNDI";
            case "Mardi": return "MARDI";
            case "Mercredi": return "MERCREDI";
            case "Jeudi": return "JEUDI";
            case "Vendredi": return "VENDREDI";
            case "Samedi": return "SAMEDI";
            default: return selected.toUpperCase();
        }
    }
}