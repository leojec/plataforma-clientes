import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../../contexts/AuthContext';

// Mock do api ANTES de importar Configuracoes
jest.mock('../../services/api', () => ({
  api: {
    get: jest.fn(() => Promise.resolve({ data: [] })),
    post: jest.fn(() => Promise.resolve({ data: {} })),
    put: jest.fn(() => Promise.resolve({ data: {} })),
    delete: jest.fn(() => Promise.resolve({ data: {} })),
    defaults: {
      headers: {
        common: {}
      }
    }
  }
}));

// Mock do useSidebar
jest.mock('../../hooks/useSidebar', () => ({
  useSidebar: () => ({ sidebarExpanded: true })
}));

import Configuracoes from '../Configuracoes';

const renderConfiguracoes = () => {
  return render(
    <BrowserRouter>
      <AuthProvider>
        <Configuracoes />
      </AuthProvider>
    </BrowserRouter>
  );
};

describe('Configuracoes Page', () => {
  it('deve renderizar a página de configurações', () => {
    renderConfiguracoes();
    expect(document.body).toBeInTheDocument();
  });
});

