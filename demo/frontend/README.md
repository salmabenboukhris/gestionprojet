# Projet Frontend - Suivi de Projets (Phase 1)

Ce projet est la base de l'interface client frontend conçue avec React (Vite) en correspondance directe avec le backend Spring Boot existant.

La structure des dossiers respecte une stricte séparation des préoccupations, incluant d'ores et déjà :
- L'injection d'Axios (`src/services/api.js`).
- Le React Router DOM v6 mis en page dans `AppRoutes`.
- Des services API par domaine (mocks pour le moment) (`src/services/modules/*`).
- Les Layouts primaires (`MainLayout`, `AuthLayout`).
- Des composants Ant Design factorisés (AppTable, AppModal).
- Un contexte d'authentification et des Guards pré-construits.
- Les pages placeholders pour l'ensemble des modules.

## Architecture

* `src/components/common` : Éléments purement UI réutilisables.
* `src/context` : Store React natif (ex. `AuthContext`).
* `src/guards` : Composants filtrant l'accès aux routes privées (ex. `AuthGuard`).
* `src/hooks` : Hooks React custom.
* `src/layouts` : Structures visuelles parente (Menu, Header).
* `src/pages` : Code pointant vers les différentes routes (le contenu des vues).
* `src/routes` : Configuration du routeur (Table des routes).
* `src/services` : Connexion aux webservices distants avec Axios.
* `src/utils` : Fonctions pure, aide locales, variables d'environnements (Constantes).

## Exécution
```bash
npm install
npm run dev
```
La phase 1 ne contient que la structure minimaliste sans implémenter la véritable connexion backend (Phase 2).
