import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { AuthProvider, useAuth } from '../AuthContext';
import { api } from '../../services/api';


jest.mock('../../services/api', () => ({
  api: {
    post: jest.fn(),
    defaults: {
      headers: {
        common: {}
      }
    }
  }
}));


function TestComponent() {
  const { user, login, logout, loading } = useAuth();

  return (
    <div>
      {loading ? (
        <div>Loading...</div>
      ) : (
        <div>
          <div data-testid="user">{user ? JSON.stringify(user) : 'null'}</div>
          <button onClick={() => login('test@test.com', 'password')}>Login</button>
          <button onClick={logout}>Logout</button>
        </div>
      )}
    </div>
  );
}

describe('AuthContext', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  it('deve renderizar provider sem erros', () => {
    render(
      <AuthProvider>
        <div>Test</div>
      </AuthProvider>
    );

    expect(screen.getByText('Test')).toBeInTheDocument();
  });

  it('deve inicializar sem usuário quando não há token', async () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      expect(screen.getByTestId('user')).toHaveTextContent('null');
    });
  });

  it('deve inicializar com token quando existe no localStorage', async () => {
    localStorage.setItem('token', 'test-token');

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      expect(api.defaults.headers.common['Authorization']).toBe('Bearer test-token');
    });
  });

  it('deve fazer login com sucesso', async () => {
    const mockResponse = {
      data: {
        token: 'new-token',
        usuario: { id: 1, nome: 'Test User', email: 'test@test.com' }
      }
    };

    api.post.mockResolvedValue(mockResponse);

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      const loginButton = screen.getByText('Login');
      loginButton.click();
    });

    await waitFor(() => {
      expect(api.post).toHaveBeenCalledWith('/auth/login', {
        email: 'test@test.com',
        senha: 'password'
      });
      expect(localStorage.getItem('token')).toBe('new-token');
    });
  });

  it('deve fazer logout corretamente', async () => {
    localStorage.setItem('token', 'test-token');

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      const logoutButton = screen.getByText('Logout');
      logoutButton.click();
    });

    await waitFor(() => {
      expect(localStorage.getItem('token')).toBeNull();
      expect(api.defaults.headers.common['Authorization']).toBeUndefined();
    });
  });

  it('deve lançar erro quando useAuth é usado fora do provider', () => {

    const consoleError = jest.spyOn(console, 'error').mockImplementation(() => {});

    expect(() => {
      render(<TestComponent />);
    }).toThrow('useAuth deve ser usado dentro de um AuthProvider');

    consoleError.mockRestore();
  });

  it('deve tratar erro de login com mensagem de string', async () => {
    api.post.mockRejectedValue({
      response: {
        data: 'Erro de autenticação'
      }
    });

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      const loginButton = screen.getByText('Login');
      loginButton.click();
    });

    await waitFor(() => {
      expect(api.post).toHaveBeenCalled();
    });
  });

  it('deve tratar erro de login com objeto de erro', async () => {
    api.post.mockRejectedValue({
      response: {
        data: { message: 'Erro de conexão' }
      }
    });

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      const loginButton = screen.getByText('Login');
      loginButton.click();
    });

    await waitFor(() => {
      expect(api.post).toHaveBeenCalled();
    });
  });

  it('deve tratar erro de login sem response', async () => {
    api.post.mockRejectedValue({
      message: 'Network error'
    });

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    await waitFor(() => {
      const loginButton = screen.getByText('Login');
      loginButton.click();
    });

    await waitFor(() => {
      expect(api.post).toHaveBeenCalled();
    });
  });
});

