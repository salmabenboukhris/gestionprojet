import api from '../api';

export const documentService = {
  getByProjet: async (projetId) => {
    console.log(`[documentService] GET /projets/${projetId}/documents`);
    const response = await api.get(`/projets/${projetId}/documents`);
    return response.data;
  },
  
  getById: async (id) => {
    console.log(`[documentService] GET /documents/${id}`);
    const response = await api.get(`/documents/${id}`);
    return response.data;
  },
  
  create: async (projetId, formData) => {
    // Log du contenu du FormData pour le débogage (les Files ne s'affichent pas en direct dans un log normal)
    const formDataEntries = {};
    formData.forEach((value, key) => { formDataEntries[key] = value instanceof File ? `File(${value.name})` : value; });
    
    console.log(`[documentService] POST /projets/${projetId}/documents | Payload:`, formDataEntries);
    
    // Axios avec FormData calcule automatiquement le boundary requis.
    // NE PAS définir le header 'Content-Type': 'multipart/form-data', c'est la source de l'erreur dans Axios.
    const response = await api.post(`/projets/${projetId}/documents`, formData);
    return response.data;
  },
  
  delete: async (id) => {
    console.log(`[documentService] DELETE /documents/${id}`);
    const response = await api.delete(`/documents/${id}`);
    return response.data;
  },

  download: async (id) => {
    console.log(`[documentService] GET /documents/${id}/download`);
    const response = await api.get(`/documents/${id}/download`, {
      responseType: 'blob'
    });
    return response.data; 
  }
};
