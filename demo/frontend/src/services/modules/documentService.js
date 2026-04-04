import api from '../api';

const BASE_PATH = '/documents';

export const documentService = {
  // GET /api/documents (By ProjetId)
  getByProjet: async (projetId) => {
    const response = await api.get(BASE_PATH, { params: { projetId } });
    return response.data;
  },
  
  // GET /api/documents/{id}
  getById: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}`);
    return response.data;
  },
  
  // POST /api/documents (Généralement multipart formData)
  create: async (formData) => {
    const response = await api.post(BASE_PATH, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
  },
  
  // DELETE /api/documents/{id}
  delete: async (id) => {
    const response = await api.delete(`${BASE_PATH}/${id}`);
    return response.data;
  },

  // GET /api/documents/{id}/telecharger
  // Important : responseType spécifié en 'blob' pour télécharger des fichiers
  download: async (id) => {
    const response = await api.get(`${BASE_PATH}/${id}/telecharger`, {
      responseType: 'blob'
    });
    return response.data; 
  }
};
