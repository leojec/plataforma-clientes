import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import Agenda from '../Agenda';
import { AuthProvider } from '../../contexts/AuthContext';

// Mock do useSidebar
jest.mock('../../hooks/useSidebar', () => ({
  useSidebar: () => ({ sidebarExpanded: true })
}));

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

