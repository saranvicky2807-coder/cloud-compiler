import api from './api';

export const compilerService = {
  async compile(data) {
    const res = await api.post('/compiler/compile', data);
    return res.data;
  },

  async tokenize(data) {
    const res = await api.post('/compiler/tokenize', data);
    return res.data;
  },

  async parse(data) {
    const res = await api.post('/compiler/parse', data);
    return res.data;
  },

  async analyze(data) {
    const res = await api.post('/compiler/analyze', data);
    return res.data;
  },

  async generateIr(data) {
    const res = await api.post('/compiler/generate-ir', data);
    return res.data;
  }
};
