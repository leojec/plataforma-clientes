import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import Dashboard from '../Dashboard';
import { AuthProvider } from '../../contexts/AuthContext';

// Mock do useSidebar
jest.mock('../../hooks/useSidebar', () => ({
  useSidebar: () => ({ sidebarExpanded: true })
}));

// Mock do CanvasJS
global.window.CanvasJS = {
  Chart: jest.fn().mockImplementation(() => ({
    render: jest.fn()
  }))
};

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { retry: false }
  }
});

const renderDashboard = () => {
  return render(
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <Dashboard />
        </AuthProvider>
      </QueryClientProvider>
    </BrowserRouter>
  );
};

describe('Dashboard Page', () => {
  it('deve renderizar a página de dashboard', () => {
    renderDashboard();
    expect(document.body).toBeInTheDocument();
  });
});

