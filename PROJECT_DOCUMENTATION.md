# Documentation du Projet - Gestion Pédagogique ENSA

**Dernière mise à jour :** 2024-12-19  
**Version :** 1.2

> ⚠️ **IMPORTANT :** Cette documentation doit être mise à jour à chaque ajout de fonctionnalité, modification d'architecture, ou changement important dans le projet.

## 📋 Table des Matières

1. [Vue d'ensemble](#vue-densemble)
2. [Architecture](#architecture)
3. [Base de Données](#base-de-données)
4. [Structure du Projet](#structure-du-projet)
5. [Fonctionnalités Implémentées](#fonctionnalités-implémentées)
6. [Types d'Utilisateurs](#types-dutilisateurs)
7. [Workflows](#workflows)
8. [Technologies Utilisées](#technologies-utilisées)
9. [Guide de Développement](#guide-de-développement)
10. [État du Projet](#état-du-projet)
11. [Changelog](#changelog)

---

## 🎯 Vue d'ensemble

### Description
Application Android de gestion pédagogique pour l'École Nationale des Sciences Appliquées (ENSA) de Tétouan. Le système permet de gérer les formations, cahiers de charges, emplois du temps et réunions pédagogiques.

### Objectifs
- Digitaliser la gestion pédagogique de l'ENSA Tétouan
- Faciliter la communication entre les différents acteurs
- Optimiser les processus de gestion des formations
- Gérer les emplois du temps et réunions

### Package
`com.example.gestionbpedagogique`

### Configuration
- **Min SDK :** 30 (Android 11)
- **Target SDK :** 36 (Android 15)
- **Compile SDK :** 36
- **Java Version :** 11
- **Gradle AGP :** 8.13.2

---

## 🏗️ Architecture

### Pattern Architectural
- **MVVM** (Model-View-ViewModel) - À implémenter
- Actuellement : **MVC simple** avec Activities directes

### Structure des Packages
```
com.example.gestionbpedagogique/
├── database/
│   ├── AppDatabase.java              # Base de données Room principale
│   ├── DatabaseInitializer.java      # Initialisation des données de test
│   ├── dao/                          # Data Access Objects
│   │   ├── UserDao.java
│   │   ├── FormationDao.java
│   │   ├── ModuleDao.java
│   │   ├── CahierChargesDao.java
│   │   ├── ReunionDao.java
│   │   ├── ReunionParticipantDao.java
│   │   └── EmploiTempsDao.java
│   └── entities/                      # Entités Room
│       ├── User.java
│       ├── Formation.java
│       ├── Module.java
│       ├── CahierCharges.java
│       ├── Reunion.java
│       ├── ReunionParticipant.java
│       └── EmploiTemps.java
├── WelcomeActivity.java              # Page d'accueil
├── LoginActivity.java                # Authentification
├── HomeActivity.java                 # Page d'accueil après connexion
├── DashboardActivity.java            # Tableau de bord principal
├── EmploiTempsActivity.java          # Consultation emplois du temps
├── EmploiTempsAdapter.java           # Adapter pour RecyclerView
├── EmploiTempsEditActivity.java      # Création/Modification emplois du temps (Admin)
├── UserSpinnerAdapter.java           # Adapter personnalisé pour spinner des utilisateurs
├── ModuleSpinnerAdapter.java          # Adapter personnalisé pour spinner des modules
└── GestionPedagogiqueApp.java        # Application class
```

---

## 💾 Base de Données

### Room Database
- **Nom de la base :** `gestion_pedagogique_db`
- **Version :** 1
- **Localisation :** SQLite locale

### Entités

#### 1. User (Utilisateurs)
```java
- id: long (PK, auto)
- username: String (unique)
- password: String
- userType: String (ADMIN, PROFESSEUR_ASSISTANT, PROFESSEUR_VACATAIRE)
- fullName: String
- email: String
- phone: String
```

#### 2. Formation (Formations)
```java
- id: long (PK, auto)
- type: String (INITIALE, CONTINUE)
- cycle: String (PREPARATOIRE, INGENIEUR, MASTER, DCA, DCESS)
- title: String
- description: String
- status: String (EN_ATTENTE, APPROUVEE, REFUSEE)
- createdDate: long
- validatedDate: long
- createdByUserId: long (FK -> User)
```

#### 3. Module (Modules de cours)
```java
- id: long (PK, auto)
- code: String
- nom: String
- volumeHoraire: int
- formationId: long (FK -> Formation)
- professeurId: Long (FK -> User, nullable)
```

#### 4. CahierCharges (Cahiers de charges)
```java
- id: long (PK, auto)
- titre: String
- type: String (FORMATION_INITIALE, FORMATION_CONTINUE)
- filePath: String
- auteurId: long (FK -> User)
- formationId: Long (FK -> Formation, nullable)
- statut: String (BROUILLON, ENVOYE, APPROUVE, REFUSE)
- dateCreation: long
- dateValidation: Long (nullable)
```

#### 5. Reunion (Réunions)
```java
- id: long (PK, auto)
- titre: String
- dateHeure: long (timestamp)
- organisateurId: long (FK -> User)
- ordreDuJour: String
- statut: String (PLANIFIEE, EN_COURS, TERMINEE)
```

#### 6. ReunionParticipant (Participants aux réunions)
```java
- id: long (PK, auto)
- reunionId: long (FK -> Reunion)
- userId: long (FK -> User)
- present: boolean
```

#### 7. EmploiTemps (Emplois du temps)
```java
- id: long (PK, auto)
- professeurId: long (FK -> User)
- moduleId: long (FK -> Module)
- jourSemaine: String (LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI)
- heureDebut: String (format: "HH:mm")
- heureFin: String (format: "HH:mm")
- salle: String
- typeCours: String (CM, TD, TP)
```

### Données de Test

#### Utilisateurs
- **Admin :** `admin` / `admin123`
- **Professeur Assistant 1 :** `prof.assistant1` / `prof123`
- **Professeur Assistant 2 :** `prof.assistant2` / `prof123`
- **Professeur Vacataire :** `prof.vacataire` / `prof123`

#### Formations Initialisées
- Cycle Préparatoire
- Cycle Ingénieur
- Cycle Master
- DCA (Formation continue)
- DCESS (Formation continue)

#### Modules Initialisés
- **Cycle Préparatoire :** MATH101, PHYS101, INFO101
- **Cycle Ingénieur :** ALGO201, BD201, RESEAU201

#### Emplois du Temps Initialisés
- Algorithmique (ALGO201) - Professeur Assistant 1 - Lundi 08:00-10:00 - CM
- Algorithmique (ALGO201) - Professeur Assistant 1 - Mercredi 14:00-16:00 - TD
- Bases de Données (BD201) - Professeur Assistant 2 - Mardi 10:00-12:00 - CM
- Bases de Données (BD201) - Professeur Assistant 2 - Jeudi 14:00-16:00 - TP
- Mathématiques (MATH101) - Professeur Vacataire - Vendredi 08:00-10:00 - CM

---

## 📁 Structure du Projet

### Layouts (res/layout/)
- `activity_welcome.xml` - Page d'accueil
- `activity_login.xml` - Page de connexion
- `activity_home.xml` - Page d'accueil après connexion
- `activity_dashboard.xml` - Tableau de bord
- `activity_emploi_temps.xml` - Consultation emplois du temps
- `activity_emploi_temps_edit.xml` - Formulaire création/modification emploi du temps
- `item_emploi_temps.xml` - Item de la liste des emplois du temps

### Ressources (res/values/)
- `strings.xml` - Toutes les chaînes de caractères (français)
- `colors.xml` - Palette de couleurs ENSA
- `themes.xml` - Thèmes Material Design 3

### Couleurs Principales
- **Primary Blue :** `#1E3A8A`
- **Primary Blue Dark :** `#1E40AF`
- **Primary Blue Light :** `#3B82F6`
- **Accent Orange :** `#F97316`
- **Background Light :** `#F8FAFC`
- **Text Primary :** `#1F2937`
- **Text Secondary :** `#6B7280`
- **Card Background :** `#FFFFFF`

---

## ✨ Fonctionnalités Implémentées

### ✅ Complétées (5/8) - 100% fonctionnel

#### 1. Authentification
- **Fichiers :** `WelcomeActivity.java`, `LoginActivity.java`
- **Fonctionnalités :**
  - Page d'accueil avec présentation de l'app
  - Formulaire de connexion
  - Validation des identifiants via base de données
  - Option "Se souvenir de moi"
  - Gestion des erreurs de connexion

#### 2. Page d'Accueil Post-Connexion
- **Fichiers :** `HomeActivity.java`
- **Fonctionnalités :**
  - Affichage du nom de l'utilisateur
  - Affichage du type d'utilisateur (traduit en français)
  - Redirection automatique vers Dashboard après 2 secondes

#### 3. Tableau de Bord
- **Fichiers :** `DashboardActivity.java`
- **Fonctionnalités :**
  - Menu adaptatif selon le type d'utilisateur
  - Affichage des informations utilisateur
  - Bouton de déconnexion
  - Navigation vers les différentes fonctionnalités

#### 4. Consultation des Emplois du Temps
- **Fichiers :** `EmploiTempsActivity.java`, `EmploiTempsAdapter.java`
- **Fonctionnalités :**
  - Liste des emplois du temps avec RecyclerView
  - Recherche en temps réel (professeur, module, jour, salle, type)
  - Affichage adaptatif :
    - Admin : voit tous les emplois du temps + bouton "Ajouter" + clic pour éditer
    - Autres : voient uniquement leurs propres emplois (lecture seule)
  - État vide géré
  - Design Material Design 3

#### 5. Élaborer Emploi du Temps (Admin)
- **Fichiers :** `EmploiTempsEditActivity.java`, `UserSpinnerAdapter.java`, `ModuleSpinnerAdapter.java`
- **Fonctionnalités :**
  - Créer un nouvel emploi du temps
  - Modifier un emploi du temps existant (clic sur item dans la liste)
  - Formulaire avec :
    - Sélection du professeur (spinner avec affichage du nom complet)
    - Sélection du module (spinner avec affichage du nom)
    - Sélection du jour (spinner)
    - **Heure de début et fin : TimePickerDialog** (sélection visuelle, format 24h)
    - Salle (texte)
    - Type de cours (CM, TD, TP)
  - Validation des champs
  - Sauvegarde en base de données
  - **TimePicker intégré :** Les champs d'heure utilisent un TimePickerDialog natif pour éviter les erreurs de format

### 🚧 À Implémenter (3/8)

#### 1. Planifier une Réunion (Admin)
- Sélection des professeurs à inviter
- Date et heure
- Ordre du jour
- Envoi d'invitations

#### 2. Envoyer Cahier de Charges
- **Admin :** Envoyer à la présidence/ministère
- **Professeur Assistant :** Envoyer au directeur adjoint
- Upload de documents
- Gestion des statuts

#### 3. Traiter Formation (Admin)
- Gestion des formations initiales (cycles)
- Gestion des formations continues (DCA, DCESS)
- Ajout/modification de modules
- Recherche et filtrage

#### 4. Élaborer Emploi du Temps (Admin) ✅ COMPLÉTÉ
- ✅ Créer des emplois du temps
- ✅ Modifier des créneaux existants
- ✅ Formulaire complet avec validation
- ✅ TimePickerDialog pour sélection des heures (évite les erreurs de format)
- ✅ Spinners personnalisés pour affichage correct des noms

---

## 👥 Types d'Utilisateurs

### 1. ADMIN (Directeur Adjoint)
**Permissions :** Tous les droits

**Fonctionnalités :**
- ✅ Consulter tous les emplois du temps
- ✅ Élaborer les emplois du temps (créer, modifier)
- 🚧 Planifier des réunions
- 🚧 Envoyer cahiers de charges à la présidence/ministère
- 🚧 Traiter les formations (ajouter, modifier)

### 2. PROFESSEUR_ASSISTANT (Professeur Assistant)
**Permissions :** Droits limités

**Fonctionnalités :**
- ✅ Consulter ses propres emplois du temps
- 🚧 Envoyer cahier de charges au directeur adjoint
- 🚧 Consulter le planning des réunions

### 3. PROFESSEUR_VACATAIRE (Professeur Vacataire)
**Permissions :** Consultation uniquement

**Fonctionnalités :**
- ✅ Consulter ses propres emplois du temps

---

## 🔄 Workflows

### Workflow de Connexion
```
WelcomeActivity → LoginActivity → HomeActivity → DashboardActivity
```

### Workflow de Consultation Emploi du Temps
```
DashboardActivity → EmploiTempsActivity
  ├─ Admin : Voit tous les emplois
  ├─ Professeur Assistant : Voit ses emplois
  └─ Professeur Vacataire : Voit ses emplois
```

### Workflow de Déconnexion
```
DashboardActivity → WelcomeActivity (avec clear task)
```

---

## 🛠️ Technologies Utilisées

### Core
- **Java 11** - Langage de programmation
- **Android SDK** - Framework Android

### UI/UX
- **Material Design 3** - Design system
- **Material Components** - Composants UI
- **ConstraintLayout** - Layouts
- **RecyclerView** - Listes performantes

### Base de Données
- **Room Database 2.6.1** - ORM SQLite
  - Runtime
  - Compiler (annotation processor)

### Architecture Components
- **AppCompat 1.7.1** - Compatibilité
- **Activity 1.12.2** - Gestion des activités
- **Edge-to-Edge** - Interface moderne

### Build Tools
- **Gradle 8.13** - Build system
- **AGP 8.13.2** - Android Gradle Plugin
- **Version Catalog** - Gestion des dépendances

---

## 📖 Guide de Développement

### Ajouter une Nouvelle Fonctionnalité

1. **Créer l'Activity**
   ```java
   public class NewFeatureActivity extends AppCompatActivity {
       // Implementation
   }
   ```

2. **Créer le Layout**
   - Créer `activity_new_feature.xml` dans `res/layout/`

3. **Ajouter au Manifest**
   ```xml
   <activity
       android:name=".NewFeatureActivity"
       android:exported="false"
       android:parentActivityName=".DashboardActivity" />
   ```

4. **Ajouter la Navigation**
   - Mettre à jour `DashboardActivity` pour naviguer vers la nouvelle activité

5. **Ajouter les Strings**
   - Ajouter les chaînes dans `res/values/strings.xml`

### Ajouter une Nouvelle Entité

1. **Créer l'Entité**
   ```java
   @Entity(tableName = "new_entity")
   public class NewEntity {
       @PrimaryKey(autoGenerate = true)
       public long id;
       // Fields
   }
   ```

2. **Créer le DAO**
   ```java
   @Dao
   public interface NewEntityDao {
       @Query("SELECT * FROM new_entity")
       List<NewEntity> getAll();
       // Other queries
   }
   ```

3. **Mettre à jour AppDatabase**
   - Ajouter l'entité dans `@Database(entities = {...})`
   - Ajouter le DAO : `public abstract NewEntityDao newEntityDao();`

4. **Mettre à jour DatabaseInitializer**
   - Ajouter l'initialisation des données de test si nécessaire

### Bonnes Pratiques

1. **Threading**
   - Les opérations de base de données doivent être faites en arrière-plan
   - Utiliser `new Thread()` ou AsyncTask (à migrer vers Coroutines/RxJava)

2. **Gestion des Erreurs**
   - Toujours vérifier les valeurs null
   - Afficher des messages d'erreur appropriés

3. **Strings**
   - Toujours utiliser les ressources strings.xml
   - Pas de strings hardcodées

4. **Navigation**
   - Utiliser Intent avec USER_ID pour passer l'utilisateur connecté
   - Gérer le bouton retour correctement

5. **Design**
   - Suivre Material Design 3
   - Utiliser les couleurs définies dans colors.xml
   - Responsive design (ScrollView pour petits écrans)

---

## 📊 État du Projet

### Version Actuelle : 1.2

#### Fonctionnalités Complétées (5/8)
- ✅ Authentification complète
- ✅ Page d'accueil et navigation
- ✅ Tableau de bord avec menu adaptatif
- ✅ Consultation des emplois du temps
- ✅ Élaboration des emplois du temps (création/modification avec TimePicker)

#### Fonctionnalités En Cours (0/8)
- Aucune

#### Fonctionnalités À Faire (3/8)
- 🚧 Planifier une réunion (Admin)
- 🚧 Envoyer cahier de charges (Admin + Professeur Assistant)
- 🚧 Traiter formation (Admin)

### Prochaines Étapes Suggérées

**Prochaine fonctionnalité recommandée : "Planifier une Réunion" (Admin)**

1. **Implémenter "Planifier une Réunion" (Admin)**
   - Créer `ReunionActivity` et `ReunionEditActivity`
   - Sélection multiple de professeurs (checkboxes)
   - Date picker et TimePicker pour date/heure
   - Champ ordre du jour (textarea)
   - Enregistrement en base de données
   - Affichage des réunions planifiées
   - Gestion des participants (ReunionParticipant)

2. **Implémenter "Envoyer Cahier de Charges"**
   - Upload de fichiers (Storage Access Framework)
   - Gestion des statuts (BROUILLON, ENVOYE, APPROUVE, REFUSE)
   - Workflow d'approbation
   - Différents workflows selon le type d'utilisateur

3. **Implémenter "Traiter Formation"**
   - CRUD formations (créer, lire, modifier, supprimer)
   - Gestion des modules associés
   - Recherche et filtrage par type/cycle
   - Validation des formations

### Améliorations Futures

1. **Architecture**
   - Migrer vers MVVM avec ViewModel et LiveData
   - Utiliser Repository pattern
   - Navigation Component

2. **Sécurité**
   - Hashage des mots de passe (BCrypt)
   - Chiffrement des données sensibles
   - Gestion des sessions

3. **Performance**
   - Utiliser Coroutines ou RxJava pour les opérations async
   - Pagination pour les grandes listes
   - Cache des données

4. **UI/UX**
   - Animations de transition
   - Pull-to-refresh
   - Swipe actions

5. **Fonctionnalités**
   - Notifications push
   - Export PDF des emplois du temps
   - Synchronisation cloud (optionnel)

---

## 🔧 Configuration et Dépendances

### Fichiers de Configuration

#### build.gradle (app)
```gradle
compileSdk 36
minSdk 30
targetSdk 36
javaVersion 11
```

#### Dependencies (gradle/libs.versions.toml)
- appcompat: 1.7.1
- material: 1.13.0
- activity: 1.12.2
- constraintlayout: 2.2.1
- recyclerview: 1.3.2
- room: 2.6.1

### Configuration Room
- Schema location: `$projectDir/schemas`
- Fallback to destructive migration: Activé (dev uniquement)
- Allow main thread queries: Activé (dev uniquement - à changer en prod)

---

## 📝 Notes Importantes

### Développement
- Les mots de passe sont stockés en clair (à changer en production)
- Les requêtes DB sont faites sur le thread principal (à changer)
- `fallbackToDestructiveMigration()` est activé (à désactiver en prod)

### Base de Données
- La base est initialisée automatiquement au démarrage
- Les données de test sont créées si elles n'existent pas
- La méthode `addEmploiTempsIfMissing()` assure la présence des emplois du temps

### Navigation
- USER_ID est passé via Intent entre les activités
- Le bouton retour est géré automatiquement via parentActivityName

---

## 🐛 Problèmes Connus

Aucun problème connu actuellement.

---

## 📞 Support

Pour toute question ou problème :
- Vérifier les logs dans Logcat
- Vérifier la base de données via Device File Explorer
- Consulter cette documentation

---

**Dernière mise à jour :** 2024-12-19  
**Maintenu par :** Équipe de développement ENSA

---

## 📅 Changelog

### Version 1.2 - 2024-12-19

#### Améliorations
- ⚡ **TimePickerDialog intégré** : Les champs d'heure utilisent maintenant un TimePickerDialog natif au lieu de saisie manuelle
  - Évite les erreurs de format "HH:mm"
  - Interface plus intuitive avec sélection visuelle
  - Format 24h automatique
  - Champs en lecture seule avec icône d'horloge cliquable
- 🔄 **Spinners personnalisés** : Création de `UserSpinnerAdapter` et `ModuleSpinnerAdapter` pour afficher correctement les noms au lieu de "com.example..."

#### Corrections
- 🐛 Correction de l'affichage des spinners (professeur et module) qui affichaient "com.example..." au lieu des noms
- 🐛 Correction du bouton "Ajouter" qui ne fonctionnait pas correctement

---

### Version 1.1 - 2024-12-19

#### Fonctionnalités Ajoutées
- ✅ Élaborer emploi du temps (Admin) - Création et modification
- ✅ Formulaire complet avec spinners pour professeur, module, jour, type
- ✅ Validation des champs (format heure, champs requis)
- ✅ Navigation depuis la liste (clic sur item pour éditer)
- ✅ Bouton "Ajouter" visible uniquement pour Admin

#### Modifications
- 🔄 EmploiTempsActivity : Ajout du bouton "Ajouter" pour Admin
- 🔄 EmploiTempsAdapter : Ajout du clic pour éditer (Admin uniquement)
- 🔄 Layouts : Nouveau layout pour le formulaire d'édition

---

### Version 1.0 - 2024-12-19

#### Fonctionnalités Ajoutées
- ✅ Système d'authentification complet (WelcomeActivity, LoginActivity)
- ✅ Page d'accueil post-connexion (HomeActivity)
- ✅ Tableau de bord adaptatif selon le type d'utilisateur (DashboardActivity)
- ✅ Consultation des emplois du temps avec recherche (EmploiTempsActivity)

#### Base de Données
- ✅ Création de toutes les entités Room (7 entités)
- ✅ Création de tous les DAOs (7 DAOs)
- ✅ Initialisation automatique des données de test
- ✅ Méthode pour ajouter les emplois du temps si manquants

#### Infrastructure
- ✅ Configuration Room Database
- ✅ Application class pour initialisation
- ✅ Structure de navigation complète
- ✅ Design Material Design 3

#### Documentation
- ✅ Documentation complète du projet créée
- ✅ README mis à jour

---

### Instructions pour Mettre à Jour la Documentation

**À chaque modification importante :**

1. **Ajout de fonctionnalité :**
   - Mettre à jour la section "Fonctionnalités Implémentées"
   - Ajouter dans "État du Projet"
   - Ajouter une entrée dans le Changelog

2. **Modification d'entité/DAO :**
   - Mettre à jour la section "Base de Données"
   - Documenter les nouveaux champs/méthodes

3. **Ajout de fichier/package :**
   - Mettre à jour "Structure du Projet"
   - Documenter le rôle du nouveau composant

4. **Changement d'architecture :**
   - Mettre à jour "Architecture"
   - Documenter les raisons du changement

5. **Mise à jour des dépendances :**
   - Mettre à jour "Technologies Utilisées"
   - Mettre à jour "Configuration et Dépendances"

**Format du Changelog :**
```markdown
### Version X.X - YYYY-MM-DD

#### Fonctionnalités Ajoutées
- ✅ Description de la fonctionnalité

#### Modifications
- 🔄 Description de la modification

#### Corrections
- 🐛 Description du bug corrigé

#### Améliorations
- ⚡ Description de l'amélioration
```
