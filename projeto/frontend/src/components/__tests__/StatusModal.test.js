import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import StatusModal from '../StatusModal';
import toast from 'react-hot-toast';


jest.mock('react-hot-toast', () => ({
  __esModule: true,
  default: {
    success: jest.fn(),
    error: jest.fn(),
  },
}));

describe('StatusModal', () => {
  const mockOnClose = jest.fn();
  const mockOnSave = jest.fn();

  const defaultProps = {
    isOpen: true,
    onClose: mockOnClose,
    onSave: mockOnSave,
    leadId: '123',
    currentStatus: 'Lead'
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve renderizar quando isOpen é true', () => {
    render(<StatusModal {...defaultProps} />);
    expect(screen.getByText(/alterar status/i)).toBeInTheDocument();
  });

  it('não deve renderizar quando isOpen é false', () => {
    render(<StatusModal {...defaultProps} isOpen={false} />);
    expect(screen.queryByText(/alterar status/i)).not.toBeInTheDocument();
  });

  it('deve chamar onClose quando clicar em cancelar', () => {
    render(<StatusModal {...defaultProps} />);
    const cancelButton = screen.getByText(/cancelar/i);
    fireEvent.click(cancelButton);
    expect(mockOnClose).toHaveBeenCalled();
  });

  it('deve mostrar erro quando tentar salvar sem selecionar status', async () => {
    render(<StatusModal {...defaultProps} />);
    const saveButtons = screen.getAllByText(/alterar/i);
    const submitButton = saveButtons.find(btn => btn.type === 'submit') || saveButtons[0];
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(toast.error).toHaveBeenCalledWith('Por favor, selecione um status.');
    });
  });

  it('deve permitir selecionar um status', () => {
    render(<StatusModal {...defaultProps} />);
    const statusSelect = screen.getByLabelText(/selecione um status/i);
    expect(statusSelect).toBeInTheDocument();
    fireEvent.change(statusSelect, { target: { value: 'Em Andamento' } });
    expect(statusSelect.value).toBe('Em Andamento');
  });

  it('deve chamar onSave quando submeter com status selecionado', () => {
    render(<StatusModal {...defaultProps} />);
    const statusSelect = screen.getByLabelText(/selecione um status/i);
    fireEvent.change(statusSelect, { target: { value: 'Em Andamento' } });

    const submitButtons = screen.getAllByText(/alterar/i);
    const submitButton = submitButtons.find(btn => btn.type === 'submit') || submitButtons[submitButtons.length - 1];
    const form = submitButton.closest('form');
    fireEvent.submit(form);

    expect(mockOnSave).toHaveBeenCalledWith(
      expect.objectContaining({
        leadId: '123',
        newStatus: 'Em Andamento',
        previousStatus: 'Lead'
      })
    );
    expect(mockOnClose).toHaveBeenCalled();
  });

  it('deve resetar status quando modal abre', () => {
    const { rerender } = render(<StatusModal {...defaultProps} isOpen={false} />);
    rerender(<StatusModal {...defaultProps} isOpen={true} />);

    const statusSelect = screen.getByLabelText(/selecione um status/i);
    expect(statusSelect.value).toBe('');
  });

  it('deve fechar modal ao clicar no X', () => {
    render(<StatusModal {...defaultProps} />);
    const closeButtons = screen.getAllByRole('button');
    const xButton = closeButtons.find(btn => btn.querySelector('svg'));

    if (xButton) {
      fireEvent.click(xButton);
      expect(mockOnClose).toHaveBeenCalled();
    }
  });
});

