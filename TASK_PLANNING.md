# Plan de Développement - Tâches Parallèles

**Date de création :** 2024-12-19  
**Objectif :** Répartition des tâches pour développement en parallèle

---

## 📋 Vue d'Ensemble des Tâches

### Tâches Non Implémentées (3/8)
1. **Planifier une Réunion** (Admin)
2. **Envoyer Cahier de Charges** (Admin + Professeur Assistant)
3. **Traiter Formation** (Admin)

---

## 🎯 TÂCHE 1 : Planifier une Réunion (Admin)

### Description
Permettre à l'administrateur de créer et gérer des réunions pédagogiques avec sélection de participants.

### Sous-tâches Détaillées

#### 1.1 Interface de Liste des Réunions
- [ ] Créer `ReunionActivity.java`
- [ ] Créer `activity_reunion.xml` (layout)
- [ ] Créer `ReunionAdapter.java` (RecyclerView adapter)
- [ ] Créer `item_reunion.xml` (layout item)
- [ ] Afficher liste des réunions avec statut (PLANIFIEE, EN_COURS, TERMINEE)
- [ ] Filtrer par statut
- [ ] Recherche par titre/date
- [ ] Navigation depuis DashboardActivity

#### 1.2 Formulaire de Création/Modification
- [ ] Créer `ReunionEditActivity.java`
- [ ] Créer `activity_reunion_edit.xml` (layout)
- [ ] Champ titre (TextInputEditText)
- [ ] DatePickerDialog pour date/heure
- [ ] Champ ordre du jour (TextInputEditText multiline)
- [ ] Sélection multiple de participants (checkboxes ou RecyclerView avec checkboxes)
- [ ] Validation des champs
- [ ] Sauvegarde en base de données (Reunion + ReunionParticipant)

#### 1.3 Gestion des Participants
- [ ] Charger liste des professeurs (assistants + vacataires)
- [ ] Afficher avec checkboxes
- [ ] Sauvegarder participants sélectionnés dans ReunionParticipant
- [ ] Afficher participants dans la liste des réunions

#### 1.4 Navigation & Intégration
- [ ] Ajouter bouton "Planifier une réunion" dans DashboardActivity (Admin)
- [ ] Navigation vers ReunionActivity
- [ ] Navigation vers ReunionEditActivity (création)
- [ ] Navigation vers ReunionEditActivity (modification depuis liste)
- [ ] Ajouter activités dans AndroidManifest.xml

#### 1.5 Strings & Ressources
- [ ] Ajouter strings dans `strings.xml`
- [ ] Créer icônes si nécessaire

**Estimation :** 6-8 heures  
**Complexité :** Moyenne  
**Dépendances :** Base de données déjà prête (Reunion, ReunionParticipant)

---

## 🎯 TÂCHE 2 : Envoyer Cahier de Charges

### Description
Permettre aux utilisateurs d'envoyer des cahiers de charges avec upload de fichiers et gestion des statuts.

### Sous-tâches Détaillées

#### 2.1 Interface de Liste des Cahiers de Charges
- [ ] Créer `CahierChargesActivity.java`
- [ ] Créer `activity_cahier_charges.xml` (layout)
- [ ] Créer `CahierChargesAdapter.java` (RecyclerView adapter)
- [ ] Créer `item_cahier_charges.xml` (layout item)
- [ ] Afficher liste selon type d'utilisateur :
  - Admin : tous les cahiers
  - Prof Assistant : ses propres cahiers
- [ ] Filtrer par statut (BROUILLON, ENVOYE, APPROUVE, REFUSE)
- [ ] Recherche par titre/type
- [ ] Navigation depuis DashboardActivity

#### 2.2 Formulaire de Création/Modification
- [ ] Créer `CahierChargesEditActivity.java`
- [ ] Créer `activity_cahier_charges_edit.xml` (layout)
- [ ] Champ titre (TextInputEditText)
- [ ] Spinner type (FORMATION_INITIALE, FORMATION_CONTINUE)
- [ ] Spinner formation (optionnel, lié au type)
- [ ] Bouton upload fichier (Storage Access Framework)
- [ ] Afficher nom du fichier sélectionné
- [ ] Validation des champs
- [ ] Sauvegarde en base de données

