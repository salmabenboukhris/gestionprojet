import api from '../api';

const BASE_PATH = '/auth';

export const authService = {
  // Endpoint de connexion
  login: async (loginData) => {
    const response = await api.post(`${BASE_PATH}/login`, loginData);
    return response.data;
  },
  
  // Endpoint pour récupérer l'utilisateur courant via JWT
  getCurrentUser: async () => {
    const response = await api.get(`${BASE_PATH}/me`);
    return response.data;
  },
  
  // Endpoint pour changer le mot de passe
  changePassword: async (passwordData) => {
    const response = await api.post(`${BASE_PATH}/change-password`, passwordData);
    return response.data;
  }
};
