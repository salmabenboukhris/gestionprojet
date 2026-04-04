import api from '../api';

const BASE_PATH = '/employes';

export const employeService = {
  // GET /api/employes - Liste des collaborateurs
  getAll: async (params) => {
    const response = await api.get(BASE_PATH, { params });
    return response.data;
  },
  
  // GET /api/employes/{id}
  getById: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}`);
    return response.data;
  },
  
  // GET /api/employes/disponibles - Chercher selon période
  getDisponibles: async (dateDebut, dateFin) => {
    const response = await api.get(`${BASE_PATH}/disponibles`, { 
      params: { dateDebut, dateFin } 
    });
    return response.data;
  },
  
  // POST /api/employes
  create: async (data) => {
    const response = await api.post(BASE_PATH, data);
    return response.data;
  },
  
  // PUT /api/employes/{id}
  update: async (id, data) => {
    const response = await api.put(`${BASE_PATH}/${id}`, data);
    return response.data;
  },
  
  // DELETE /api/employes/{id}
  delete: async (id) => {
    const response = await api.delete(`${BASE_PATH}/${id}`);
    return response.data;
  }
};
