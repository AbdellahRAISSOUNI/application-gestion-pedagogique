# Base de Données - Configuration et Utilisation

## ✅ Base de données créée avec succès !

La base de données Room a été configurée et est prête à être utilisée.

## 📊 Structure de la Base de Données

### Entités créées :

1. **User** - Utilisateurs du système
   - Types: ADMIN, PROFESSEUR_ASSISTANT, PROFESSEUR_VACATAIRE
   
2. **Formation** - Formations (initiale et continue)
   - Types: INITIALE (PREPARATOIRE, INGENIEUR, MASTER) ou CONTINUE (DCA, DCESS)
   
3. **Module** - Modules de cours
   - Liés aux formations
   - Peuvent être assignés à des professeurs
   
4. **CahierCharges** - Cahiers de charges
   - Créés par les professeurs assistants
   - Statuts: BROUILLON, ENVOYE, APPROUVE, REFUSE
   
5. **Reunion** - Réunions pédagogiques
   - Planifiées par l'admin
   - Statuts: PLANIFIEE, EN_COURS, TERMINEE
   
6. **ReunionParticipant** - Participants aux réunions
   - Table de liaison entre réunions et utilisateurs
   
7. **EmploiTemps** - Emplois du temps
   - Liés aux professeurs et modules
   - Contient jour, heures, salle, type de cours

## 🔧 Accès à la Base de Données

### Dans votre code :

```java
// Obtenir l'instance de la base de données
AppDatabase db = AppDatabase.getDatabase(context);

// Accéder aux DAOs
UserDao userDao = db.userDao();
FormationDao formationDao = db.formationDao();
// etc...
```

## 👥 Utilisateurs de Test

La base de données est initialisée automatiquement avec des utilisateurs de test :

1. **Admin**
   - Username: `admin`
   - Password: `admin123`
   - Type: ADMIN

2. **Professeur Assistant 1**
   - Username: `prof.assistant1`
   - Password: `prof123`
   - Type: PROFESSEUR_ASSISTANT

3. **Professeur Assistant 2**
   - Username: `prof.assistant2`
   - Password: `prof123`
   - Type: PROFESSEUR_ASSISTANT

4. **Professeur Vacataire**
   - Username: `prof.vacataire`
   - Password: `prof123`
   - Type: PROFESSEUR_VACATAIRE

## 📝 Données de Test Initialisées

- **Formations** :
  - Cycle Préparatoire
  - Cycle Ingénieur
  - Cycle Master
  - DCA (Formation continue)
  - DCESS (Formation continue)

- **Modules** :
  - Modules pour Cycle Préparatoire (MATH101, PHYS101, INFO101)
  - Modules pour Cycle Ingénieur (ALGO201, BD201, RESEAU201)

## 🚀 Utilisation

La base de données est automatiquement initialisée au démarrage de l'application via `GestionPedagogiqueApp`.

### Exemple d'utilisation dans une Activity :

```java
public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(this);
        
        // Vérifier un utilisateur
        User user = db.userDao().getUserByUsername("admin");
        if (user != null && user.password.equals("admin123")) {
            // Connexion réussie
        }
    }
}
```

## 📍 Emplacement de la Base de Données

La base de données SQLite est stockée dans :
```
/data/data/com.example.gestionbpedagogique/databases/gestion_pedagogique_db
```

## ⚠️ Notes Importantes

1. **Sécurité** : Les mots de passe sont stockés en clair dans cette version de développement. En production, utilisez un hashage (BCrypt, etc.).

2. **Threads** : La base de données est configurée pour permettre les requêtes sur le thread principal (`allowMainThreadQueries()`). En production, utilisez des threads d'arrière-plan (AsyncTask, Coroutines, RxJava, etc.).

3. **Migrations** : Actuellement configuré avec `fallbackToDestructiveMigration()` pour le développement. En production, créez des migrations appropriées.

## 🔍 Vérification

Pour vérifier que la base de données fonctionne :

1. Lancez l'application
2. La base de données sera créée automatiquement
3. Les données de test seront insérées au premier lancement

Vous pouvez maintenant utiliser la base de données dans votre application !
