# Airline Online Check-In — Mobile App

> Projet · 2CS SIL · Techniques de Développement Mobile  
> École Nationale Supérieure d'Informatique (ESI)

Application Android native permettant aux passagers d'effectuer leur enregistrement en ligne avant leur vol, de sélectionner leur siège, de déclarer leurs bagages et d'obtenir leur carte d'embarquement numérique directement depuis leur smartphone.

---

## Structure du projet

Le projet suit une **architecture Clean Architecture** organisée en trois modules Gradle indépendants :

```
Check-In-Mobile-App/
|-- app/                        # Module principal (couche Presentation)
|   +-- src/main/
|       +-- AndroidManifest.xml
|       +-- java/com/example/check_in_mobile_app/
|       |   +-- di/                 # Injection de dependances
|       |   +-- presentation/       # Ecrans et ViewModels Compose
|       |   |   +-- auth/           # Inscription & Connexion (email / Google)
|       |   |   +-- flight/         # Recherche & recuperation de reservation
|       |   |   +-- checkin/        # Processus d'enregistrement (scan passeport, siege, bagages...)
|       |   |   +-- seat/           # Selection du siege (carte cabine interactive)
|       |   |   +-- boarding/       # Carte d'embarquement & QR Code
|       |   |   +-- notification/   # Ecran des notifications
|       |   |   +-- components/     # Composants Compose reutilisables
|       |   |   +-- navigation/     # Graphe de navigation Compose
|       |   +-- ui/theme/           # Couleurs, typographie, theme Material 3
|       +-- res/                    # Ressources (drawables, valeurs, XML)
|
|-- domain/                     # Module domaine (logique metier pure)
|   +-- src/main/java/com/example/domain/
|       +-- model/              # Entites metier (Flight, Passenger, BoardingPass...)
|       +-- repository/         # Interfaces des repositories
|       +-- usecase/            # Cas d'utilisation
|       +-- state/              # Machine a etats du check-in (patron State)
|       +-- factory/            # Creation des sessions check-in (patron Factory)
|       +-- strategy/           # Strategies d'authentification (patron Strategy)
|       +-- validation/
|           +-- registration/   # Validation a l'inscription (patron Chain of Responsibility)
|           +-- ocr/            # Validation du scan passeport (patron Chain of Responsibility)
|
|-- data/                       # Module donnees (implementations & sources)
|   +-- src/main/java/com/example/data/
|       +-- remote/
|       |   +-- api/            # Interfaces Retrofit (appels vers le backend REST)
|       |   +-- dto/            # Objets JSON recus du serveur
|       |   +-- ocr/            # Scan passeport via ML Kit (local)
|       +-- local/              # Base de donnees locale SQLite via Room
|       |   +-- dao/            # Requetes SQL
|       |   +-- entity/         # Tables Room
|       +-- mapper/             # Conversion DTO <-> modele domaine
|       +-- repository/         # Implementations concretes des interfaces du domaine
|
+-- gradle/
|   +-- libs.versions.toml      # Catalogue de versions des dependances
+-- build.gradle.kts            # Configuration Gradle racine
+-- settings.gradle.kts         # Declaration des modules
+-- README.md
```

---

## Architecture

Le projet applique les principes de la **Clean Architecture** avec une séparation stricte des responsabilités entre trois couches :

```
┌─────────────────────────────────────┐
│           app (Présentation)        │  Jetpack Compose, ViewModels, Navigation
├─────────────────────────────────────┤
│              domain                 │  Use Cases, Entités, Interfaces Repository
├─────────────────────────────────────┤
│               data                  │  Retrofit (API), Room (local), Repositories
└─────────────────────────────────────┘
```

- **`app`** dépend de `domain` et `data`
- **`domain`** est indépendant de tout framework Android
- **`data`** implémente les interfaces définies dans `domain`

---

## Fonctionnalités

| # | Fonctionnalité | Description |
|---|---------------|-------------|
| 1 | **Inscription / Connexion** | Création de compte (email, téléphone, mot de passe) ou connexion via Google |
| 2 | **Recherche de vol** | Récupération de la réservation par numéro de dossier et nom de famille |
| 3 | **Enregistrement en ligne** | Disponible 24h avant le départ — scan de passeport (OCR), revue des informations, sélection du siège, déclaration de bagages et demandes spéciales |
| 4 | **Carte d'embarquement** | Génération d'un QR Code unique, téléchargement en PDF, accès hors ligne |
| 5 | **Mode hors ligne** | Données mises en cache localement (Room) avec synchronisation automatique au retour de la connexion |

---

## Stack technique

| Composant | Technologie |
|-----------|------------|
| Langage | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose (`2.x`) |
| Architecture | Clean Architecture + MVVM |
| Build | Gradle (Kotlin DSL) · Version Catalog |
| SDK cible | Android 36 (minSdk 24) |
| Tests | JUnit 4 · Espresso · Compose UI Test |

---

## Lancer le projet

### Prérequis

- Android Studio Hedgehog (ou version plus récente)
- JDK 11
- Un émulateur Android (API 24+) ou un appareil physique

### Étapes

```bash
# 1. Cloner le dépôt
git clone https://github.com/malik630/Airline-Online-Check-In-Mobile-App

# 2. Ouvrir le projet dans Android Studio
# File > Open > sélectionner le dossier Check-In-Mobile-App/

# 3. Synchroniser Gradle
# Cliquer sur "Sync Now" dans la barre de notification

# 4. Lancer l'application
# Run > Run 'app'
```

> *Ce projet a été réalisé dans le cadre du module Techniques de Développement Mobile — ESI Alger.*
