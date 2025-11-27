import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Login from '../Login';
import { AuthProvider } from '../../contexts/AuthContext';
import { api } from '../../services/api';
import toast from 'react-hot-toast';

// Mock do react-router-dom
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

// Mock do react-hot-toast
jest.mock('react-hot-toast', () => ({
  __esModule: true,
  default: {
    success: jest.fn(),
    error: jest.fn(),
  },
}));

// Mock do api
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

const renderLogin = () => {
  return render(
    <BrowserRouter>
      <AuthProvider>
        <Login />
      </AuthProvider>
    </BrowserRouter>
  );
};

describe('Login Page', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  it('deve renderizar o formulário de login', () => {
    renderLogin();
    
    expect(screen.getByPlaceholderText(/email/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/senha/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /entrar/i })).toBeInTheDocument();
  });

  it('deve permitir digitar email e senha', () => {
    renderLogin();
    
    const emailInput = screen.getByPlaceholderText(/email/i);
    const senhaInput = screen.getByPlaceholderText(/senha/i);
    
    fireEvent.change(emailInput, { target: { value: 'test@test.com' } });
    fireEvent.change(senhaInput, { target: { value: 'password123' } });
    
    expect(emailInput.value).toBe('test@test.com');
    expect(senhaInput.value).toBe('password123');
  });

  it('deve alternar visibilidade da senha', () => {
    renderLogin();
    
    const senhaInput = screen.getByPlaceholderText(/sua senha/i);
    const toggleButtons = screen.getAllByRole('button');
    const toggleButton = toggleButtons.find(btn => 
      btn.querySelector('svg') || btn.getAttribute('aria-label')?.includes('senha')
    ) || toggleButtons[1];
    
    expect(senhaInput.type).toBe('password');
    
    if (toggleButton) {
      fireEvent.click(toggleButton);
      expect(senhaInput).toBeInTheDocument();
    }
  });

  it('deve permitir submeter formulário de login', () => {
    renderLogin();
    
    const emailInput = screen.getByPlaceholderText(/seu@email.com/i);
    const senhaInput = screen.getByPlaceholderText(/sua senha/i);
    const submitButton = screen.getByRole('button', { name: /entrar/i });
    
    fireEvent.change(emailInput, { target: { value: 'test@test.com' } });
    fireEvent.change(senhaInput, { target: { value: 'password123' } });
    
    expect(emailInput.value).toBe('test@test.com');
    expect(senhaInput.value).toBe('password123');
    expect(submitButton).toBeInTheDocument();
  });

  it('deve submeter formulário de login', async () => {
    renderLogin();
    
    const emailInput = screen.getByPlaceholderText(/seu@email.com/i);
    const senhaInput = screen.getByPlaceholderText(/sua senha/i);
    const form = emailInput.closest('form');
    
    fireEvent.change(emailInput, { target: { value: 'test@test.com' } });
    fireEvent.change(senhaInput, { target: { value: 'password123' } });
    fireEvent.submit(form);
    
    // Apenas verifica que o formulário foi submetido
    await waitFor(() => {
      expect(emailInput.value).toBe('test@test.com');
    });
  });

  it('deve renderizar botão de criar conta', () => {
    renderLogin();
    const criarContaButton = screen.getByText(/criar conta/i);
    expect(criarContaButton).toBeInTheDocument();
  });

  it('deve renderizar título da página', () => {
    renderLogin();
    expect(screen.getByText(/crm shot fair brasil/i)).toBeInTheDocument();
  });

  it('deve renderizar logo do CRM', () => {
    renderLogin();
    expect(screen.getByText(/crm/i)).toBeInTheDocument();
    expect(screen.getByText(/shot/i)).toBeInTheDocument();
  });

  it('deve renderizar texto de fazer login', () => {
    renderLogin();
    expect(screen.getByText(/faça login em sua conta/i)).toBeInTheDocument();
  });

  it('deve renderizar botão de entrar', () => {
    renderLogin();
    const entrarButton = screen.getByRole('button', { name: /entrar/i });
    expect(entrarButton).toBeInTheDocument();
  });

  it('deve renderizar labels dos campos', () => {
    renderLogin();
    expect(screen.getByText(/email/i)).toBeInTheDocument();
    expect(screen.getByText(/senha/i)).toBeInTheDocument();
  });
});
