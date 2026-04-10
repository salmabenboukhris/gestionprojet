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
    // ✅ Endpoint multipart correct : /documents/upload
    const response = await api.post(`/projets/${projetId}/documents/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
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