#### 2.3 Upload de Fichiers
- [ ] Implémenter Storage Access Framework (SAF)
- [ ] Intent ACTION_OPEN_DOCUMENT pour sélection fichier
- [ ] Copier fichier vers répertoire interne de l'app
- [ ] Stocker chemin dans `filePath` de CahierCharges
- [ ] Gérer permissions (READ_EXTERNAL_STORAGE si nécessaire)

#### 2.4 Gestion des Statuts
- [ ] Workflow selon type d'utilisateur :
  - **Prof Assistant :** BROUILLON → ENVOYE (vers Admin)
  - **Admin :** ENVOYE → APPROUVE/REFUSE
- [ ] Bouton "Envoyer" pour Prof Assistant
- [ ] Boutons "Approuver"/"Refuser" pour Admin
- [ ] Mise à jour statut + dateValidation
- [ ] Affichage visuel du statut (couleurs)

#### 2.5 Navigation & Intégration
- [ ] Ajouter bouton dans DashboardActivity (Admin + Prof Assistant)
- [ ] Navigation vers CahierChargesActivity
- [ ] Navigation vers CahierChargesEditActivity (création)
- [ ] Navigation vers CahierChargesEditActivity (modification)
- [ ] Ajouter activités dans AndroidManifest.xml
- [ ] Ajouter permissions dans AndroidManifest.xml si nécessaire

#### 2.6 Strings & Ressources
- [ ] Ajouter strings dans `strings.xml`
- [ ] Créer icônes si nécessaire

**Estimation :** 8-10 heures  
**Complexité :** Élevée (upload fichiers)  
**Dépendances :** Base de données déjà prête (CahierCharges, Formation)

---

## 🎯 TÂCHE 3 : Traiter Formation (Admin)

### Description
Permettre à l'administrateur de gérer les formations (créer, modifier, valider) et leurs modules associés.

### Sous-tâches Détaillées

#### 3.1 Interface de Liste des Formations
- [ ] Créer `FormationActivity.java`
- [ ] Créer `activity_formation.xml` (layout)
- [ ] Créer `FormationAdapter.java` (RecyclerView adapter)
- [ ] Créer `item_formation.xml` (layout item)
- [ ] Afficher liste des formations
- [ ] Filtrer par type (INITIALE, CONTINUE)
- [ ] Filtrer par statut (EN_ATTENTE, APPROUVEE, REFUSEE)
- [ ] Filtrer par cycle (PREPARATOIRE, INGENIEUR, MASTER, DCA, DCESS)
- [ ] Recherche par titre/description
- [ ] Navigation depuis DashboardActivity

#### 3.2 Formulaire de Création/Modification Formation
- [ ] Créer `FormationEditActivity.java`
- [ ] Créer `activity_formation_edit.xml` (layout)
- [ ] Champ titre (TextInputEditText)
- [ ] Champ description (TextInputEditText multiline)
- [ ] Spinner type (INITIALE, CONTINUE)
- [ ] Spinner cycle (dynamique selon type)
- [ ] Validation des champs
- [ ] Sauvegarde en base de données

#### 3.3 Gestion des Modules
- [ ] Section "Modules" dans FormationEditActivity
- [ ] Liste des modules associés à la formation
- [ ] Bouton "Ajouter Module"
- [ ] Créer `ModuleEditDialog.java` ou nouvelle Activity
- [ ] Formulaire module : code, nom, volume horaire
- [ ] Option assigner professeur (spinner)
- [ ] Sauvegarde module avec formationId
- [ ] Supprimer module (avec confirmation)

#### 3.4 Validation des Formations
- [ ] Bouton "Approuver" dans FormationActivity (Admin)
- [ ] Bouton "Refuser" dans FormationActivity (Admin)
- [ ] Mise à jour statut + validatedDate
- [ ] Affichage visuel du statut (couleurs)

