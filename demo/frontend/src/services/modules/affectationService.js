import api from '../api';

export const affectationService = {
  getByEmploye: async (employeId) => {
    console.log(`[affectationService] GET /employes/${employeId}/phases`);
    const response = await api.get(`/employes/${employeId}/phases`);
    return response.data;
  },

  getByPhase: async (phaseId) => {
    console.log(`[affectationService] GET /phases/${phaseId}/employes`);
    const response = await api.get(`/phases/${phaseId}/employes`);
    return response.data;
  },

  create: async (phaseId, employeId, data) => {
    console.log(`[affectationService] POST /phases/${phaseId}/employes/${employeId}`, data);
    const response = await api.post(`/phases/${phaseId}/employes/${employeId}`, data);
    return response.data;
  },

  delete: async (phaseId, employeId) => {
    console.log(`[affectationService] DELETE /phases/${phaseId}/employes/${employeId}`);
    const response = await api.delete(`/phases/${phaseId}/employes/${employeId}`);
    return response.data;
  }
};
