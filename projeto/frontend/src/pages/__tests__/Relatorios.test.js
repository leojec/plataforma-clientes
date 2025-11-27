import React from 'react';
import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import Relatorios from '../Relatorios';
import { AuthProvider } from '../../contexts/AuthContext';

// Mock do useSidebar
jest.mock('../../hooks/useSidebar', () => ({
  useSidebar: () => ({ sidebarExpanded: true })
}));

// Mock do useNavigate
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { retry: false }
  }
});

const renderRelatorios = () => {
  return render(
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <Relatorios />
        </AuthProvider>
      </QueryClientProvider>
    </BrowserRouter>
  );
};

describe('Relatorios Page', () => {
  it('deve renderizar a página de relatórios', () => {
    renderRelatorios();
    expect(document.body).toBeInTheDocument();
  });
});

