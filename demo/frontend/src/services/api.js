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
    
    // LOG REQUEST DEBUG
    console.log(`[API REQUEST] ${config.method.toUpperCase()} ${config.baseURL || ''}${config.url}`);
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log(`[API REQUEST] Authorization Token Added.`);
    } else {
      console.log(`[API REQUEST] No token found in storage.`);
    }
    
    console.log(`[API REQUEST] Headers:`, config.headers);
    
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => {
    // LOG RESPONSE SUCCESS DEBUG
    console.log(`[API RESPONSE] ${response.status} OK - ${response.config.url}`);
    return response;
  },
  (error) => {
    // LOG RESPONSE ERROR DEBUG
    console.error(`[API ERROR] Failed URL: ${error.config?.baseURL || ''}${error.config?.url}`);
    console.error(`[API ERROR] Status: ${error.response?.status}`);
    console.error(`[API ERROR] Message: ${error.message}`);
    
    if (error.response) {
      console.error(`[API ERROR] Backend Response:`, error.response.data);
      
      if (error.response.status === 401) {
        console.warn('Non autorisé. Suppression du token.');
        storage.removeToken();
        storage.removeUser();
      }
      if (error.response.status === 403) {
        console.warn('Accès interdit.');
      }
    } else if (error.request) {
      console.error(`[API ERROR] No response received from backend (Possible CORS or Network Error). Request:`, error.request);
    }
    
    return Promise.reject(error);
  }
);

export default api;
