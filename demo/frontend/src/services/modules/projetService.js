import api from '../api';

const BASE_PATH = '/projets';

export const projetService = {
  // GET /api/projets
  getAll: async (params) => {
    const response = await api.get(BASE_PATH, { params });
    return response.data;
  },
  
  // GET /api/projets/{id}
  getById: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}`);
    return response.data;
  },
  
  // GET /api/projets/{id}/resume
  getResume: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}/resume`);
    return response.data;
  },
  
  // POST /api/projets
  create: async (data) => {
    const response = await api.post(BASE_PATH, data);
    return response.data;
  },
  
  // PUT /api/projets/{id}
  update: async (id, data) => {
    const response = await api.put(`${BASE_PATH}/${id}`, data);
    return response.data;
  },
  
  // DELETE /api/projets/{id}
  delete: async (id) => {
    const response = await api.delete(`${BASE_PATH}/${id}`);
    return response.data;
  }
};
