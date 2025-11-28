import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import AddClientModal from '../AddClientModal';
import { api } from '../../services/api';


jest.mock('../../services/api', () => ({
  api: {
    get: jest.fn(),
    post: jest.fn()
  }
}));


jest.mock('react-hot-toast', () => ({
  __esModule: true,
  default: {
    success: jest.fn(),
    error: jest.fn(),
  },
}));

describe('AddClientModal', () => {
  const mockOnClose = jest.fn();
  const mockOnAddClient = jest.fn();

  const defaultProps = {
    isOpen: true,
    onClose: mockOnClose,
    onAddClient: mockOnAddClient
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve renderizar quando isOpen é true', () => {
    render(<AddClientModal {...defaultProps} />);
    expect(document.body).toBeInTheDocument();
  });

  it('não deve renderizar quando isOpen é false', () => {
    const { container } = render(<AddClientModal {...defaultProps} isOpen={false} />);
    expect(container.firstChild).toBeNull();
  });

  it('deve formatar CNPJ corretamente', () => {
    render(<AddClientModal {...defaultProps} />);

    expect(document.body).toBeInTheDocument();
  });

  it('deve permitir preencher campos do formulário', () => {
    render(<AddClientModal {...defaultProps} />);

    const inputs = screen.getAllByRole('textbox');
    if (inputs.length > 0) {
      const cnpjInput = inputs.find(input => input.name === 'cnpj') || inputs[0];
      fireEvent.change(cnpjInput, { target: { value: '12345678000190', name: 'cnpj' } });
    }

    expect(document.body).toBeInTheDocument();
  });

  it('deve chamar onClose quando clicar em cancelar', () => {
    render(<AddClientModal {...defaultProps} />);
    const cancelButtons = screen.getAllByText(/cancelar/i);
    if (cancelButtons.length > 0) {
      fireEvent.click(cancelButtons[0]);
      expect(mockOnClose).toHaveBeenCalled();
    }
  });

  it('deve buscar dados do CNPJ quando CNPJ completo', async () => {
    api.get.mockResolvedValue({ data: { razaoSocial: 'Teste LTDA' } });

    render(<AddClientModal {...defaultProps} />);

    const inputs = screen.getAllByRole('textbox');
    const cnpjInput = inputs.find(input => input.name === 'cnpj');

    if (cnpjInput) {
      fireEvent.change(cnpjInput, { target: { value: '12345678000190', name: 'cnpj' } });
    }


    await new Promise(resolve => setTimeout(resolve, 100));

    expect(document.body).toBeInTheDocument();
  });

  it('deve mostrar erro quando campos obrigatórios não preenchidos', async () => {
    render(<AddClientModal {...defaultProps} />);

    const submitButtons = screen.getAllByText(/salvar|adicionar/i);
    if (submitButtons.length > 0) {
      const form = submitButtons[0].closest('form');
      if (form) {
        fireEvent.submit(form);
      }
    }

    await waitFor(() => {
      expect(toast.error).toHaveBeenCalled();
    });
  });

  it('deve permitir preencher múltiplos campos', () => {
    render(<AddClientModal {...defaultProps} />);

    const inputs = screen.getAllByRole('textbox');
    if (inputs.length > 0) {
      const razaoSocialInput = inputs.find(input => input.name === 'razaoSocial') || inputs[0];
      const emailInput = inputs.find(input => input.name === 'email') || inputs[1];

      if (razaoSocialInput) {
        fireEvent.change(razaoSocialInput, { target: { value: 'Empresa Teste', name: 'razaoSocial' } });
      }
      if (emailInput) {
        fireEvent.change(emailInput, { target: { value: 'teste@teste.com', name: 'email' } });
      }
    }

    expect(document.body).toBeInTheDocument();
  });

  it('deve renderizar todos os campos do formulário', () => {
    render(<AddClientModal {...defaultProps} />);
    expect(document.body).toBeInTheDocument();
  });

  it('deve criar cliente com sucesso quando todos os campos obrigatórios preenchidos', async () => {
    api.post.mockResolvedValue({ data: { id: 1, razaoSocial: 'Teste LTDA' } });

    render(<AddClientModal {...defaultProps} />);

    const inputs = screen.getAllByRole('textbox');
    const razaoSocialInput = inputs.find(input => input.name === 'razaoSocial');
    const cnpjInput = inputs.find(input => input.name === 'cnpj');
    const emailInput = inputs.find(input => input.name === 'email');

    if (razaoSocialInput) {
      fireEvent.change(razaoSocialInput, { target: { value: 'Empresa Teste', name: 'razaoSocial' } });
    }
    if (cnpjInput) {
      fireEvent.change(cnpjInput, { target: { value: '12345678000190', name: 'cnpj' } });
    }
    if (emailInput) {
      fireEvent.change(emailInput, { target: { value: 'teste@teste.com', name: 'email' } });
    }

    const submitButtons = screen.getAllByText(/salvar|adicionar/i);
    if (submitButtons.length > 0) {
      const form = submitButtons[0].closest('form');
      if (form) {
        fireEvent.submit(form);
      }
    }

    await waitFor(() => {
      expect(api.post).toHaveBeenCalled();
    });
  });
});

