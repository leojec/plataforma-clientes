import React from 'react';
import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { AuthProvider } from '../../contexts/AuthContext';


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


jest.mock('../../hooks/useSidebar', () => ({
  useSidebar: () => ({ sidebarExpanded: true })
}));


jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

import Relatorios from '../Relatorios';

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

