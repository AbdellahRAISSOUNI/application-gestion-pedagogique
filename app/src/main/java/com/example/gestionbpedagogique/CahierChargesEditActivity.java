package com.example.gestionbpedagogique;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestionbpedagogique.database.AppDatabase;
import com.example.gestionbpedagogique.database.dao.CahierChargesDao;
import com.example.gestionbpedagogique.database.dao.FormationDao;
import com.example.gestionbpedagogique.database.dao.UserDao;
import com.example.gestionbpedagogique.database.entities.CahierCharges;
import com.example.gestionbpedagogique.database.entities.Formation;
import com.example.gestionbpedagogique.database.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class CahierChargesEditActivity extends AppCompatActivity {

    private TextInputEditText titreInput;
    private Spinner typeSpinner;
    private Spinner formationSpinner;
    private TextInputEditText filePathInput;
    private TextInputLayout titreLayout;
    private TextInputLayout filePathLayout;
    private MaterialButton saveButton;
    private MaterialButton sendButton;
    private MaterialButton approveButton;
    private MaterialButton refuseButton;
    private TextView titleText;
    
    private long userId;
    private String userType;
    private long cahierChargesId = -1; // -1 for new, >0 for edit
    private List<Formation> formations = new ArrayList<>();
    private Uri selectedFileUri;
    private String selectedFilePath;
    private FormationSpinnerAdapter formationAdapter;
    
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cahier_charges_edit);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getLongExtra("USER_ID", -1);
        cahierChargesId = getIntent().getLongExtra("CAHIER_CHARGES_ID", -1);
        
        // Setup file picker launcher
        filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        try {
                            // Take persistent permission for the URI
                            getContentResolver().takePersistableUriPermission(
                                uri, 
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                            
                            selectedFileUri = uri;
                            // Get file path (simplified - in production, copy file to app storage)
                            selectedFilePath = uri.toString();
                            String fileName = getFileName(uri);
                            if (fileName != null && !fileName.isEmpty()) {
                                filePathInput.setText(fileName);
                                filePathLayout.setError(null);
                            } else {
                                filePathInput.setText(uri.toString());
                            }
                        } catch (SecurityException e) {
                            Toast.makeText(this, "Erreur d'accès au fichier", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Aucun fichier sélectionné", Toast.LENGTH_SHORT).show();
                    }
                } else if (result.getResultCode() == RESULT_CANCELED) {
                    // User cancelled, do nothing
                } else {
                    Toast.makeText(this, "Erreur lors de la sélection du fichier", Toast.LENGTH_SHORT).show();
                }
            }
        );
        
        initializeViews();
        loadData();
        setupButtons();
        
        // Set title based on mode
        if (cahierChargesId == -1) {
            titleText.setText(getString(R.string.add_cahier_charges));
        } else {
            titleText.setText(getString(R.string.edit_cahier_charges));
        }
    }

    private void initializeViews() {
        titleText = findViewById(R.id.title_text);
        titreInput = findViewById(R.id.titre_input);
        typeSpinner = findViewById(R.id.type_spinner);
        formationSpinner = findViewById(R.id.formation_spinner);
        filePathInput = findViewById(R.id.file_path_input);
        titreLayout = findViewById(R.id.titre_layout);
        filePathLayout = findViewById(R.id.file_path_layout);
        saveButton = findViewById(R.id.save_button);
        sendButton = findViewById(R.id.send_button);
        approveButton = findViewById(R.id.approve_button);
        refuseButton = findViewById(R.id.refuse_button);
        
        // Setup type spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
            this, R.array.cahier_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        
        // Make file path input clickable but not editable
        filePathInput.setFocusable(false);
        filePathInput.setClickable(true);
        filePathInput.setOnClickListener(v -> openFilePicker());
        
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
    }
    
    private void loadData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            FormationDao formationDao = db.formationDao();
            UserDao userDao = db.userDao();
            
            // Get user type
            User currentUser = userDao.getUserById(userId);
            String loadedUserType = null;
            if (currentUser != null) {
                loadedUserType = currentUser.userType;
                userType = loadedUserType; // Set instance variable
            }
            
            // Get all formations
            formations = formationDao.getAllFormations();
            
            // Store userType in a final variable for use in runOnUiThread
            final String finalUserType = loadedUserType;
            
            runOnUiThread(() -> {
                // Setup formation spinner with custom adapter
                formationAdapter = new FormationSpinnerAdapter(this, formations);
                formationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                formationSpinner.setAdapter(formationAdapter);
                
                // If editing, load existing data
                if (cahierChargesId != -1) {
                    loadCahierChargesData();
                } else {
                    // For new cahier, show buttons based on user type
                    // Professeur Assistant can create (BROUILLON status)
                    if (finalUserType != null) {
                        updateButtonVisibility("BROUILLON");
                    }
                }
            });
        }).start();
    }
    
    private void loadCahierChargesData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            CahierChargesDao cahierChargesDao = db.cahierChargesDao();
            
            CahierCharges cc = cahierChargesDao.getCahierChargesById(cahierChargesId);
            if (cc != null) {
                runOnUiThread(() -> {
                    titreInput.setText(cc.titre);
                    
                    // Set type
                    String[] types = getResources().getStringArray(R.array.cahier_types);
                    for (int i = 0; i < types.length; i++) {
                        if ((cc.type.equals("FORMATION_INITIALE") && i == 0) ||
                            (cc.type.equals("FORMATION_CONTINUE") && i == 1)) {
                            typeSpinner.setSelection(i);
                            break;
                        }
                    }
                    
                    // Set formation
                    if (cc.formationId != null) {
                        for (int i = 0; i < formations.size(); i++) {
                            if (formations.get(i).id == cc.formationId) {
                                formationSpinner.setSelection(i);
                                break;
                            }
                        }
                    }
                    
                    // Set file path
                    if (cc.filePath != null && !cc.filePath.isEmpty()) {
                        filePathInput.setText(cc.filePath);
                        selectedFilePath = cc.filePath;
                    }
                    
                    // Update button visibility based on statut and user type
                    updateButtonVisibility(cc.statut);
                });
            }
        }).start();
    }
    
    private void updateButtonVisibility(String statut) {
        // Hide all buttons first
        saveButton.setVisibility(View.GONE);
        sendButton.setVisibility(View.GONE);
        approveButton.setVisibility(View.GONE);
        refuseButton.setVisibility(View.GONE);
        
        // Wait for userType to be loaded
        if (userType == null) {
            return;
        }
        
        if ("PROFESSEUR_ASSISTANT".equals(userType)) {
            // Professeur Assistant can save (BROUILLON) or send (ENVOYE)
            // For new cahier (statut == null) or existing brouillon, show both buttons
            if (statut == null || "BROUILLON".equals(statut)) {
                saveButton.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
            }
        } else if ("ADMIN".equals(userType)) {
            // Admin can approve or refuse if status is ENVOYE
            if ("ENVOYE".equals(statut)) {
                approveButton.setVisibility(View.VISIBLE);
                refuseButton.setVisibility(View.VISIBLE);
            }
        }
    }
    
    private void setupButtons() {
        saveButton.setOnClickListener(v -> saveCahierCharges("BROUILLON"));
        sendButton.setOnClickListener(v -> saveCahierCharges("ENVOYE"));
        approveButton.setOnClickListener(v -> updateStatut("APPROUVE"));
        refuseButton.setOnClickListener(v -> updateStatut("REFUSE"));
    }
    
    private void openFilePicker() {
        // Primary intent: ACTION_OPEN_DOCUMENT (works better on modern Android)
        Intent openDocumentIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openDocumentIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openDocumentIntent.setType("*/*");
        
        // Support multiple file types
        String[] mimeTypes = {
            "application/pdf",
            "image/*",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        };
        openDocumentIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        
        // Fallback intent: ACTION_GET_CONTENT (for compatibility)
        Intent getContentIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getContentIntent.setType("*/*");
        getContentIntent.addCategory(Intent.CATEGORY_OPENABLE);
        getContentIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        
        // Create chooser with both intents
        Intent chooserIntent = Intent.createChooser(openDocumentIntent, getString(R.string.cahier_select_file));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{getContentIntent});
        
        try {
            filePickerLauncher.launch(chooserIntent);
        } catch (Exception e) {
            // If everything fails, try the simplest approach
            Intent simpleIntent = new Intent(Intent.ACTION_GET_CONTENT);
            simpleIntent.setType("*/*");
            simpleIntent.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                filePickerLauncher.launch(Intent.createChooser(simpleIntent, getString(R.string.cahier_select_file)));
            } catch (Exception ex) {
                Toast.makeText(this, "Impossible d'ouvrir le sélecteur de fichiers. Assurez-vous d'avoir un gestionnaire de fichiers installé.", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private String getFileName(Uri uri) {
        String result = null;
        try {
            if (uri != null && "content".equals(uri.getScheme())) {
                try (android.database.Cursor cursor = getContentResolver().query(
                    uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                        if (nameIndex >= 0) {
                            result = cursor.getString(nameIndex);
                        }
                    }
                }
            }
            
            if (result == null && uri != null) {
                String path = uri.getPath();
                if (path != null) {
                    int cut = path.lastIndexOf('/');
                    if (cut != -1) {
                        result = path.substring(cut + 1);
                    } else {
                        result = path;
                    }
                }
            }
            
            // If still null, use a default name
            if (result == null || result.isEmpty()) {
                result = "fichier_selectionne";
            }
        } catch (Exception e) {
            result = "fichier_selectionne";
        }
        return result;
    }
    
    private void saveCahierCharges(String statut) {
        // Clear errors
        titreLayout.setError(null);
        filePathLayout.setError(null);
        
        // Validate
        String titre = titreInput.getText() != null ? titreInput.getText().toString().trim() : "";
        if (TextUtils.isEmpty(titre)) {
            titreLayout.setError(getString(R.string.cahier_titre_required));
            return;
        }
        
        if (selectedFilePath == null || selectedFilePath.isEmpty()) {
            filePathLayout.setError(getString(R.string.cahier_file_required));
            return;
        }
        
        String type = typeSpinner.getSelectedItemPosition() == 0 ? "FORMATION_INITIALE" : "FORMATION_CONTINUE";
        Formation selectedFormation = (Formation) formationSpinner.getSelectedItem();
        Long formationId = (selectedFormation != null) ? selectedFormation.id : null;
        
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            CahierChargesDao cahierChargesDao = db.cahierChargesDao();
            
            CahierCharges cc;
            if (cahierChargesId == -1) {
                // Create new
                cc = new CahierCharges(titre, type, userId);
                cc.filePath = selectedFilePath;
                cc.formationId = formationId;
                cc.statut = statut;
                cahierChargesDao.insertCahierCharges(cc);
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.cahier_created_success), Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                // Update existing
                cc = cahierChargesDao.getCahierChargesById(cahierChargesId);
                if (cc != null) {
                    cc.titre = titre;
                    cc.type = type;
                    cc.filePath = selectedFilePath;
                    cc.formationId = formationId;
                    cc.statut = statut;
                    cahierChargesDao.updateCahierCharges(cc);
                    runOnUiThread(() -> {
                        if ("ENVOYE".equals(statut)) {
                            Toast.makeText(this, getString(R.string.cahier_sent_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getString(R.string.cahier_updated_success), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    });
                }
            }
        }).start();
    }
    
    private void updateStatut(String newStatut) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            CahierChargesDao cahierChargesDao = db.cahierChargesDao();
            
            CahierCharges cc = cahierChargesDao.getCahierChargesById(cahierChargesId);
            if (cc != null) {
                cc.statut = newStatut;
                cc.dateValidation = System.currentTimeMillis();
                cahierChargesDao.updateCahierCharges(cc);
                runOnUiThread(() -> {
                    if ("APPROUVE".equals(newStatut)) {
                        Toast.makeText(this, getString(R.string.cahier_approved_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.cahier_refused_success), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                });
            }
        }).start();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
