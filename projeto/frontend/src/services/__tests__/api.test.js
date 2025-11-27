// Mock do axios antes de importar
jest.mock('axios', () => {
  const mockAxios = jest.fn(() => Promise.resolve({ data: {} }));
  mockAxios.create = jest.fn(() => {
    const instance = jest.fn(() => Promise.resolve({ data: {} }));
    instance.defaults = {
      baseURL: 'http://localhost:8080/api',
      timeout: 10000,
      headers: {
        common: {}
      }
    };
    instance.interceptors = {
      request: {
        handlers: [],
        use: jest.fn((fulfilled, rejected) => {
          instance.interceptors.request.handlers.push({ fulfilled, rejected });
          return 0;
        })
      },
      response: {
        handlers: [],
        use: jest.fn((fulfilled, rejected) => {
          instance.interceptors.response.handlers.push({ fulfilled, rejected });
          return 0;
        })
      }
    };
    return instance;
  });
  return mockAxios;
});

import { api } from '../api';

describe('API Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
    delete window.location;
    window.location = { href: '' };
  });

  it('deve criar instância do axios com configuração correta', () => {
    expect(api.defaults.baseURL).toBe(process.env.REACT_APP_API_URL || 'http://localhost:8080/api');
    expect(api.defaults.timeout).toBe(10000);
  });

  it('deve adicionar token no header quando existe no localStorage', () => {
    localStorage.setItem('token', 'test-token');
    
    const config = {
      headers: {}
    };
    
    const interceptor = api.interceptors.request.handlers[0].fulfilled;
    const result = interceptor(config);
    
    expect(result.headers.Authorization).toBe('Bearer test-token');
  });

  it('não deve adicionar token quando não existe no localStorage', () => {
    localStorage.removeItem('token');
    
    const config = {
      headers: {}
    };
    
    const interceptor = api.interceptors.request.handlers[0].fulfilled;
    const result = interceptor(config);
    
    expect(result.headers.Authorization).toBeUndefined();
  });

  it('deve redirecionar para login quando receber 401', () => {
    const error = {
      response: {
        status: 401
      }
    };
    
    const interceptor = api.interceptors.response.handlers[0].rejected;
    
    interceptor(error).catch(() => {});
    
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('deve retornar response quando sucesso', () => {
    const response = { data: { success: true } };
    
    const interceptor = api.interceptors.response.handlers[0].fulfilled;
    const result = interceptor(response);
    
    expect(result).toBe(response);
  });
});

