/**
 * Store global applicatif.
 *
 * L'application utilise principalement Context API (AuthContext)
 * pour la gestion de l'état d'authentification.
 *
 * Ce fichier centralise l'état global partagé entre plusieurs composants
 * qui ne sont pas liés par une relation parent-enfant directe.
 */

// État initial global
export const initialState = {
  // Filtres globaux persistants entre les pages
  filters: {
    projetId: null,
    phaseId: null,
    chefProjetId: null,
    dateDebut: null,
    dateFin: null,
  },

  // Préférences UI
  ui: {
    sidebarCollapsed: false,
    theme: 'light',
  },

  // Cache léger pour éviter des rechargements inutiles
  cache: {
    projets: null,
    organismes: null,
    employes: null,
    lastFetched: {},
  },
};

/**
 * Reducer simple pour la gestion d'état global.
 * Utilisable avec useReducer() dans un Context ou composant racine.
 */
export function appReducer(state = initialState, action) {
  switch (action.type) {
    case 'SET_FILTER':
      return {
        ...state,
        filters: { ...state.filters, [action.key]: action.value },
      };

    case 'RESET_FILTERS':
      return {
        ...state,
        filters: { ...initialState.filters },
      };

    case 'TOGGLE_SIDEBAR':
      return {
        ...state,
        ui: { ...state.ui, sidebarCollapsed: !state.ui.sidebarCollapsed },
      };

    case 'SET_CACHE':
      return {
        ...state,
        cache: {
          ...state.cache,
          [action.key]: action.data,
          lastFetched: { ...state.cache.lastFetched, [action.key]: Date.now() },
        },
      };

    case 'CLEAR_CACHE':
      return {
        ...state,
        cache: { ...initialState.cache },
      };

    default:
      return state;
  }
}

// ── Action creators ──────────────────────────────────────────────────────────

export const setFilter = (key, value) => ({ type: 'SET_FILTER', key, value });
export const resetFilters = () => ({ type: 'RESET_FILTERS' });
export const toggleSidebar = () => ({ type: 'TOGGLE_SIDEBAR' });
export const setCache = (key, data) => ({ type: 'SET_CACHE', key, data });
export const clearCache = () => ({ type: 'CLEAR_CACHE' });
