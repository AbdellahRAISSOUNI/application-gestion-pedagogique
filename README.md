# Application de Gestion Pédagogique - ENSA Tétouan

Application Android pour la gestion pédagogique de l'École Nationale des Sciences Appliquées (ENSA) de Tétouan.

## 📱 Description

Système de gestion pédagogique permettant de gérer :
- Les formations (initiale et continue)
- Les cahiers de charges
- Les emplois du temps
- Les réunions pédagogiques

## 🚀 Fonctionnalités

### Types d'utilisateurs

1. **Directeur Adjoint (Admin)**
   - Planifier des réunions
   - Envoyer des cahiers de charges
   - Traiter les formations
   - Élaborer les emplois du temps

2. **Professeur Assistant**
   - Envoyer des cahiers de charges
   - Consulter les emplois du temps

3. **Professeur Vacataire**
   - Consulter les emplois du temps

## 🛠️ Technologies utilisées

- **Android** (Java)
- **Room Database** - Base de données locale SQLite
- **Material Design 3** - Interface utilisateur moderne
- **Gradle** - Gestion des dépendances

## 📋 Prérequis

- Android Studio Hedgehog ou version ultérieure
- Android SDK 30 (Android 11) minimum
- Java 11

## 🔧 Installation

1. Cloner le dépôt :
```bash
git clone https://github.com/AbdellahRAISSOUNI/application-gestion-pedagogique.git
```

2. Ouvrir le projet dans Android Studio

3. Synchroniser les dépendances Gradle

4. Exécuter l'application sur un appareil ou un émulateur

## 👥 Utilisateurs de test

- **Admin** : `admin` / `admin123`
- **Professeur Assistant** : `prof.assistant1` / `prof123`
- **Professeur Vacataire** : `prof.vacataire` / `prof123`

## 📊 Structure de la base de données

La base de données Room contient les entités suivantes :
- User (Utilisateurs)
- Formation (Formations)
- Module (Modules de cours)
- CahierCharges (Cahiers de charges)
- Reunion (Réunions)
- ReunionParticipant (Participants aux réunions)
- EmploiTemps (Emplois du temps)

## 📝 Documentation

- **[Documentation complète du projet](PROJECT_DOCUMENTATION.md)** - Documentation technique complète pour le développement

## 📄 Licence

Ce projet est développé dans le cadre académique de l'ENSA Tétouan.

## 👨‍💻 Auteur

Abdellah RAISSOUNI

## 📞 Contact

Pour toute question ou suggestion, veuillez ouvrir une issue sur GitHub.