#### 3.5 Navigation & Intégration
- [ ] Ajouter bouton "Traiter formation" dans DashboardActivity (Admin)
- [ ] Navigation vers FormationActivity
- [ ] Navigation vers FormationEditActivity (création)
- [ ] Navigation vers FormationEditActivity (modification)
- [ ] Navigation vers ModuleEditDialog/Activity
- [ ] Ajouter activités dans AndroidManifest.xml

#### 3.6 Strings & Ressources
- [ ] Ajouter strings dans `strings.xml`
- [ ] Créer icônes si nécessaire

**Estimation :** 8-10 heures  
**Complexité :** Élevée (gestion modules imbriquée)  
**Dépendances :** Base de données déjà prête (Formation, Module, User)

---

## 📊 Répartition Recommandée pour Travail en Parallèle

### Option 1 : Par Fonctionnalité Complète
- **Développeur 1 :** Tâche 1 - Planifier une Réunion (6-8h)
- **Développeur 2 :** Tâche 2 - Envoyer Cahier de Charges (8-10h)
- **Développeur 3 :** Tâche 3 - Traiter Formation (8-10h)

### Option 2 : Par Composant (Plus Granulaire)
- **Développeur 1 :** 
  - Tâche 1.1, 1.2, 1.3 (Liste + Formulaire Réunion)
  - Tâche 2.1, 2.2 (Liste + Formulaire Cahier de Charges)
- **Développeur 2 :**
  - Tâche 2.3, 2.4 (Upload + Statuts Cahier de Charges)
  - Tâche 3.1, 3.2 (Liste + Formulaire Formation)
- **Développeur 3 :**
  - Tâche 1.4, 1.5 (Navigation + Ressources Réunion)
  - Tâche 2.5, 2.6 (Navigation + Ressources Cahier)
  - Tâche 3.3, 3.4, 3.5, 3.6 (Modules + Validation + Navigation Formation)

---

## 🎯 Ma Sélection de Tâches

**Je vais travailler sur :** **TÂCHE 1 - Planifier une Réunion (Admin)**

### Justification
- Complexité moyenne (bon équilibre)
- Base de données déjà prête
- Pas de dépendances externes (upload fichiers)
- Feature complète et autonome
- Bon point d'entrée pour comprendre l'architecture

### Tâches que je vais implémenter :
1. ✅ Interface de liste des réunions
2. ✅ Formulaire de création/modification
3. ✅ Gestion des participants (sélection multiple)
4. ✅ Navigation & intégration
5. ✅ Strings & ressources

### Ce que mes collègues peuvent faire en parallèle :
- **Collègue 1 :** Tâche 2 - Envoyer Cahier de Charges (plus complexe, upload fichiers)
- **Collègue 2 :** Tâche 3 - Traiter Formation (gestion modules imbriquée)

---

## 📝 Notes Importantes

### Points d'Attention
1. **Cohérence UI :** Suivre le design Material Design 3 existant
2. **Navigation :** Utiliser le pattern Intent avec USER_ID
3. **Threading :** Utiliser `new Thread()` pour DB operations (comme dans le code existant)
4. **Validation :** Toujours valider les champs avant sauvegarde
5. **Strings :** Utiliser `strings.xml`, pas de hardcoding
6. **Base de données :** Les entités et DAOs sont déjà créés, juste les utiliser

### Fichiers de Référence
- `EmploiTempsActivity.java` - Exemple de liste avec RecyclerView
- `EmploiTempsEditActivity.java` - Exemple de formulaire avec validation
- `DashboardActivity.java` - Exemple de navigation selon userType
- `UserSpinnerAdapter.java` - Exemple d'adapter personnalisé

### Tests à Prévoir
- Tester avec différents types d'utilisateurs
- Tester création, modification, suppression
- Tester validation des champs
- Tester navigation

---

## ✅ Checklist de Finalisation

Pour chaque tâche complétée :
- [ ] Code implémenté et testé
- [ ] Strings ajoutées dans `strings.xml`
- [ ] Layouts créés et stylisés
- [ ] Navigation fonctionnelle
- [ ] Activités ajoutées dans `AndroidManifest.xml`
- [ ] Documentation mise à jour dans `PROJECT_DOCUMENTATION.md`
- [ ] Changelog mis à jour

---

**Dernière mise à jour :** 2024-12-19

