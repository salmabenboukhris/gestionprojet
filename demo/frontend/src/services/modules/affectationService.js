import api from '../api';

const BASE_PATH = '/affectations';

export const affectationService = {
  // GET /api/affectations/employe/{employeId}
  getByEmploye: async (employeId) => {
    const response = await api.get(`${BASE_PATH}/employe/${employeId}`);
    return response.data;
  },

  // GET /api/affectations/phase/{phaseId}
  getByPhase: async (phaseId) => {
    const response = await api.get(`${BASE_PATH}/phase/${phaseId}`);
    return response.data;
  },

  // POST /api/affectations
  // Attend DTO : { employeId, phaseId, dateDebut, dateFin }
  create: async (data) => {
    const response = await api.post(BASE_PATH, data);
    return response.data;
  },

  // DELETE /api/affectations/{employeId}/{phaseId}
  // Gère la suppression via clé composée
  delete: async (employeId, phaseId) => {
    const response = await api.delete(`${BASE_PATH}/${employeId}/${phaseId}`);
    return response.data;
  }
};
