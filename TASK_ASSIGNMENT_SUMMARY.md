# Répartition des Tâches - Développement Parallèle

**Date :** 2024-12-19  
**Projet :** Application de Gestion Pédagogique ENSA

---

## 📋 Vue d'Ensemble

Nous avons **3 fonctionnalités majeures** à implémenter en parallèle :

1. ✅ **Planifier une Réunion** (Admin) - **ASSIGNÉ À : [Votre nom]**
2. ⏳ **Envoyer Cahier de Charges** (Admin + Prof Assistant) - **À ASSIGNER**
3. ⏳ **Traiter Formation** (Admin) - **À ASSIGNER**

---

## 🎯 TÂCHE ASSIGNÉE : Planifier une Réunion

**Développeur :** [Votre nom]  
**Estimation :** 6-8 heures  
**Complexité :** Moyenne

### Ce que je vais implémenter :

1. **Interface de liste des réunions**
   - Liste avec RecyclerView
   - Filtrage par statut
   - Recherche

2. **Formulaire de création/modification**
   - Titre, date/heure (DatePickerDialog)
   - Ordre du jour
   - Sélection multiple de participants (checkboxes)

3. **Gestion des participants**
   - Charger liste des professeurs
   - Sauvegarder dans ReunionParticipant

4. **Navigation & intégration**
   - Bouton dans DashboardActivity (Admin)
   - Navigation complète

### Fichiers à créer :
- `ReunionActivity.java`
- `ReunionEditActivity.java`
- `ReunionAdapter.java`
- Layouts correspondants
- Strings dans `strings.xml`

### Base de données :
✅ Déjà prête (Reunion, ReunionParticipant entities + DAOs)

---

## ⏳ TÂCHES DISPONIBLES POUR COLLÈGUES

### TÂCHE 2 : Envoyer Cahier de Charges
**Estimation :** 8-10 heures  
**Complexité :** Élevée (upload fichiers)

**Fonctionnalités :**
- Liste des cahiers de charges (selon type utilisateur)
- Formulaire création/modification
- **Upload de fichiers** (Storage Access Framework)
- Gestion des statuts (BROUILLON → ENVOYE → APPROUVE/REFUSE)
- Workflow différent Admin vs Prof Assistant

**Fichiers à créer :**
- `CahierChargesActivity.java`
- `CahierChargesEditActivity.java`
- `CahierChargesAdapter.java`
- Layouts + permissions pour fichiers

**Base de données :** ✅ Déjà prête (CahierCharges entity + DAO)

---

### TÂCHE 3 : Traiter Formation
**Estimation :** 8-10 heures  
**Complexité :** Élevée (gestion modules imbriquée)

**Fonctionnalités :**
- Liste des formations avec filtres multiples
- Formulaire création/modification formation
- **Gestion des modules** (ajouter, modifier, supprimer)
- Validation des formations (Approuver/Refuser)
- Assignation de professeurs aux modules

**Fichiers à créer :**
- `FormationActivity.java`
- `FormationEditActivity.java`
- `FormationAdapter.java`
- `ModuleEditDialog.java` ou Activity
- Layouts

**Base de données :** ✅ Déjà prête (Formation, Module entities + DAOs)

---

## 📚 Ressources & Références

### Fichiers de référence dans le projet :
- `EmploiTempsActivity.java` - Exemple liste RecyclerView
- `EmploiTempsEditActivity.java` - Exemple formulaire avec validation
- `DashboardActivity.java` - Exemple navigation selon userType
- `UserSpinnerAdapter.java` - Exemple adapter personnalisé

### Documentation :
- `PROJECT_DOCUMENTATION.md` - Documentation complète du projet
- `TASK_PLANNING.md` - Plan détaillé avec toutes les sous-tâches

### Base de données :
- Toutes les entités et DAOs sont déjà créés
- Pas besoin de modifier le schéma
- Utiliser `AppDatabase.getDatabase(context)`

---

## 🎨 Standards à Suivre

1. **UI/UX :**
   - Material Design 3
   - Couleurs ENSA (primary_blue #1E3A8A, accent_orange #F97316)
   - Suivre les layouts existants

2. **Code :**
   - Threading : `new Thread()` pour DB operations
   - Navigation : Intent avec USER_ID
   - Validation : Toujours valider avant sauvegarde
   - Strings : Utiliser `strings.xml`, pas de hardcoding

3. **Architecture :**
   - Pattern MVC actuel (pas encore MVVM)
   - Activities directes
   - Room Database pour persistence

---

## ✅ Checklist de Finalisation

Pour chaque tâche :
- [ ] Code implémenté et testé
- [ ] Strings ajoutées dans `strings.xml`
- [ ] Layouts créés et stylisés
- [ ] Navigation fonctionnelle
- [ ] Activités ajoutées dans `AndroidManifest.xml`
- [ ] Documentation mise à jour dans `PROJECT_DOCUMENTATION.md`
- [ ] Changelog mis à jour

---

## 📞 Coordination

**Questions ou blocages ?**
- Consulter `PROJECT_DOCUMENTATION.md`
- Consulter `TASK_PLANNING.md` pour détails complets
- Vérifier les fichiers de référence existants

**Bon développement ! 🚀**

