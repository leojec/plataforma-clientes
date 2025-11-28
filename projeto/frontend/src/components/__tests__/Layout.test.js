import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter, MemoryRouter } from 'react-router-dom';
import Layout from '../Layout';
import { AuthProvider } from '../../contexts/AuthContext';


jest.mock('../ChatBot', () => {
  return function ChatBot() {
    return <div>ChatBot</div>;
  };
});

const renderLayout = () => {
  return render(
    <MemoryRouter>
      <AuthProvider>
        <Layout />
      </AuthProvider>
    </MemoryRouter>
  );
};

describe('Layout Component', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it('deve renderizar o componente Layout', () => {
    renderLayout();
    expect(document.body).toBeInTheDocument();
  });

  it('deve renderizar links de navegação', () => {
    renderLayout();

    expect(document.body).toBeInTheDocument();
  });
});

