import api from '../api';

/**
 * Phase Service — matches real backend PhaseController:
 *
 * POST   /api/projets/{projetId}/phases          → create
 * GET    /api/projets/{projetId}/phases          → list by projet
 * GET    /api/phases/{id}                        → get one
 * PUT    /api/phases/{id}                        → update
 * DELETE /api/phases/{id}                        → delete
 * PATCH  /api/phases/{id}/realisation            → mark as réalisée {value: true}
 * PATCH  /api/phases/{id}/facturation            → mark as facturée {value: true}
 * PATCH  /api/phases/{id}/paiement              → mark as payée    {value: true}
 */
export const phaseService = {

  /** GET all phases for a specific project */
  getByProjet: async (projetId) => {
    const response = await api.get(`/projets/${projetId}/phases`);
    return response.data;
  },

  /** GET all phases across all projects — uses new single endpoint */
  getAll: async (params = {}) => {
    // If projetId given, fetch for that specific project
    if (params.projetId) {
      const response = await api.get(`/projets/${params.projetId}/phases`);
      return response.data;
    }
    // Otherwise use the efficient GET /api/phases endpoint (no N+1)
    const response = await api.get('/phases');
    return response.data;
  },

  /** GET /api/phases/{id} */
  getById: async (id) => {
    const response = await api.get(`/phases/${id}`);
    return response.data;
  },

  /** POST /api/projets/{projetId}/phases */
  create: async (projetId, data) => {
    const response = await api.post(`/projets/${projetId}/phases`, data);
    return response.data;
  },

  /** PUT /api/phases/{id} */
  update: async (id, data) => {
    const response = await api.put(`/phases/${id}`, data);
    return response.data;
  },

  /** DELETE /api/phases/{id} */
  delete: async (id) => {
    const response = await api.delete(`/phases/${id}`);
    return response.data;
  },

  /** PATCH /api/phases/{id}/realisation — body: {value: true} */
  markAsRealise: async (id) => {
    const response = await api.patch(`/phases/${id}/realisation`, { value: true });
    return response.data;
  },

  /** PATCH /api/phases/{id}/facturation — body: {value: true} */
  markAsFacture: async (id) => {
    const response = await api.patch(`/phases/${id}/facturation`, { value: true });
    return response.data;
  },

  /** PATCH /api/phases/{id}/paiement — body: {value: true} */
  markAsPaye: async (id) => {
    const response = await api.patch(`/phases/${id}/paiement`, { value: true });
    return response.data;
  },
};
