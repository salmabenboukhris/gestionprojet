import api from '../api';

const BASE_PATH = '/organismes';

export const organismeService = {
  // GET /api/organismes - Liste les organismes (supporte params code, nom, contact)
  getAll: async (params) => {
    const response = await api.get(BASE_PATH, { params });
    return response.data;
  },
  
  // GET /api/organismes/{id} - Un organisme
  getById: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}`);
    return response.data;
  },
  
  // POST /api/organismes - Créer un organisme
  create: async (data) => {
    const response = await api.post(BASE_PATH, data);
    return response.data;
  },
  
  // PUT /api/organismes/{id} - Mettre à jour
  update: async (id, data) => {
    const response = await api.put(`${BASE_PATH}/${id}`, data);
    return response.data;
  },
  
  // DELETE /api/organismes/{id} - Supprimer
  delete: async (id) => {
    const response = await api.delete(`${BASE_PATH}/${id}`);
    return response.data;
  }
};
