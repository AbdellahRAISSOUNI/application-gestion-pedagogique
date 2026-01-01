# Cahier des Charges - Système de Gestion Pédagogique ENSA

## 1. Présentation du Projet

### 1.1 Contexte
Application mobile Android pour la gestion pédagogique de l'École Nationale des Sciences Appliquées (ENSA) de Tétouan. Le système permet de gérer les formations (initiale et continue), les cahiers de charges, les emplois du temps, et les réunions pédagogiques.

### 1.2 Objectifs
- Optimiser la gestion pédagogique au sein de l'ENSA Tétouan
- Digitaliser les processus de gestion des formations
- Faciliter la communication entre les différents acteurs (directeur adjoint, professeurs assistants, professeurs vacataires)
- Gérer les cahiers de charges et leur validation
- Planifier et gérer les emplois du temps
- Organiser les réunions pédagogiques

---

## 2. Types d'Utilisateurs

### 2.1 Directeur Adjoint (Admin)
**Rôle:** Administrateur du système avec tous les droits de gestion.

**Fonctionnalités:**
- Authentification
- Planifier des réunions (sélection des professeurs, envoi d'invitations)
- Envoyer des emails
- Envoyer les cahiers de charges à la présidence/ministère
- Traiter les formations (ajouter, modifier)
  - Formation initiale (Cycle préparatoire, Cycle d'ingénieur, Cycle master)
  - Formation continue (DCA, DCESS)
- Modifier les CPs (modules) avec recherche/filtre
- Élaborer les emplois du temps (créer, modifier, rechercher)

### 2.2 Professeur Assistant
**Rôle:** Professeur permanent avec droits limités.

**Fonctionnalités:**
- Authentification
- Envoyer le cahier de charges au directeur adjoint
- Consulter les emplois du temps (avec recherche/filtre)
- Consulter le planning des réunions

### 2.3 Professeur Vacataire
**Rôle:** Professeur temporaire avec droits de consultation uniquement.

**Fonctionnalités:**
- Authentification
- Consulter les emplois du temps (avec recherche/filtre)

---

## 3. Fonctionnalités Détaillées

### 3.1 Authentification
- Page de bienvenue/connexion
- Champs: nom d'utilisateur, mot de passe
- Option "Se souvenir de moi"
- Lien "Mot de passe oublié"
- Vérification des identifiants
- Redirection selon le type d'utilisateur

### 3.2 Gestion des Réunions (Admin)
- Interface de planification
- Sélection des professeurs à inviter
- Envoi d'invitations par email
- Consultation du planning des réunions

### 3.3 Gestion des Cahiers de Charges

#### 3.3.1 Admin
- Télécharger les cahiers de charges
- Envoyer à la présidence ou au ministère
- Traiter les réponses (acceptation/refus)
- Demander des modifications si refus

#### 3.3.2 Professeur Assistant
- Générer/rédiger le cahier de charges
- Télécharger le document
- Envoyer au directeur adjoint
- Ajouter un message d'accompagnement

### 3.4 Gestion des Formations (Admin)

#### 3.4.1 Formation Initiale
- Gérer les cycles:
  - Cycle préparatoire
  - Cycle d'ingénieur
  - Cycle master
- Ajouter/modifier les modules
- Recherche et filtrage

#### 3.4.2 Formation Continue
- Gérer les types:
  - DCA (Diplôme de Cycle d'Approfondissement)
  - DCESS (Diplôme de Cycle d'Études Supérieures Spécialisées)
- Ajouter/modifier les modules
- Recherche et filtrage

### 3.5 Gestion des Emplois du Temps

#### 3.5.1 Admin
- Créer les emplois du temps
- Modifier les créneaux
- Colonnes: Professeur, Module, Jour, Heure, Salle
- Recherche et filtrage

#### 3.5.2 Professeur Assistant & Vacataire
- Consulter les emplois du temps
- Recherche et filtrage par différents critères

### 3.6 Gestion des CPs (Modules) - Admin
- Interface de modification des CPs
- Table avec filtres de recherche
- Ajouter/modifier des modules

---

## 4. Modèles de Données

### 4.1 Utilisateur
- ID
- Nom d'utilisateur
- Mot de passe (hashé)
- Type (Admin, Professeur Assistant, Professeur Vacataire)
- Nom complet
- Email
- Téléphone

### 4.2 Formation
- ID
- Type (Initiale, Continue)
- Cycle/Niveau (pour formation initiale)
- Type (DCA, DCESS pour formation continue)
- Modules associés
- Date de création
- Statut (En attente, Approuvée, Refusée)

### 4.3 Module (CP)
- ID
- Nom
- Code
- Volume horaire
- Formation associée
- Professeur responsable

### 4.4 Cahier de Charges
- ID
- Titre
- Type (Formation initiale, Formation continue)
- Fichier (PDF/Document)
- Auteur (Professeur Assistant)
- Statut (Brouillon, Envoyé, Approuvé, Refusé)
- Date de création
- Date de validation

### 4.5 Réunion
- ID
- Titre
- Date et heure
- Organisateur (Admin)
- Participants (liste de professeurs)
- Statut (Planifiée, En cours, Terminée)
- Ordre du jour

### 4.6 Emploi du Temps
- ID
- Professeur
- Module
- Jour de la semaine
- Heure de début
- Heure de fin
- Salle
- Type de cours (CM, TD, TP)

---

## 5. Interfaces Utilisateur

### 5.1 Page de Bienvenue/Connexion
- Logo ENSA
- Titre "Bienvenue"
- Sous-titre "Gestion Pédagogique ENSA"
- Formulaire de connexion
- Design moderne et professionnel

### 5.2 Dashboard Admin
- Menu avec options:
  - Planifier une réunion
  - Envoyer cahier de charges
  - Traiter formation
  - Élaborer emploi du temps

### 5.3 Dashboard Professeur Assistant
- Menu avec options:
  - Envoyer cahier de charges
  - Consulter emploi du temps

### 5.4 Dashboard Professeur Vacataire
- Menu avec option:
  - Consulter emploi du temps

### 5.5 Interfaces Spécifiques
- Interface de planification de réunion
- Interface d'envoi d'email
- Interface d'envoi de cahier de charges
- Interface de traitement de formation
- Interface de formation initiale
- Interface de formation continue
- Interface de modification des CPs
- Interface d'élaboration d'emploi du temps
- Interface de consultation d'emploi du temps

---

## 6. Contraintes Techniques

### 6.1 Plateforme
- Android (min SDK 30, target SDK 36)
- Java 11
- Material Design 3

### 6.2 Architecture
- Architecture à définir (MVVM recommandée)
- Base de données locale (Room Database)
- Gestion des fichiers (PDF, documents)
- Envoi d'emails (intent ou API)

### 6.3 Sécurité
- Hashage des mots de passe
- Validation des entrées
- Gestion des sessions
- Stockage sécurisé des données sensibles

---

## 7. Workflow Principal

### 7.1 Processus de Formation Initiale
1. Professeur Assistant détecte un besoin de modification (20% par an)
2. Génère un cahier de charges
3. Envoie au Directeur Adjoint
4. Directeur Adjoint examine et envoie à la présidence
5. Réponse: Acceptation → Formation créée | Refus → Modification requise

### 7.2 Processus de Formation Continue
1. Professeur Assistant rédige le cahier de charges
2. Envoie au Directeur Adjoint
3. Directeur Adjoint envoie à la présidence pour accréditation
4. Réponse: Acceptation → Formation créée | Refus → Modification requise

### 7.3 Processus de Renouvellement (5 ans)
1. Directeur Adjoint orchestre les préparations
2. Réunions avec enseignants
3. Nouveau cahier des charges
4. Nouveaux syllabus et descriptifs de filière

---

## 8. Critères d'Acceptation

### 8.1 Fonctionnels
- Tous les types d'utilisateurs peuvent s'authentifier
- Chaque utilisateur accède uniquement à ses fonctionnalités
- Les workflows sont respectés
- Les données sont persistées correctement

### 8.2 Non-Fonctionnels
- Interface intuitive et moderne
- Performance acceptable
- Sécurité des données
- Compatibilité Android 11+

---

## 9. Plan de Développement

### Phase 1: Fondations
- Page de bienvenue/connexion
- Système d'authentification
- Navigation de base

### Phase 2: Dashboard et Navigation
- Dashboards par type d'utilisateur
- Navigation entre écrans
- Structure de base de données

### Phase 3: Gestion des Formations
- CRUD formations
- Gestion des modules
- Interface de traitement

### Phase 4: Gestion des Cahiers de Charges
- Génération/upload de documents
- Workflow d'envoi et validation
- Gestion des statuts

### Phase 5: Gestion des Emplois du Temps
- CRUD emplois du temps
- Consultation avec recherche
- Filtres et recherche

### Phase 6: Gestion des Réunions
- Planification
- Envoi d'invitations
- Consultation du planning

### Phase 7: Finalisation
- Tests
- Optimisations
- Documentation
