# Diagrammes de Conception - Gestion Pédagogique ENSA

Ce dossier contient tous les diagrammes de conception du projet **Application de Gestion Pédagogique ENSA Tétouan**.

## 📋 Contenu

### 1. Diagramme de Cas d'Utilisation (`use-case-diagram.puml`)

Ce diagramme présente tous les cas d'utilisation du système selon les différents types d'utilisateurs :

- **Directeur Adjoint (Admin)** : Tous les droits d'administration
- **Professeur Assistant** : Création et envoi de cahiers de charges, consultation
- **Professeur Vacataire** : Consultation uniquement

**Fonctionnalités couvertes :**
- Authentification
- Gestion des formations
- Gestion des réunions
- Gestion des cahiers de charges
- Gestion des emplois du temps

### 2. Diagrammes de Séquence

Ces diagrammes détaillent les interactions entre les différents composants du système pour les workflows principaux :

#### 2.1. Authentification (`sequence-authentication.puml`)
- Flux de connexion utilisateur
- Navigation WelcomeActivity → LoginActivity → HomeActivity → DashboardActivity
- Vérification des identifiants via la base de données

#### 2.2. Création d'Emploi du Temps (`sequence-create-schedule.puml`)
- Workflow complet de création d'un emploi du temps par l'Admin
- Utilisation de TimePicker pour la sélection des heures
- Sauvegarde en base de données

#### 2.3. Planification de Réunion (`sequence-plan-meeting.puml`)
- Création d'une réunion par l'Admin
- Sélection multiple de participants
- Utilisation de DatePicker et TimePicker

#### 2.4. Envoi de Cahier de Charges (`sequence-send-cahier.puml`)
- Workflow pour le Professeur Assistant
- Création en brouillon
- Upload de fichier via Storage Access Framework
- Envoi au Directeur Adjoint

#### 2.5. Approbation de Cahier (`sequence-approve-cahier.puml`)
- Workflow d'approbation/refus par l'Admin
- Mise à jour du statut et de la date de validation

#### 2.6. Consultation d'Emploi du Temps (`sequence-view-schedule.puml`)
- Consultation en lecture seule pour les professeurs
- Filtrage et recherche

### 3. Diagramme de Classes (`class-diagram.puml`)

Ce diagramme complet présente :

#### 3.1. Couche Base de Données
- **AppDatabase** : Base de données Room principale
- **DatabaseInitializer** : Initialisation des données

#### 3.2. Entités (Entities)
- **User** : Utilisateurs du système
- **Formation** : Formations (initiale et continue)
- **Module** : Modules de cours
- **CahierCharges** : Cahiers de charges
- **Reunion** : Réunions pédagogiques
- **ReunionParticipant** : Participants aux réunions
- **EmploiTemps** : Emplois du temps

#### 3.3. DAOs (Data Access Objects)
- Interfaces pour toutes les opérations CRUD sur les entités

#### 3.4. Activities
- Toutes les activités Android avec leurs méthodes principales

#### 3.5. Adapters
- Adapters pour RecyclerView et Spinners

#### 3.6. Relations
- Relations entre entités (Foreign Keys)
- Relations entre composants (utilisation, navigation)

## 🛠️ Utilisation

### Prérequis

Pour visualiser ces diagrammes, vous avez besoin d'un outil compatible PlantUML :

1. **En ligne** : [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
2. **Extension VS Code** : PlantUML Extension
3. **Extension IntelliJ/Android Studio** : PlantUML Integration
4. **Application Desktop** : PlantUML (nécessite Java)

### Visualisation

1. **Méthode 1 - En ligne** :
   - Ouvrir [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
   - Copier le contenu d'un fichier `.puml`
   - Coller dans l'éditeur
   - Le diagramme sera généré automatiquement

2. **Méthode 2 - VS Code** :
   - Installer l'extension "PlantUML"
   - Ouvrir un fichier `.puml`
   - Appuyer sur `Alt+D` pour prévisualiser

3. **Méthode 3 - IntelliJ/Android Studio** :
   - Installer le plugin "PlantUML integration"
   - Ouvrir un fichier `.puml`
   - Clic droit → "Preview PlantUML Diagram"

### Export

Pour exporter en image (PNG, SVG, PDF) :

1. **En ligne** : Utiliser le bouton d'export sur PlantUML Online
2. **VS Code** : Clic droit sur la prévisualisation → "Export Current Diagram"
3. **IntelliJ** : Clic droit → "Export Diagram"

## 📝 Notes Importantes

- Tous les diagrammes utilisent le thème `plain` pour un rendu propre
- Les couleurs sont cohérentes avec le design de l'application
- Les diagrammes sont détaillés et incluent toutes les interactions importantes
- Les noms de classes, méthodes et attributs correspondent exactement au code source

## 🔍 Structure des Fichiers

```
conception-diagrams/
├── README.md                          # Ce fichier
├── use-case-diagram.puml             # Diagramme de cas d'utilisation
├── class-diagram.puml                 # Diagramme de classes complet
├── sequence-authentication.puml       # Séquence d'authentification
├── sequence-create-schedule.puml       # Séquence création emploi du temps
├── sequence-plan-meeting.puml         # Séquence planification réunion
├── sequence-send-cahier.puml          # Séquence envoi cahier de charges
├── sequence-approve-cahier.puml       # Séquence approbation cahier
└── sequence-view-schedule.puml        # Séquence consultation emploi du temps
```

## ✨ Caractéristiques des Diagrammes

- ✅ **Complets** : Tous les composants importants sont représentés
- ✅ **Détaillés** : Méthodes, attributs et relations sont documentés
- ✅ **Propres** : Formatage cohérent et lisible
- ✅ **Sans erreurs** : Syntaxe PlantUML valide
- ✅ **Professionnels** : Utilisation de thèmes et couleurs appropriés

## 📚 Documentation Complémentaire

Pour plus d'informations sur le projet, consultez :
- `README.md` à la racine du projet
- `PROJECT_DOCUMENTATION.md` pour la documentation technique complète

---

**Dernière mise à jour** : 2024-12-19  
**Version** : 1.0
