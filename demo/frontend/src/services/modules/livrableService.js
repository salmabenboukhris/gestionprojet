import api from '../api';

export const livrableService = {
  // GET /api/phases/{phaseId}/livrables
  getByPhase: async (phaseId) => {
    console.log(`[livrableService] GET /phases/${phaseId}/livrables`);
    const response = await api.get(`/phases/${phaseId}/livrables`);
    return response.data;
  },
  
  // GET /api/livrables/{id}
  getById: async (id) => {
    console.log(`[livrableService] GET /livrables/${id}`);
    const response = await api.get(`/livrables/${id}`);
    return response.data;
  },
  
  // POST /api/phases/{phaseId}/livrables
  create: async (phaseId, data) => {
    console.log(`[livrableService] POST /phases/${phaseId}/livrables`, data);
    const response = await api.post(`/phases/${phaseId}/livrables`, data);
    return response.data;
  },
  
  // PUT /api/livrables/{id}
  update: async (id, data) => {
    console.log(`[livrableService] PUT /livrables/${id}`, data);
    const response = await api.put(`/livrables/${id}`, data);
    return response.data;
  },
  
  // DELETE /api/livrables/{id}
  delete: async (id) => {
    console.log(`[livrableService] DELETE /livrables/${id}`);
    const response = await api.delete(`/livrables/${id}`);
    return response.data;
  }
};
