import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Configuracoes from '../Configuracoes';
import { AuthProvider } from '../../contexts/AuthContext';

// Mock do useSidebar
jest.mock('../../hooks/useSidebar', () => ({
  useSidebar: () => ({ sidebarExpanded: true })
}));

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

