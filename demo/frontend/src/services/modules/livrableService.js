import api from '../api';

const BASE_PATH = '/livrables';

export const livrableService = {
  // GET /api/livrables
  getAll: async (params) => {
    const response = await api.get(BASE_PATH, { params });
    return response.data;
  },
  
  // GET /api/livrables/{id}
  getById: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}`);
    return response.data;
  },
  
  // POST /api/livrables
  create: async (data) => {
    const response = await api.post(BASE_PATH, data);
    return response.data;
  },
  
  // PUT /api/livrables/{id}
  update: async (id, data) => {
    const response = await api.put(`${BASE_PATH}/${id}`, data);
    return response.data;
  },
  
  // DELETE /api/livrables/{id}
  delete: async (id) => {
    const response = await api.delete(`${BASE_PATH}/${id}`);
    return response.data;
  }
};
