# Suivi Projet

## Description

**Suivi Projet** est une application web de gestion et de suivi de projets destinée aux entreprises souhaitant piloter leurs projets, phases, ressources humaines et finances depuis une interface centralisée.

La problématique adressée est la suivante : comment assurer un suivi rigoureux du cycle de vie d'un projet — de sa création jusqu'au paiement des phases — tout en contrôlant les accès selon les rôles des utilisateurs ?


---

## Objectifs

- Centraliser la gestion des projets, phases, livrables, documents et factures dans une seule application
- Implémenter un contrôle d'accès basé sur les rôles (RBAC) avec 5 profils distincts
- Automatiser le workflow financier des phases : Réalisation → Facturation → Paiement
- Offrir un tableau de bord et des rapports en temps réel pour les décideurs
- Conteneuriser l'application pour faciliter le déploiement via Docker

---

## Architecture du projet

### Backend (Spring Boot) — *Architecture backend Spring Boot · Repositories et accès aux données*
- API REST exposée sur le port **8082**
- Architecture en couches : Controller → Service → Repository → Entity
- Repositories Spring Data JPA pour chaque entité (Projet, Phase, Employe, Affectation, Facture, Livrable, Document, Organisme, Profil)
- Sécurité JWT avec filtre HTTP personnalisé
- Gestion centralisée des exceptions (`GlobalExceptionHandler`)
- Documentation auto-générée via Swagger / OpenAPI 3

### Frontend (React + Vite) — *Architecture frontend React · Intégration frontend backend*
- Interface SPA (Single Page Application) servie par Nginx sur le port **3000**
- Routing côté client géré par React Router
- Appels API centralisés via Axios avec intercepteurs JWT
- Proxy Nginx (`/api/*` → `backend:8082`) assurant l'intégration frontend ↔ backend sans CORS
- Contrôle d'accès aux routes selon le rôle de l'utilisateur connecté

### Base de données — *Modélisation JPA et base de données*
- MySQL 8.0 avec schéma auto-géré par Hibernate (`ddl-auto: update`)
- Base de données : `suivi_projet_db`
- 9 entités JPA mappées : `Projet`, `Phase`, `Employe`, `Affectation` (clé composite `AffectationId`), `Livrable`, `Document`, `Facture`, `Organisme`, `Profil`
- Relations : `@ManyToOne`, `@OneToMany`, `@OneToOne`, `@EmbeddedId` avec contraintes d'intégrité référentielle

### Infrastructure (Docker) — *Conteneurisation*
- 3 services orchestrés : `db`, `backend`, `frontend`
- Réseau interne Docker (`suivi-network`) pour la communication inter-services
- Healthcheck MySQL garantissant le démarrage ordonné des services
- Volume persistant pour les données MySQL

---

