# Airline Online Check-In — Mobile App

> Projet · 2CS SIL · Techniques de Développement Mobile  
> École Nationale Supérieure d'Informatique (ESI)

Application Android native permettant aux passagers d'effectuer leur enregistrement en ligne avant leur vol, de sélectionner leur siège, de déclarer leurs bagages et d'obtenir leur carte d'embarquement numérique directement depuis leur smartphone.

---

## Structure du projet

Le projet suit une **architecture Clean Architecture** organisée en trois modules Gradle indépendants :

```
Check-In-Mobile-App/
├── app/                        # Module principal (couche Présentation)
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/check_in_mobile_app/
│       │   ├── di/             # Injection de dépendances
│       │   ├── presentation/   # Écrans et ViewModels Compose
│       │   │   ├── auth/           # Inscription & Connexion (email / Google)
│       │   │   ├── flight/         # Recherche & récupération de réservation
│       │   │   ├── checkin/        # Processus d'enregistrement (scan passeport, siège, bagages…)
│       │   │   ├── boarding/       # Carte d'embarquement & QR Code
│       │   │   ├── notification/   # Notifications de confirmation
│       │   │   └── navigation/     # Graphe de navigation Compose
│       │   └── ui/theme/           # Couleurs, typographie, thème Material 3
│       └── res/                # Ressources (drawables, valeurs, XML)
│
├── domain/                     # Module domaine (logique métier pure)
│   └── src/main/java/com/example/domain/
│       ├── model/              # Entités métier (Flight, Passenger, BoardingPass…)
│       ├── repository/         # Interfaces des repositories
│       └── usecase/            # Cas d'utilisation (CheckInUseCase, GetFlightUseCase…)
|       └── state/ 
|       └── factory/ 
|       └── strategy/ 
|       └── validation/ 
│
├── data/                       # Module données (implémentations & sources)
│   └── src/main/java/com/example/data/
│       ├── remote/             # API REST, DTOs
│       ├── local/              # Base de données locale (Room), cache offline
│       └── repository/        # Implémentations des repositories du domaine
│
├── gradle/
│   └── libs.versions.toml      # Catalogue de versions des dépendances
├── build.gradle.kts            # Configuration Gradle racine
├── settings.gradle.kts         # Déclaration des modules
└── README.md
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
