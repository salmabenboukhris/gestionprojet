import api from '../api';

const BASE_PATH = '/reporting';

export const reportingService = {
  // Global KPIs (GET /api/reporting/kpis)
  getGlobalKpis: async () => {
    const response = await api.get(`${BASE_PATH}/kpis`);
    return response.data;
  },
  
  // Get active projects metrics (example, to adjust with real backend analysis)
  getProjetsActifs: async () => {
    const response = await api.get(`${BASE_PATH}/projets-actifs`);
    return response.data;
  },
  
  // Chiffre d'affaires
  getChiffreAffaires: async (year) => {
    const response = await api.get(`${BASE_PATH}/chiffre-affaires`, { params: { annee: year }});
    return response.data;
  }
};
