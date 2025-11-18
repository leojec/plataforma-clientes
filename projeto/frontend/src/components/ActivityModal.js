import React, { useState } from 'react';
import { X, Calendar, Clock } from 'lucide-react';

function ActivityModal({ isOpen, onClose, onSave, leadId }) {
  const [formData, setFormData] = useState({
    tipoAtividade: '',
    descricao: '',
    valorProposta: '',
    metrosQuadrados: '',
    link: '',
    dataAgendamento: '',
    horarioAgendamento: ''
  });

  const tiposAtividade = [
    'Ligação',
    'Contato WhatsApp', 
    'Reunião',
    'Email',
    'Proposta',
    'Fechado'
  ];

  const horarios = [
    '08:00', '08:30', '09:00', '09:30', '10:00', '10:30',
    '11:00', '11:30', '12:00', '12:30', '13:00', '13:30',
    '14:00', '14:30', '15:00', '15:30', '16:00', '16:30',
    '17:00', '17:30', '18:00', '18:30', '19:00', '19:30'
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;
    
    // Para campos numéricos de proposta, aceitar apenas números e ponto decimal
    if ((name === 'valorProposta' || name === 'metrosQuadrados') && value !== '') {
      const numericValue = value.replace(/[^\d.,]/g, '').replace(',', '.');
      setFormData({
        ...formData,
        [name]: numericValue
      });
    } else {
      setFormData({
        ...formData,
        [name]: value
      });
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave({
      ...formData,
      leadId,
      dataCriacao: new Date().toLocaleDateString('pt-BR')
    });
    setFormData({
      tipoAtividade: '',
      descricao: '',
      valorProposta: '',
      metrosQuadrados: '',
      link: '',
      dataAgendamento: '',
      horarioAgendamento: ''
    });
  };

  const handleCadastrarProximo = () => {
    handleSubmit({ preventDefault: () => {} });
    // Não fecha o modal, apenas limpa os campos
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">Cadastro de Atividade</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          {/* Tipo de Atividade */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Tipo de Atividade
            </label>
            <select
              name="tipoAtividade"
              value={formData.tipoAtividade}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="">Selecione o tipo</option>
              {tiposAtividade.map((tipo) => (
                <option key={tipo} value={tipo}>
                  {tipo}
                </option>
              ))}
            </select>
          </div>

          {/* Campos condicionais - Proposta/Fechado ou Descrição */}
          {(formData.tipoAtividade === 'Proposta' || formData.tipoAtividade === 'Fechado') ? (
            <>
              {/* Valor da Proposta */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Valor (R$)
                </label>
                <div className="relative">
                  <span className="absolute left-3 top-2.5 text-gray-500 font-medium">R$</span>
                  <input
                    type="text"
                    name="valorProposta"
                    value={formData.valorProposta}
                    onChange={handleChange}
                    required
                    className="w-full pl-12 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="0,00"
                  />
                </div>
              </div>

              {/* Metros Quadrados */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Área (m²)
                </label>
                <div className="relative">
                  <input
                    type="text"
                    name="metrosQuadrados"
                    value={formData.metrosQuadrados}
                    onChange={handleChange}
                    required
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="0,00"
                  />
                  <span className="absolute right-3 top-2.5 text-gray-500 font-medium">m²</span>
                </div>
              </div>
            </>
          ) : (
            /* Descrição para outros tipos */
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Descrição
              </label>
              <textarea
                name="descricao"
                value={formData.descricao}
                onChange={handleChange}
                required
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Descreva a atividade..."
              />
            </div>
          )}

          {/* Link */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Link
            </label>
            <input
              type="url"
              name="link"
              value={formData.link}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              placeholder="https://exemplo.com"
            />
          </div>

          {/* Data Agendamento */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Data Agendamento
            </label>
            <div className="relative">
              <input
                type="date"
                name="dataAgendamento"
                value={formData.dataAgendamento}
                onChange={handleChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
              <Calendar className="absolute right-3 top-2.5 h-5 w-5 text-gray-400 pointer-events-none" />
            </div>
          </div>

          {/* Horário Agendamento */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Horário Agendamento
            </label>
            <div className="relative">
              <select
                name="horarioAgendamento"
                value={formData.horarioAgendamento}
                onChange={handleChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent appearance-none"
              >
                <option value="">Selecione o horário</option>
                {horarios.map((horario) => (
                  <option key={horario} value={horario}>
                    {horario}
                  </option>
                ))}
              </select>
              <Clock className="absolute right-3 top-2.5 h-5 w-5 text-gray-400 pointer-events-none" />
            </div>
          </div>

          {/* Buttons */}
          <div className="flex space-x-3 pt-4">
            <button
              type="button"
              onClick={handleCadastrarProximo}
              className="flex-1 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
            >
              Cadastrar Próximo
            </button>
            <button
              type="submit"
              className="flex-1 bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md transition-colors"
            >
              Salvar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ActivityModal;
