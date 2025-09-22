import React, { useState } from 'react';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { 
  Calendar, 
  Clock, 
  User, 
  Phone, 
  Mail, 
  Video,
  ChevronLeft,
  ChevronRight,
  Filter,
  CheckCircle,
  Circle
} from 'lucide-react';

function Agenda() {
  const [selectedDate, setSelectedDate] = useState(new Date());

  // Buscar atividades da agenda
  const { data: agendaData, isLoading, refetch, error } = useQuery(
    ['agenda', selectedDate],
    async () => {
      try {
        const response = await api.get(`/agenda/atividades?data=${selectedDate.toISOString()}&modo=dia`);
        return response.data;
      } catch (err) {
        console.error('Erro ao buscar atividades:', err);
        throw new Error('Falha ao carregar atividades');
      }
    },
    {
      refetchInterval: 30000,
      retry: false,
      onError: (error) => {
        console.error('Erro ao buscar atividades da agenda:', error);
      }
    }
  );

  // Usar dados da API ou dados fictícios como fallback
  const atividades = Array.isArray(agendaData?.atividades) ? agendaData.atividades : [];
  
  // Debug para identificar problemas
  if (agendaData && !Array.isArray(agendaData.atividades)) {
    console.error('Dados inválidos recebidos da API:', agendaData);
  }

  const formatDate = (date) => {
    try {
      if (!date || !(date instanceof Date)) {
        return 'Data inválida';
      }
      return date.toLocaleDateString('pt-BR', { 
        weekday: 'long', 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
      });
    } catch (error) {
      console.error('Erro ao formatar data:', error);
      return 'Data inválida';
    }
  };

  const getTipoIcon = (tipo) => {
    switch (tipo) {
      case 'Ligação':
        return <Phone className="w-4 h-4" />;
      case 'Email':
        return <Mail className="w-4 h-4" />;
      case 'Reunião':
        return <Video className="w-4 h-4" />;
      case 'Contato WhatsApp':
        return <Phone className="w-4 h-4" />;
      default:
        return <Calendar className="w-4 h-4" />;
    }
  };

  const getTipoColor = (tipo) => {
    switch (tipo) {
      case 'Ligação':
        return 'bg-blue-100 text-blue-800';
      case 'Email':
        return 'bg-green-100 text-green-800';
      case 'Reunião':
        return 'bg-purple-100 text-purple-800';
      case 'Contato WhatsApp':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const changeDate = (days) => {
    const newDate = new Date(selectedDate);
    newDate.setDate(selectedDate.getDate() + days);
    setSelectedDate(newDate);
  };

  const marcarComoConcluida = async (atividadeId) => {
    try {
      // Fazer requisição para marcar como concluída no backend
      await api.put(`/agenda/atividades/${atividadeId}/concluir`);
      
      // Refetch dos dados para atualizar a interface
      refetch();
      
      console.log('Atividade marcada como concluída:', atividadeId);
    } catch (error) {
      console.error('Erro ao marcar atividade como concluída:', error);
      alert('Erro ao marcar atividade como concluída');
    }
  };

  if (isLoading) {
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (error) {
    console.error('Erro completo:', error);
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="text-red-500 text-6xl mb-4">⚠️</div>
          <p className="text-red-600 mb-2">Erro ao carregar agenda</p>
          <p className="text-gray-500 text-sm">
            Erro de conexão ou servidor
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-gray-50">
      {/* Header */}
      <div className="bg-blue-100 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <h1 className="text-xl font-semibold text-gray-800">Agenda</h1>
          <span className="text-sm text-gray-600">
            {formatDate(selectedDate)}
          </span>
        </div>

        <div className="flex items-center justify-center">
          <button className="p-2 bg-blue-500 hover:bg-blue-600 rounded-lg transition-colors">
            <Calendar className="w-5 h-5 text-white" />
          </button>
        </div>

      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto p-6">
        {/* Date Navigation */}
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center space-x-4">
            <button
              onClick={() => changeDate(-1)}
              className="p-2 hover:bg-gray-200 rounded-lg transition-colors"
            >
              <ChevronLeft className="w-5 h-5 text-gray-600" />
            </button>
            <h2 className="text-lg font-medium text-gray-900">
              {selectedDate ? selectedDate.toLocaleDateString('pt-BR') : 'Data inválida'}
            </h2>
            <button
              onClick={() => changeDate(1)}
              className="p-2 hover:bg-gray-200 rounded-lg transition-colors"
            >
              <ChevronRight className="w-5 h-5 text-gray-600" />
            </button>
          </div>

          <div className="flex items-center space-x-2">
            <button className="flex items-center space-x-2 px-4 py-2 border border-gray-300 text-gray-700 text-sm rounded-lg hover:bg-gray-50 transition-colors">
              <Filter className="w-4 h-4" />
              <span>Filtros</span>
            </button>
          </div>
        </div>

        {/* Timeline de Atividades */}
        <div className="bg-white rounded-lg shadow-sm border">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-medium text-gray-900">
              Atividades do Dia
            </h3>
          </div>

          <div className="p-6">
            {atividades.length === 0 ? (
              <div className="text-center py-12">
                <Calendar className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                <p className="text-gray-500">Nenhuma atividade agendada para esta data</p>
              </div>
            ) : (
              <div className="space-y-4">
                {atividades.map((atividade) => (
                  <div
                    key={atividade.id}
                    className={`flex items-start space-x-4 p-4 rounded-lg border-l-4 ${
                      atividade.status === 'concluida' 
                        ? 'border-green-500 bg-green-50' 
                        : 'border-blue-500 bg-blue-50'
                    } hover:shadow-md transition-shadow cursor-pointer`}
                  >
                    {/* Horário */}
                    <div className="flex flex-col items-center min-w-0">
                      <div className="flex items-center space-x-1 text-sm font-medium text-gray-900">
                        <Clock className="w-4 h-4" />
                        <span>{String(atividade.horario || '')}</span>
                      </div>
                    </div>

                    {/* Conteúdo */}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center space-x-2 mb-2">
                        <span className={`inline-flex items-center space-x-1 px-2 py-1 rounded-full text-xs font-medium ${getTipoColor(atividade.tipo)}`}>
                          {getTipoIcon(atividade.tipo)}
                          <span>{String(atividade.tipo || '')}</span>
                        </span>
                        <span className="text-sm text-gray-500">•</span>
                        <div className="flex items-center space-x-1 text-sm text-gray-600">
                          <User className="w-3 h-3" />
                          <span>{String(atividade.leadNome || '')}</span>
                        </div>
                      </div>
                      
                      <h4 className="text-sm font-medium text-gray-900 mb-1">
                        {String(atividade.titulo || '')}
                      </h4>
                      
                      <p className="text-sm text-gray-600">
                        {String(atividade.descricao || '')}
                      </p>
                    </div>

                    {/* Status e Ações */}
                    <div className="flex items-center space-x-2">
                      {atividade.status === 'concluida' ? (
                        <div className="flex items-center space-x-2">
                          <CheckCircle className="w-5 h-5 text-green-600" />
                          <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800">
                            Concluída
                          </span>
                        </div>
                      ) : (
                        <div className="flex items-center space-x-2">
                          <button
                            onClick={() => marcarComoConcluida(atividade.id)}
                            className="p-1 hover:bg-green-100 rounded-full transition-colors"
                            title="Marcar como concluída"
                          >
                            <Circle className="w-5 h-5 text-gray-400 hover:text-green-600" />
                          </button>
                          <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                            Agendada
                          </span>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Resumo do Dia */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">
          <div className="bg-white rounded-lg p-4 shadow-sm border">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-blue-100 rounded-lg">
                <Calendar className="w-5 h-5 text-blue-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Total de Atividades</p>
                <p className="text-xl font-bold text-gray-900">{Number(agendaData?.total || atividades.length || 0)}</p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg p-4 shadow-sm border">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-green-100 rounded-lg">
                <Clock className="w-5 h-5 text-green-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Concluídas</p>
                <p className="text-xl font-bold text-gray-900">
                  {Number(agendaData?.concluidas || atividades.filter(a => a?.status === 'concluida').length || 0)}
                </p>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg p-4 shadow-sm border">
            <div className="flex items-center space-x-3">
              <div className="p-2 bg-yellow-100 rounded-lg">
                <User className="w-5 h-5 text-yellow-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Pendentes</p>
                <p className="text-xl font-bold text-gray-900">
                  {Number(agendaData?.pendentes || atividades.filter(a => a?.status === 'agendada').length || 0)}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Agenda;
