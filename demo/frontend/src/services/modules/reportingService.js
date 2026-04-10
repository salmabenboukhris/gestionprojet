import api from '../api';

/**
 * Reporting Service — matches backend /api/reporting endpoints
 * See ReportingController.java
 */
const BASE_PATH = '/reporting';

export const reportingService = {

  // GET /api/reporting/tableau-de-bord
  getTableauDeBord: async () => {
    const response = await api.get(`${BASE_PATH}/tableau-de-bord`);
    return response.data;
  },

  // GET /api/reporting/projets/en-cours
  getProjetsEnCours: async () => {
    const response = await api.get(`${BASE_PATH}/projets/en-cours`);
    return response.data;
  },

  // GET /api/reporting/projets/clotures
  getProjetsClotures: async () => {
    const response = await api.get(`${BASE_PATH}/projets/clotures`);
    return response.data;
  },

  // GET /api/reporting/phases/terminees-non-facturees
  getPhasesTermineesNonFacturees: async (params = {}) => {
    const response = await api.get(`${BASE_PATH}/phases/terminees-non-facturees`, { params });
    return response.data;
  },

  // GET /api/reporting/phases/facturees-non-payees
  getPhasesFactureesNonPayees: async (params = {}) => {
    const response = await api.get(`${BASE_PATH}/phases/facturees-non-payees`, { params });
    return response.data;
  },

  // GET /api/reporting/phases/payees
  getPhasesPayees: async (params = {}) => {
    const response = await api.get(`${BASE_PATH}/phases/payees`, { params });
    return response.data;
  },
};
