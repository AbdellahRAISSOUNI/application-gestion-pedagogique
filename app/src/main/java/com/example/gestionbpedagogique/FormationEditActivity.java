package com.example.gestionbpedagogique;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.dao.FormationDao;
import com.example.gestionbpedagogique.database.dao.ModuleDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.Formation;
import com.example.gestionbpedagogique.database.entities.Module;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class FormationEditActivity extends AppCompatActivity {

    private TextInputEditText titleInput;
    private TextInputEditText descriptionInput;
    private Spinner typeSpinner;
    private Spinner cycleSpinner;
    private TextInputLayout titleLayout;
    private TextInputLayout descriptionLayout;
    private MaterialButton saveButton;
    private MaterialButton approveButton;
    private MaterialButton refuseButton;
    private TextView titleText;
    private RecyclerView modulesRecyclerView;
    private MaterialButton addModuleButton;
    
    private long userId;
    private String userType;
    private long formationId = -1; // -1 for new, >0 for edit
    private Formation currentFormation;
    private List<Module> modules = new ArrayList<>();
    private ModuleAdapter moduleAdapter;
    private List<User> allProfesseurs = new ArrayList<>();
    private String selectedType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formation_edit);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        formationId = getIntent().getLongExtra("FORMATION_ID", -1);
        
        initializeViews();
        loadData();
        setupButtons();
        setupTypeSpinner();
        
        // Set title based on mode
        if (formationId == -1) {
            titleText.setText(getString(R.string.add_formation));
        } else {
            titleText.setText(getString(R.string.edit_formation));
        }
    }

    private void initializeViews() {
        titleText = findViewById(R.id.title_text);
        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.description_input);
        typeSpinner = findViewById(R.id.type_spinner);
        cycleSpinner = findViewById(R.id.cycle_spinner);
        titleLayout = findViewById(R.id.title_layout);
        descriptionLayout = findViewById(R.id.description_layout);
        saveButton = findViewById(R.id.save_button);
        approveButton = findViewById(R.id.approve_button);
        refuseButton = findViewById(R.id.refuse_button);
        modulesRecyclerView = findViewById(R.id.modules_recycler_view);
        addModuleButton = findViewById(R.id.add_module_button);
        
        // Setup modules RecyclerView
        modulesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        moduleAdapter = new ModuleAdapter(modules, this);
        modulesRecyclerView.setAdapter(moduleAdapter);
        
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
    }
    
    private void setupTypeSpinner() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
            this, R.array.formation_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        
        typeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();
                updateCycleSpinner();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }
    
    private void updateCycleSpinner() {
        int arrayResId;
        if ("INITIALE".equals(selectedType)) {
            arrayResId = R.array.formation_cycles_initiale;
        } else if ("CONTINUE".equals(selectedType)) {
            arrayResId = R.array.formation_cycles_continue;
        } else {
            return;
        }
        
        ArrayAdapter<CharSequence> cycleAdapter = ArrayAdapter.createFromResource(
            this, arrayResId, android.R.layout.simple_spinner_item);
        cycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cycleSpinner.setAdapter(cycleAdapter);
    }

    private void loadData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            UserDao userDao = db.userDao();
            FormationDao formationDao = db.formationDao();
            ModuleDao moduleDao = db.moduleDao();
            
            // Get user type
            User currentUser = userDao.getUserById(userId);
            if (currentUser != null) {
                userType = currentUser.userType;
            }
            
            // Only Admin can access
            if (!"ADMIN".equals(userType)) {
                runOnUiThread(() -> {
                    finish();
                });
                return;
            }
            
            // Get all professors for module assignment
            List<User> assistants = userDao.getUsersByType("PROFESSEUR_ASSISTANT");
            List<User> vacataires = userDao.getUsersByType("PROFESSEUR_VACATAIRE");
            allProfesseurs.clear();
            allProfesseurs.addAll(assistants);
            allProfesseurs.addAll(vacataires);
            
            // If editing, load existing data
            if (formationId != -1) {
                currentFormation = formationDao.getFormationById(formationId);
                if (currentFormation != null) {
                    modules = moduleDao.getModulesByFormation(formationId);
                }
            }
            
            runOnUiThread(() -> {
                if (formationId != -1 && currentFormation != null) {
                    // Load existing formation data
                    titleInput.setText(currentFormation.title);
                    descriptionInput.setText(currentFormation.description);
                    
                    // Set type spinner
                    int typePosition = 0;
                    if ("CONTINUE".equals(currentFormation.type)) {
                        typePosition = 1;
                    }
                    typeSpinner.setSelection(typePosition);
                    selectedType = currentFormation.type;
                    updateCycleSpinner();
                    
                    // Set cycle spinner after adapter is updated
                    // Wait a bit for adapter to be set, then set selection
                    cycleSpinner.post(() -> {
                        if (cycleSpinner.getAdapter() != null) {
                            String[] cycles = getResources().getStringArray(
                                "INITIALE".equals(selectedType) ? R.array.formation_cycles_initiale : R.array.formation_cycles_continue);
                            for (int i = 0; i < cycles.length; i++) {
                                if (cycles[i].equals(currentFormation.cycle)) {
                                    cycleSpinner.setSelection(i);
                                    break;
                                }
                            }
                        }
                    });
                    
                    updateButtonVisibility(currentFormation.status);
                } else {
                    // New formation
                    updateButtonVisibility("EN_ATTENTE");
                }
                
                // Update modules adapter
                moduleAdapter.updateModules(modules);
            });
        }).start();
    }
    
    private void updateButtonVisibility(String status) {
        if (formationId == -1) {
            // New formation - show save button only
            saveButton.setVisibility(View.VISIBLE);
            approveButton.setVisibility(View.GONE);
            refuseButton.setVisibility(View.GONE);
        } else {
            // Existing formation
            if ("EN_ATTENTE".equals(status)) {
                saveButton.setVisibility(View.VISIBLE);
                approveButton.setVisibility(View.VISIBLE);
                refuseButton.setVisibility(View.VISIBLE);
            } else {
                // Already approved or refused - can only view
                saveButton.setVisibility(View.GONE);
                approveButton.setVisibility(View.GONE);
                refuseButton.setVisibility(View.GONE);
            }
        }
    }

    private void setupButtons() {
        saveButton.setOnClickListener(v -> saveFormation());
        approveButton.setOnClickListener(v -> approveFormation());
        refuseButton.setOnClickListener(v -> refuseFormation());
        addModuleButton.setOnClickListener(v -> showModuleDialog(-1));
    }
    
    private void saveFormation() {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String type = typeSpinner.getSelectedItem().toString();
        String cycle = cycleSpinner.getSelectedItem().toString();
        
        // Validation
        if (TextUtils.isEmpty(title)) {
            titleLayout.setError(getString(R.string.formation_title_required));
            return;
        }
        titleLayout.setError(null);
        
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            FormationDao formationDao = db.formationDao();
            
            if (formationId == -1) {
                // Create new formation
                Formation formation = new Formation(type, cycle, title, description, userId);
                long newId = formationDao.insertFormation(formation);
                
                // Save modules
                ModuleDao moduleDao = db.moduleDao();
                for (Module module : modules) {
                    module.formationId = newId;
                    moduleDao.insertModule(module);
                }
                
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.formation_created_success), Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                // Update existing formation
                if (currentFormation != null) {
                    currentFormation.title = title;
                    currentFormation.description = description;
                    currentFormation.type = type;
                    currentFormation.cycle = cycle;
                    
                    formationDao.updateFormation(currentFormation);
                    
                    // Update modules
                    ModuleDao moduleDao = db.moduleDao();
                    // Delete old modules
                    List<Module> oldModules = moduleDao.getModulesByFormation(formationId);
                    for (Module oldModule : oldModules) {
                        moduleDao.deleteModule(oldModule);
                    }
                    // Insert updated modules
                    for (Module module : modules) {
                        module.formationId = formationId;
                        if (module.id > 0) {
                            // Existing module - update
                            moduleDao.insertModule(module);
                        } else {
                            // New module - insert
                            moduleDao.insertModule(module);
                        }
                    }
                    
                    runOnUiThread(() -> {
                        Toast.makeText(this, getString(R.string.formation_updated_success), Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }
        }).start();
    }
    
    private void approveFormation() {
        if (formationId == -1 || currentFormation == null) return;
        
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            FormationDao formationDao = db.formationDao();
            
            currentFormation.status = "APPROUVEE";
            currentFormation.validatedDate = System.currentTimeMillis();
            formationDao.updateFormation(currentFormation);
            
            runOnUiThread(() -> {
                Toast.makeText(this, getString(R.string.formation_approved_success), Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
    
    private void refuseFormation() {
        if (formationId == -1 || currentFormation == null) return;
        
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            FormationDao formationDao = db.formationDao();
            
            currentFormation.status = "REFUSEE";
            currentFormation.validatedDate = System.currentTimeMillis();
            formationDao.updateFormation(currentFormation);
            
            runOnUiThread(() -> {
                Toast.makeText(this, getString(R.string.formation_refused_success), Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
    
    private void showModuleDialog(long moduleId) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_module, null);
        
        TextInputEditText codeInput = dialogView.findViewById(R.id.code_input);
        TextInputEditText nomInput = dialogView.findViewById(R.id.nom_input);
        TextInputEditText volumeInput = dialogView.findViewById(R.id.volume_input);
        Spinner professeurSpinner = dialogView.findViewById(R.id.professeur_spinner);
        TextInputLayout codeLayout = dialogView.findViewById(R.id.code_layout);
        TextInputLayout nomLayout = dialogView.findViewById(R.id.nom_layout);
        TextInputLayout volumeLayout = dialogView.findViewById(R.id.volume_layout);
        
        // Setup professeur spinner
        UserSpinnerAdapter professeurAdapter = new UserSpinnerAdapter(this, android.R.layout.simple_spinner_item, allProfesseurs);
        professeurAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professeurSpinner.setAdapter(professeurAdapter);
        
        Module moduleToEdit = null;
        if (moduleId >= 0) {
            for (Module m : modules) {
                if (m.id == moduleId) {
                    moduleToEdit = m;
                    break;
                }
            }
        }
        
        final Module finalModuleToEdit = moduleToEdit; // Make final for lambda
        
        if (finalModuleToEdit != null) {
            codeInput.setText(finalModuleToEdit.code);
            nomInput.setText(finalModuleToEdit.nom);
            volumeInput.setText(String.valueOf(finalModuleToEdit.volumeHoraire));
            if (finalModuleToEdit.professeurId != null) {
                for (int i = 0; i < allProfesseurs.size(); i++) {
                    if (allProfesseurs.get(i).id == finalModuleToEdit.professeurId) {
                        professeurSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }
        
        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle(moduleId >= 0 ? getString(R.string.edit_module) : getString(R.string.add_module))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.save_button), (d, which) -> {
                String code = codeInput.getText().toString().trim();
                String nom = nomInput.getText().toString().trim();
                String volumeStr = volumeInput.getText().toString().trim();
                
                boolean hasError = false;
                if (TextUtils.isEmpty(code)) {
                    codeLayout.setError(getString(R.string.module_code_required));
                    hasError = true;
                }
                if (TextUtils.isEmpty(nom)) {
                    nomLayout.setError(getString(R.string.module_nom_required));
                    hasError = true;
                }
                if (TextUtils.isEmpty(volumeStr)) {
                    volumeLayout.setError(getString(R.string.module_volume_required));
                    hasError = true;
                }
                
                if (hasError) return;
                
                int volume = Integer.parseInt(volumeStr);
                User selectedProfesseur = null;
                if (professeurSpinner.getSelectedItemPosition() >= 0) {
                    selectedProfesseur = allProfesseurs.get(professeurSpinner.getSelectedItemPosition());
                }
                
                if (finalModuleToEdit != null) {
                    // Update existing module
                    finalModuleToEdit.code = code;
                    finalModuleToEdit.nom = nom;
                    finalModuleToEdit.volumeHoraire = volume;
                    finalModuleToEdit.professeurId = selectedProfesseur != null ? selectedProfesseur.id : null;
                } else {
                    // Create new module
                    Module newModule = new Module(code, nom, volume, formationId);
                    newModule.professeurId = selectedProfesseur != null ? selectedProfesseur.id : null;
                    modules.add(newModule);
                }
                
                moduleAdapter.updateModules(modules);
                Toast.makeText(this, 
                    finalModuleToEdit != null ? getString(R.string.module_updated_success) : getString(R.string.module_added_success),
                    Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton(getString(android.R.string.cancel), null)
            .create();
        
        if (moduleId >= 0 && finalModuleToEdit != null) {
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.delete_module), (d, which) -> {
                modules.remove(finalModuleToEdit);
                moduleAdapter.updateModules(modules);
                Toast.makeText(this, getString(R.string.module_deleted_success), Toast.LENGTH_SHORT).show();
            });
        }
        
        dialog.show();
    }
    
    // Inner class for ModuleAdapter
    private static class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
        private List<Module> modules;
        private FormationEditActivity activity;
        
        ModuleAdapter(List<Module> modules, FormationEditActivity activity) {
            this.modules = modules;
            this.activity = activity;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Module module = modules.get(position);
            holder.bind(module, activity);
        }
        
        @Override
        public int getItemCount() {
            return modules.size();
        }
        
        void updateModules(List<Module> newModules) {
            this.modules = newModules;
            notifyDataSetChanged();
        }
        
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView codeText;
            TextView nomText;
            TextView volumeText;
            TextView professeurText;
            
            ViewHolder(View itemView) {
                super(itemView);
                codeText = itemView.findViewById(R.id.code_text);
                nomText = itemView.findViewById(R.id.nom_text);
                volumeText = itemView.findViewById(R.id.volume_text);
                professeurText = itemView.findViewById(R.id.professeur_text);
            }
            
            void bind(Module module, FormationEditActivity activity) {
                codeText.setText(module.code);
                nomText.setText(module.nom);
                volumeText.setText(activity.getString(R.string.module_volume_hint) + ": " + module.volumeHoraire + "h");
                
                if (module.professeurId != null) {
                    new Thread(() -> {
                        AppDatabase db = AppDatabase.getDatabase(activity);
                        User professeur = db.userDao().getUserById(module.professeurId);
                        activity.runOnUiThread(() -> {
                            if (professeur != null) {
                                professeurText.setText(professeur.fullName);
                            } else {
                                professeurText.setText("-");
                            }
                        });
                    }).start();
                } else {
                    professeurText.setText("-");
                }
                
                itemView.setOnClickListener(v -> activity.showModuleDialog(module.id));
            }
        }
    }
}
