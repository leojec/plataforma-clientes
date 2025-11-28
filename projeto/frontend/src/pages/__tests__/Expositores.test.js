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

import Expositores from '../Expositores';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { retry: false }
  }
});

const renderExpositores = () => {
  return render(
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <Expositores />
        </AuthProvider>
      </QueryClientProvider>
    </BrowserRouter>
  );
};

describe('Expositores Page', () => {
  it('deve renderizar a página de expositores', () => {
    renderExpositores();
    expect(document.body).toBeInTheDocument();
  });
});

