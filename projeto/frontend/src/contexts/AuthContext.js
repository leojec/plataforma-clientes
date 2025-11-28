import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '../services/api';

const AuthContext = createContext();

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  return context;
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      // Aqui você pode fazer uma chamada para validar o token e buscar dados do usuário
      setUser({ token }); // Por enquanto, apenas define o token
    }
    setLoading(false);
  }, []);

  const login = async (email, senha) => {
    try {
      const response = await api.post('/auth/login', { email, senha });
      const { token, usuario } = response.data;
      
      localStorage.setItem('token', token);
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      setUser({ ...usuario, token });
      
      return { success: true };
    } catch (error) {
      let errorMessage = 'Erro ao fazer login';
      
      // Verificar se é erro de HTML ao invés de JSON
      if (error.isHtmlResponse || (error.response?.data && typeof error.response.data === 'string' && error.response.data.trim().startsWith('<!'))) {
        errorMessage = 'API retornou HTML ao invés de JSON. Verifique a URL base e o endpoint.';
        console.error('❌ Erro: API retornou HTML. URL:', error.config?.url);
        console.error('❌ Base URL:', api.defaults.baseURL);
      } else if (error.response?.data) {
        const errorData = error.response.data;
        if (typeof errorData === 'string') {
          errorMessage = errorData;
        } else if (errorData && typeof errorData === 'object') {
          errorMessage = errorData.message || errorData.error || 'Erro de conexão com o servidor';
        } else {
          errorMessage = 'Erro de conexão com o servidor';
        }
      } else if (error.message && typeof error.message === 'string') {
        errorMessage = error.message;
      } else if (error.code === 'ECONNABORTED') {
        errorMessage = 'Timeout ao conectar com o servidor. Verifique sua conexão.';
      } else if (!error.response) {
        errorMessage = 'Não foi possível conectar ao servidor. Verifique a URL da API.';
      }
      
      console.error('❌ Erro no login:', {
        message: error.message,
        response: error.response?.data,
        status: error.response?.status,
        url: error.config?.url,
        baseURL: api.defaults.baseURL
      });
      
      return { 
        success: false, 
        message: errorMessage
      };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    delete api.defaults.headers.common['Authorization'];
    setUser(null);
  };

  const value = {
    user,
    login,
    logout,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}
