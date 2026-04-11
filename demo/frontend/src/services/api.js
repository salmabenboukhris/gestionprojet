import axios from 'axios';
import { API_BASE_URL } from '../utils/constants';
import { storage } from '../utils/storage';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = storage.getToken();

    if (import.meta.env.DEV) {
      console.log(`[API REQUEST] ${config.method.toUpperCase()} ${config.baseURL || ''}${config.url}`);
    }

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => {
    if (import.meta.env.DEV) {
      console.log(`[API RESPONSE] ${response.status} OK - ${response.config.url}`);
    }
    return response;
  },
  (error) => {
    if (import.meta.env.DEV) {
      console.error(`[API ERROR] ${error.config?.method?.toUpperCase()} ${error.config?.url} → ${error.response?.status}`);
      if (error.response?.data) {
        console.error(`[API ERROR] Backend:`, error.response.data);
      }
    }

    if (error.response) {
      if (error.response.status === 401) {
        // Token expiré ou invalide → nettoyage et redirection
        storage.removeToken();
        storage.removeUser();
        // Redirection vers login si pas déjà sur la page login
        if (!window.location.pathname.includes('/login')) {
          window.location.href = '/login';
        }
      }
    } else if (error.request && import.meta.env.DEV) {
      console.error(`[API ERROR] Pas de réponse du backend (CORS ou réseau).`);
    }

    return Promise.reject(error);
  }
);

export default api;
