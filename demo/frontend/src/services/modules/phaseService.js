import api from '../api';

const BASE_PATH = '/phases';

export const phaseService = {
  // GET /api/phases
  getAll: async (params) => {
    const response = await api.get(BASE_PATH, { params });
    return response.data;
  },
  
  // GET /api/phases/{id}
  getById: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}`);
    return response.data;
  },
  
  // POST /api/phases
  create: async (data) => {
    const response = await api.post(BASE_PATH, data);
    return response.data;
  },
  
  // PUT /api/phases/{id}
  update: async (id, data) => {
    const response = await api.put(`${BASE_PATH}/${id}`, data);
    return response.data;
  },
  
  // DELETE /api/phases/{id}
  delete: async (id) => {
    const response = await api.delete(`${BASE_PATH}/${id}`);
    return response.data;
  },

  // PATCH /api/phases/{id}/realise
  markAsRealise: async (id) => {
    const response = await api.patch(`${BASE_PATH}/${id}/realise`);
    return response.data;
  },

  // PATCH /api/phases/{id}/facture
  markAsFacture: async (id) => {
    const response = await api.patch(`${BASE_PATH}/${id}/facture`);
    return response.data;
  },

  // PATCH /api/phases/{id}/paye
  markAsPaye: async (id) => {
    const response = await api.patch(`${BASE_PATH}/${id}/paye`);
    return response.data;
  }
};
