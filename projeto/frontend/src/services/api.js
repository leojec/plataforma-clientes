import axios from 'axios';

// Normalizar e limpar a URL da API (remover caracteres invisíveis)
const normalizeURL = (url) => {
  if (!url) return 'http://localhost:8080/api';
  // Remove caracteres zero-width e espaços invisíveis
  return url.replace(/[\u200B-\u200D\uFEFF\u200C\u200D]/g, '').trim().replace(/\/+$/, '');
};

const baseURL = normalizeURL(process.env.REACT_APP_API_URL || 'http://localhost:8080/api');
// Garantir que termina com /api
const finalBaseURL = baseURL.endsWith('/api') ? baseURL : `${baseURL}/api`;

const api = axios.create({
  baseURL: finalBaseURL,
  timeout: 10000,
});

// Interceptor para adicionar token automaticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para tratar respostas
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    
    // Garantir que o erro seja uma string ou objeto simples
    if (error.response?.data) {
      const errorData = error.response.data;
      if (typeof errorData === 'object' && errorData !== null) {
        // Se for um objeto complexo, converter para string
        error.message = errorData.message || errorData.error || 'Erro do servidor';
      }
    }
    
    return Promise.reject(error);
  }
);

export { api };
