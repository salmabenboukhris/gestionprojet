import api from '../api';

export const factureService = {
  // GET /api/factures
  getAll: async () => {
    console.log(`[factureService] GET /factures`);
    const response = await api.get(`/factures`);
    return response.data;
  },
  
  // GET /api/factures/{id}
  getById: async (id) => {
    console.log(`[factureService] GET /factures/${id}`);
    const response = await api.get(`/factures/${id}`);
    return response.data;
  },
  
  // POST /api/phases/{phaseId}/facture
  create: async (phaseId, data) => {
    console.log(`[factureService] POST /phases/${phaseId}/facture`, data);
    const response = await api.post(`/phases/${phaseId}/facture`, data);
    return response.data;
  },
  
  // PUT /api/factures/{id}
  update: async (id, data) => {
    console.log(`[factureService] PUT /factures/${id}`, data);
    const response = await api.put(`/factures/${id}`, data);
    return response.data;
  }
};
