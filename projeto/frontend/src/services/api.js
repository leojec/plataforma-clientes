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

// Log da URL base para debug (apenas em desenvolvimento)
if (process.env.NODE_ENV === 'development') {
  console.log('🔗 API Base URL:', finalBaseURL);
}

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
  (response) => {
    // Verificar se a resposta é HTML ao invés de JSON
    const contentType = response.headers['content-type'] || '';
    if (contentType.includes('text/html')) {
      console.error('❌ API retornou HTML ao invés de JSON. URL:', response.config?.url);
      console.error('❌ Resposta HTML:', response.data?.substring(0, 200));
      // Criar um erro customizado
      const error = new Error('API retornou HTML ao invés de JSON. Verifique a URL base e o endpoint.');
      error.response = {
        ...response,
        data: null,
        isHtmlResponse: true
      };
      return Promise.reject(error);
    }
    
    // Verificar se response.data é uma string que parece HTML
    if (typeof response.data === 'string' && response.data.trim().startsWith('<!')) {
      console.error('❌ API retornou HTML ao invés de JSON. URL:', response.config?.url);
      const error = new Error('API retornou HTML ao invés de JSON. Verifique a URL base e o endpoint.');
      error.response = {
        ...response,
        isHtmlResponse: true
      };
      return Promise.reject(error);
    }
    
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    
    // Verificar se o erro é HTML
    if (error.response?.data && typeof error.response.data === 'string' && error.response.data.trim().startsWith('<!')) {
      console.error('❌ Erro: API retornou HTML ao invés de JSON');
      error.message = 'API retornou HTML ao invés de JSON. Verifique a URL base e o endpoint.';
      error.isHtmlResponse = true;
    }
    
    // Garantir que o erro seja uma string ou objeto simples
    if (error.response?.data && !error.isHtmlResponse) {
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
