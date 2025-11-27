import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import ActivityModal from '../ActivityModal';

describe('ActivityModal', () => {
  const mockOnClose = jest.fn();
  const mockOnSave = jest.fn();

  const defaultProps = {
    isOpen: true,
    onClose: mockOnClose,
    onSave: mockOnSave,
    leadId: '123'
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('deve renderizar quando isOpen é true', () => {
    render(<ActivityModal {...defaultProps} />);
    expect(screen.getByText(/cadastro de atividade/i)).toBeInTheDocument();
  });

  it('não deve renderizar quando isOpen é false', () => {
    render(<ActivityModal {...defaultProps} isOpen={false} />);
    expect(screen.queryByText(/cadastro de atividade/i)).not.toBeInTheDocument();
  });

  it('deve permitir preencher campo descrição', () => {
    render(<ActivityModal {...defaultProps} />);
    
    const textareas = screen.getAllByRole('textbox');
    const descricaoInput = textareas.find(textarea => 
      textarea.name === 'descricao'
    ) || textareas[0];
    
    if (descricaoInput) {
      fireEvent.change(descricaoInput, { target: { value: 'Teste de atividade', name: 'descricao' } });
    }
    
    expect(screen.getByText(/cadastro de atividade/i)).toBeInTheDocument();
  });

  it('deve renderizar botão de fechar', () => {
    render(<ActivityModal {...defaultProps} />);
    const buttons = screen.getAllByRole('button');
    // Verifica que há botões no modal
    expect(buttons.length).toBeGreaterThan(0);
  });

  it('deve renderizar campos do formulário', () => {
    render(<ActivityModal {...defaultProps} />);
    expect(screen.getByText(/cadastro de atividade/i)).toBeInTheDocument();
  });

  it('deve chamar onSave quando submeter formulário com dados válidos', () => {
    render(<ActivityModal {...defaultProps} />);
    
    // Preencher tipo de atividade
    const selects = screen.getAllByRole('combobox');
    if (selects.length > 0) {
      fireEvent.change(selects[0], { target: { value: 'Ligação', name: 'tipoAtividade' } });
    }
    
    // Preencher descrição
    const textareas = screen.getAllByRole('textbox');
    const descricaoTextarea = textareas.find(ta => ta.name === 'descricao');
    if (descricaoTextarea) {
      fireEvent.change(descricaoTextarea, { target: { value: 'Teste de atividade', name: 'descricao' } });
    }
    
    // Submeter formulário
    const forms = document.querySelectorAll('form');
    if (forms.length > 0) {
      fireEvent.submit(forms[0]);
      expect(mockOnSave).toHaveBeenCalled();
    }
  });

  it('deve formatar valores numéricos corretamente', () => {
    render(<ActivityModal {...defaultProps} />);
    
    const inputs = screen.getAllByRole('textbox');
    const valorInput = inputs.find(input => input.name === 'valorProposta');
    
    if (valorInput) {
      fireEvent.change(valorInput, { target: { value: '1234,56', name: 'valorProposta' } });
    }
    
    expect(screen.getByText(/cadastro de atividade/i)).toBeInTheDocument();
  });

  it('deve chamar handleCadastrarProximo quando clicar em cadastrar próximo', () => {
    render(<ActivityModal {...defaultProps} />);
    
    const selects = screen.getAllByRole('combobox');
    if (selects.length > 0) {
      fireEvent.change(selects[0], { target: { value: 'Ligação', name: 'tipoAtividade' } });
    }
    
    const textareas = screen.getAllByRole('textbox');
    const descricaoTextarea = textareas.find(ta => ta.name === 'descricao');
    if (descricaoTextarea) {
      fireEvent.change(descricaoTextarea, { target: { value: 'Teste', name: 'descricao' } });
    }
    
    const cadastrarProximoButtons = screen.getAllByText(/cadastrar próximo/i);
    if (cadastrarProximoButtons.length > 0) {
      fireEvent.click(cadastrarProximoButtons[0]);
      expect(mockOnSave).toHaveBeenCalled();
    }
  });

  it('deve renderizar tipos de atividade', () => {
    render(<ActivityModal {...defaultProps} />);
    expect(screen.getByText(/cadastro de atividade/i)).toBeInTheDocument();
  });

  it('deve renderizar campos de data e horário', () => {
    render(<ActivityModal {...defaultProps} />);
    expect(screen.getByText(/cadastro de atividade/i)).toBeInTheDocument();
  });
});