## Schéma de l'architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Network                        │
│                                                         │
│  ┌──────────────┐    /api/*     ┌──────────────────┐    │
│  │   Frontend   │─────proxy────▶│    Backend       │    │
│  │  React/Vite  │               │  Spring Boot     │    │
│  │  Nginx :80   │               │  Port 8082       │    │
│  └──────────────┘               └────────┬─────────┘    │
│         │                                │              │
│    Port 3000                        JDBC │              │
│   (externe)                             ▼              │
│                               ┌──────────────────┐     │
│                               │   Base de données│     │
│                               │   MySQL 8.0      │     │
│                               │   Port 3306      │     │
│                               └──────────────────┘     │
│                                        │               │
│                                   Port 3307            │
│                                   (externe)            │
└─────────────────────────────────────────────────────────┘

Utilisateur ──▶ http://localhost:3000  (Frontend)
               http://localhost:8082  (API Backend)
               localhost:3307         (MySQL - accès direct)
```

---

## Technologies utilisées

### Backend
- Java 17
- Spring Boot 3
- Spring Security (JWT / RBAC)
- Spring Data JPA / Hibernate
- MySQL Connector
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- Maven

### Frontend
- React 19
- Vite 8
- React Router DOM 7
- Axios
- Ant Design 6
- React Hook Form + Zod
- Node 18

### Base de données
- MySQL 8.0

### DevOps
- Docker & Docker Compose
- Nginx 1.25 (reverse proxy + serveur statique)
- Maven multi-stage build
- Node multi-stage build

---

## Structure du projet

```
demo/
│
├── docker-compose.yml
│
├
├── Dockerfile
├── pom.xml
└── src/
│       └── main/
│           ├── java/ma/toubkalit/suiviprojet/
│           │   ├── controllers/        ← Endpoints REST
│           │   ├── services/           ← Logique métier
│           │   │   └── impl/
│           │   ├── entities/           ← Entités JPA
│           │   ├── repositories/       ← Accès BDD
│           │   ├── dto/                ← Objets de transfert
│           │   ├── mappers/            ← Conversion entity ↔ dto
│           │   ├── security/           ← JWT, filtres, config
│           │   ├── exceptions/         ← Gestion des erreurs
│           │   └── config/             ← OpenAPI, données test
│           └── resources/
│               └── application.properties
│
└── frontend/
    ├── Dockerfile
    ├── nginx.conf
    ├── package.json
    ├── index.html
    └── src/
        ├── components/     ← Composants réutilisables
        ├── pages/          ← Pages de l'application
        ├── services/       ← Appels API (Axios)
        ├── context/        ← AuthContext (état global)
        ├── guards/         ← Protection des routes
        ├── hooks/          ← Hooks personnalisés
        ├── layouts/        ← Mise en page (Auth / Main)
        ├── routes/         ← Configuration du routage
        └── utils/          ← Constantes, helpers
```

---

## Installation et exécution

### Prérequis
- Docker Desktop installé et démarré
- Docker Compose v2+
- (Sans Docker) Java 17, Maven 3.8+, Node 18+, MySQL 8 local

### Étapes d'installation

```bash
git clone https://github.com/salmabenboukhris/gestionprojet.git
cd suivi-projet
```


### Lancer avec Docker

```bash
docker-compose up --build
```

L'application sera accessible sur :
- Frontend → http://localhost:3000
- API Backend → http://localhost:8082
- Swagger UI → http://localhost:8082/swagger-ui/index.html
- MySQL (externe) → localhost:3307

### Lancer sans Docker

#### Backend

```bash
cd demo
mvn spring-boot:run
```


#### Frontend

```bash
cd frontend
npm install
npm run dev
```



---

## Sécurité — *Sécurité backend · Sécurité frontend*

- **Type d'authentification** : JWT (JSON Web Token) — token généré à la connexion, transmis dans le header `Authorization: Bearer <token>`, validité de 24h
- **Gestion des rôles (backend)** : 5 rôles distincts avec accès restreints par endpoint via `SecurityConfig` (Spring Security)

| Rôle | Accès |
|------|-------|
| `ADMIN` | Accès complet à toutes les ressources |
| `SECRETAIRE` | Organismes, Projets |
| `CHEF_PROJET` | Projets, Phases |
| `COMPTABLE` | Factures |
| `DIRECTEUR` | Reporting et tableau de bord |

- **Protection des routes (frontend)** : `AuthGuard` bloque l'accès aux pages sans token, `RoleGuard` restreint l'accès selon le rôle de l'utilisateur connecté
- **Hashage des mots de passe** : BCrypt via `PasswordEncoder` de Spring Security

---

## Fonctionnalités principales — *Fonctionnalités backend métier · Fonctionnalités frontend · Validation et gestion des erreurs*

- Gestion complète des **organismes** clients (CRUD avec recherche par code, nom, contact)
- Gestion complète des **projets** avec validation des dates et du budget
- Gestion des **phases** d'un projet avec contrôle du montant cumulé (≤ budget projet)
- **Affectation d'employés** aux phases avec détection des chevauchements de disponibilité
- Gestion des **livrables** et **documents** attachés aux phases et projets
- Workflow de **facturation** : une phase doit être réalisée avant d'être facturée, et facturée avant d'être payée
- Gestion des **employés** avec recherche par matricule, login, email ou nom
- Changement de mot de passe et consultation du profil connecté
- **Validation métier** : vérification de la cohérence des dates, des budgets et des états avant chaque opération
- **Gestion des erreurs** : exceptions typées (`ResourceNotFoundException`, `DuplicateResourceException`, `OperationNotAllowedException`) remontées en JSON structuré via `GlobalExceptionHandler`

### Dashboard / Reporting — *Dashboard et reporting*

- Tableau de bord global : nombre de projets en cours / clôturés, montants totaux, état des phases
- Liste des phases **terminées non facturées** (avec filtres par date, projet, chef de projet)
- Liste des phases **facturées non payées**
- Liste des phases **payées**
- Liste des projets **en cours** et **clôturés**

---

## Vidéo de démonstration



---

## API Documentation (Swagger) — *Swagger / OpenAPI*

Lien : [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

> La documentation est protégée par JWT. Connectez-vous via `POST /api/auth/login`, copiez le token retourné et cliquez sur "Authorize" dans Swagger UI pour tester les endpoints.

---

## Tests

> - Tests manuels via Swagger UI


---

## Conteneurisation — *Conteneurisation*

- **Dockerfile backend** : Build multi-stage Maven → JRE 17 Alpine. Le JAR est compilé sans les tests puis copié dans une image légère avec un utilisateur non-root.
- **Dockerfile frontend** : Build multi-stage Node 18 → Nginx Alpine. Vite génère le dossier `dist/` qui est servi statiquement par Nginx.
- **docker-compose.yml** : Orchestre les 3 services (`db`, `backend`, `frontend`) sur un réseau interne partagé. Le backend attend que MySQL soit sain via `healthcheck` avant de démarrer. Les variables d'environnement surchargent `application.properties` sans modifier le code source.

---

## Auteurs



- Nom 1 : BAJADDA Asma 
- Nom 2 : Prénom NOM 
- Nom 3 : Prénom NOM 

---
