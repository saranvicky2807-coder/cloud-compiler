import api from './api';

export const projectService = {
  async getProjects() {
    const res = await api.get('/projects');
    return res.data;
  },

  async getProject(id) {
    const res = await api.get(`/projects/${id}`);
    return res.data;
  },

  async createProject(data) {
    const res = await api.post('/projects', data);
    return res.data;
  },

  async updateProject(id, data) {
    const res = await api.put(`/projects/${id}`, data);
    return res.data;
  },

  async deleteProject(id) {
    const res = await api.delete(`/projects/${id}`);
    return res.data;
  },

  // Files
  async getProjectFiles(projectId) {
    const res = await api.get(`/projects/${projectId}/files`);
    return res.data;
  },

  async createFile(projectId, data) {
    const res = await api.post(`/projects/${projectId}/files`, data);
    return res.data;
  },

  async updateFileContent(fileId, content) {
    const res = await api.put(`/files/${fileId}`, { content });
    return res.data;
  },

  async renameFile(fileId, name) {
    const res = await api.put(`/files/${fileId}/rename`, { name });
    return res.data;
  },

  async deleteFile(fileId) {
    const res = await api.delete(`/files/${fileId}`);
    return res.data;
  },

  // Dashboard & History
  async getDashboardStats() {
    const res = await api.get('/dashboard/stats');
    return res.data;
  },

  async getHistory() {
    const res = await api.get('/history');
    return res.data;
  },

  async deleteHistory(id) {
    const res = await api.delete(`/history/${id}`);
    return res.data;
  },

  async clearHistory() {
    const res = await api.delete('/history/clear');
    return res.data;
  }
};
