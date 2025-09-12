import React, { useState } from 'react';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { Plus, Search, TrendingUp, DollarSign, Calendar } from 'lucide-react';

function Oportunidades() {
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');

  const { data: oportunidades, isLoading } = useQuery(
    ['oportunidades', searchTerm, statusFilter],
    async () => {
      const response = await api.get('/oportunidades', {
        params: { 
          titulo: searchTerm,
          status: statusFilter || undefined
        }
      });
      return response.data.content || response.data;
    }
  );

  const getStatusColor = (status) => {
    switch (status) {
      case 'PROSPECCAO':
        return 'bg-blue-100 text-blue-800';
      case 'QUALIFICACAO':
        return 'bg-yellow-100 text-yellow-800';
      case 'PROPOSTA':
        return 'bg-orange-100 text-orange-800';
      case 'NEGOCIACAO':
        return 'bg-purple-100 text-purple-800';
      case 'FECHADA_GANHA':
        return 'bg-green-100 text-green-800';
      case 'FECHADA_PERDIDA':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value || 0);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Oportunidades</h1>
          <p className="text-gray-600">Gerencie seu pipeline de vendas</p>
        </div>
        <button className="btn-primary flex items-center space-x-2">
          <Plus className="h-5 w-5" />
          <span>Nova Oportunidade</span>
        </button>
      </div>

      {/* Filtros */}
      <div className="card p-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Pesquisar oportunidades..."
              className="input-field pl-10"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          
          <select
            className="input-field"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="">Todos os status</option>
            <option value="PROSPECCAO">Prospecção</option>
            <option value="QUALIFICACAO">Qualificação</option>
            <option value="PROPOSTA">Proposta</option>
            <option value="NEGOCIACAO">Negociação</option>
            <option value="FECHADA_GANHA">Fechada - Ganha</option>
            <option value="FECHADA_PERDIDA">Fechada - Perdida</option>
          </select>
        </div>
      </div>

      {/* Tabela de Oportunidades */}
      <div className="card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="table-header">Título</th>
                <th className="table-header">Expositor</th>
                <th className="table-header">Status</th>
                <th className="table-header">Valor</th>
                <th className="table-header">Probabilidade</th>
                <th className="table-header">Data Prevista</th>
                <th className="table-header">Vendedor</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {oportunidades?.map((oportunidade) => (
                <tr key={oportunidade.id} className="hover:bg-gray-50">
                  <td className="table-cell">
                    <div className="font-medium text-gray-900">
                      {oportunidade.titulo}
                    </div>
                    {oportunidade.descricao && (
                      <div className="text-sm text-gray-500 truncate max-w-xs">
                        {oportunidade.descricao}
                      </div>
                    )}
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {oportunidade.expositor?.razaoSocial || '-'}
                  </td>
                  <td className="table-cell">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(oportunidade.status)}`}>
                      {oportunidade.status}
                    </span>
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {formatCurrency(oportunidade.valorEstimado)}
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {oportunidade.probabilidadeFechamento}%
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {oportunidade.dataPrevistaFechamento ? 
                      new Date(oportunidade.dataPrevistaFechamento).toLocaleDateString('pt-BR') : 
                      '-'
                    }
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {oportunidade.vendedor?.nome || '-'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {oportunidades?.length === 0 && (
        <div className="text-center py-12">
          <TrendingUp className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">Nenhuma oportunidade</h3>
          <p className="mt-1 text-sm text-gray-500">
            Comece criando uma nova oportunidade.
          </p>
        </div>
      )}
    </div>
  );
}

export default Oportunidades;
