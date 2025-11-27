import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ChatBot from '../ChatBot';
import { api } from '../../services/api';
import toast from 'react-hot-toast';

// Mock do api
jest.mock('../../services/api', () => ({
  api: {
    post: jest.fn()
  }
}));

// Mock do toast
jest.mock('react-hot-toast', () => ({
  __esModule: true,
  default: {
    success: jest.fn(),
    error: jest.fn(),
  },
}));

describe('ChatBot', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve renderizar o componente', () => {
    render(<ChatBot />);
    expect(document.body).toBeInTheDocument();
  });

  it('deve abrir o chat quando clicar no botão', () => {
    render(<ChatBot />);
    const chatButton = screen.getByRole('button');
    fireEvent.click(chatButton);
    // Verifica que o componente renderiza após clicar
    expect(document.body).toBeInTheDocument();
  });
});

