import axios from 'axios';


const normalizeURL = (url) => {
  if (!url) return 'http://localhost:8080/api';

  return url.replace(/[\u200B-\u200D\uFEFF\u200C\u200D]/g, '').trim().replace(/\/+$/, '');
};


const isProduction = window.location.protocol === 'https:';


const backendURL = process.env.REACT_APP_BACKEND_URL;
const apiURL = process.env.REACT_APP_API_URL;

let baseURL;

if (apiURL) {
  baseURL = apiURL;
} else if (isProduction && backendURL) {

  if (backendURL.startsWith('http://') && backendURL.includes('elasticbeanstalk.com')) {
    baseURL = backendURL.replace('http://', 'https://');
    console.warn('⚠️ Convertendo backend URL de HTTP para HTTPS para evitar Mixed Content');
  } else {
    baseURL = backendURL;
  }
} else if (backendURL) {

  baseURL = backendURL;
} else {

  baseURL = 'http://localhost:8080/api';
}


const normalizedURL = normalizeURL(baseURL);
const finalBaseURL = normalizedURL.endsWith('/api') ? normalizedURL : `${normalizedURL}/api`;

const api = axios.create({
  baseURL: finalBaseURL,
  timeout: 10000,
});

console.log('🔗 API Base URL:', finalBaseURL);
console.log('🔗 Ambiente:', isProduction ? 'Produção (HTTPS)' : 'Desenvolvimento');
console.log('🔗 REACT_APP_BACKEND_URL:', process.env.REACT_APP_BACKEND_URL || 'não definido');
console.log('🔗 REACT_APP_API_URL:', process.env.REACT_APP_API_URL || 'não definido');


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

api.interceptors.response.use(
  (response) => {

    const contentType = response.headers['content-type'] || '';
    if (contentType.includes('text/html')) {
      console.error('❌ API retornou HTML ao invés de JSON. URL:', response.config?.url);
      console.error('❌ Resposta HTML:', response.data?.substring(0, 200));
      console.error('💡 Dica: Configure REACT_APP_BACKEND_URL com a URL direta do backend (Elastic Beanstalk)');

      const error = new Error('API retornou HTML ao invés de JSON. Verifique a URL base e o endpoint. Se estiver usando CloudFront, configure REACT_APP_BACKEND_URL com a URL direta do backend.');
      error.response = {
        ...response,
        data: null,
        isHtmlResponse: true
      };
      return Promise.reject(error);
    }

    if (typeof response.data === 'string' && response.data.trim().startsWith('<!')) {
      console.error('❌ API retornou HTML ao invés de JSON. URL:', response.config?.url);
      console.error('💡 Dica: Configure REACT_APP_BACKEND_URL com a URL direta do backend (Elastic Beanstalk)');
      const error = new Error('API retornou HTML ao invés de JSON. Verifique a URL base e o endpoint. Se estiver usando CloudFront, configure REACT_APP_BACKEND_URL com a URL direta do backend.');
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

    if (error.response?.data && typeof error.response.data === 'string' && error.response.data.trim().startsWith('<!')) {
      console.error('❌ Erro: API retornou HTML ao invés de JSON');
      error.message = 'API retornou HTML ao invés de JSON. Verifique a URL base e o endpoint.';
      error.isHtmlResponse = true;
    }

    if (error.response?.data && !error.isHtmlResponse) {
      const errorData = error.response.data;
      if (typeof errorData === 'object' && errorData !== null) {

        error.message = errorData.message || errorData.error || 'Erro do servidor';
      }
    }

    return Promise.reject(error);
  }
);

export { api };
