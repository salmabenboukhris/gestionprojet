import api from '../api';

const BASE_PATH = '/factures';

export const factureService = {
  // GET /api/factures
  getAll: async (params) => {
    const response = await api.get(BASE_PATH, { params });
    return response.data;
  },
  
  // GET /api/factures/{id}
  getById: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}`);
    return response.data;
  },
  
  // POST /api/factures
  create: async (data) => {
    const response = await api.post(BASE_PATH, data);
    return response.data;
  },
  
  // PUT /api/factures/{id}
  update: async (id, data) => {
    const response = await api.put(`${BASE_PATH}/${id}`, data);
    return response.data;
  },
  
  // DELETE /api/factures/{id}
  delete: async (id) => {
    const response = await api.delete(`${BASE_PATH}/${id}`);
    return response.data;
  }
};
