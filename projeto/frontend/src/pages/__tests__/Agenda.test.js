import React from 'react';
import { render, screen } from '@testing-library/react';
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

import Agenda from '../Agenda';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { retry: false }
  }
});

const renderAgenda = () => {
  return render(
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <Agenda />
        </AuthProvider>
      </QueryClientProvider>
    </BrowserRouter>
  );
};

describe('Agenda Page', () => {
  it('deve renderizar a página de agenda', () => {
    renderAgenda();
    expect(document.body).toBeInTheDocument();
  });
});

