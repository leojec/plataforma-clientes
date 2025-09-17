import React, { useState } from 'react';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { 
  Calendar, 
  Download, 
  TrendingUp, 
  Users, 
  DollarSign,
  UserPlus,
  CheckSquare,
  UserMinus,
  Hand,
  CheckCircle
} from 'lucide-react';

function Relatorios() {
  const [selectedPeriod, setSelectedPeriod] = useState('Mês');

  // Buscar dados do dashboard (mesmos dados do Dashboard)
  const { data: dashboardStats, isLoading } = useQuery(
    'dashboardStats',
    () => api.get('/dashboard/stats').then(res => res.data),
    {
      refetchInterval: 30000,
      retry: 3
    }
  );

  // Buscar dados do gráfico de atividades
  const { data: graficoData } = useQuery(
    'dashboardGrafico',
    () => api.get('/dashboard/atividades-grafico').then(res => res.data),
    {
      refetchInterval: 60000,
    }
  );

  // Dados simulados para relatórios baseados nos dados reais
  const relatoriosData = {
    oportunidadesPorStatus: [
      { name: 'Lead', value: 8, color: '#3B82F6' },
      { name: 'Em Andamento', value: 5, color: '#F59E0B' },
      { name: 'Em Negociação', value: 7, color: '#8B5CF6' },
      { name: 'Stand Fechado', value: 12, color: '#10B981' }
    ],
    vendasPorMes: [
      { mes: 'Jan', vendas: 12000 },
      { mes: 'Fev', vendas: 19000 },
      { mes: 'Mar', vendas: 15000 },
      { mes: 'Abr', vendas: 25000 },
      { mes: 'Mai', vendas: 22000 },
      { mes: 'Jun', vendas: 30000 }
    ],
    performanceVendedores: [
      { vendedor: 'João Silva', vendas: 45000, oportunidades: 15, ticketMedio: 3000, conversao: 75 },
      { vendedor: 'Maria Santos', vendas: 38000, oportunidades: 12, ticketMedio: 3166, conversao: 71 },
      { vendedor: 'Administrador', vendas: 32000, oportunidades: 10, ticketMedio: 3200, conversao: 68 }
    ]
  };

  // Dados dos cards de resumo (usando os mesmos dados do Dashboard)
  const summaryCards = [
    {
      id: 1,
      title: 'NOVOS CLIENTES',
      value: dashboardStats?.novosClientes || 0,
      icon: UserPlus,
      color: 'bg-blue-500',
      iconBg: 'bg-blue-100'
    },
    {
      id: 2,
      title: 'QTD.ATIVIDADES',
      value: dashboardStats?.qtdAtividades || 0,
      icon: CheckSquare,
      color: 'bg-blue-700',
      iconBg: 'bg-blue-100'
    },
    {
      id: 3,
      title: 'QTD.PERDA',
      value: dashboardStats?.qtdPerdas || 0,
      icon: UserMinus,
      color: 'bg-red-500',
      iconBg: 'bg-red-100'
    },
    {
      id: 4,
      title: 'VALOR PROPOSTAS EM ABERTO',
      value: `R$ ${(dashboardStats?.valorPropostasAbertas || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`,
      icon: Hand,
      color: 'bg-green-500',
      iconBg: 'bg-green-100'
    },
    {
      id: 5,
      title: 'QTD.GANHO',
      value: dashboardStats?.qtdGanhos || 0,
      icon: CheckCircle,
      color: 'bg-green-500',
      iconBg: 'bg-green-100'
    },
    {
      id: 6,
      title: 'VALOR GANHO',
      value: `R$ ${(dashboardStats?.valorGanho || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`,
      icon: DollarSign,
      color: 'bg-green-700',
      iconBg: 'bg-green-100'
    }
  ];

  if (isLoading) {
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-gray-50">
      {/* Header */}
      <div className="bg-blue-100 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <h1 className="text-xl font-semibold text-gray-800">Relatórios</h1>
        </div>

        <div className="flex items-center justify-center">
          <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
            <Calendar className="w-5 h-5 text-gray-600" />
          </button>
        </div>

        {/* Period Selector */}
        <div className="flex bg-white rounded-lg p-1">
          {['Dia', 'Semana', 'Mês'].map((period) => (
            <button
              key={period}
              onClick={() => setSelectedPeriod(period)}
              className={`px-3 py-1 text-sm rounded-md transition-colors ${
                selectedPeriod === period
                  ? 'bg-blue-500 text-white'
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              {period}
            </button>
          ))}
        </div>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto p-6">
        {/* Cards de Resumo */}
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4 mb-8">
          {summaryCards.map((card) => {
            const IconComponent = card.icon;
            return (
              <div key={card.id} className="bg-white rounded-lg p-4 shadow-sm border">
                <div className="flex items-center justify-between mb-2">
                  <div className={`p-2 rounded-lg ${card.iconBg}`}>
                    <IconComponent className={`w-5 h-5 ${card.color.replace('bg-', 'text-')}`} />
                  </div>
                </div>
                <div className="space-y-1">
                  <p className="text-xs font-medium text-gray-600 uppercase tracking-wide">
                    {card.title}
                  </p>
                  <p className="text-xl font-bold text-gray-900">
                    {card.value}
                  </p>
                </div>
              </div>
            );
          })}
        </div>

        {/* Gráficos */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Gráfico de Oportunidades por Status */}
          <div className="bg-white rounded-lg p-6 shadow-sm border">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Oportunidades por Status</h3>
            <div className="space-y-4">
              {relatoriosData.oportunidadesPorStatus.map((item, index) => (
                <div key={index} className="flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    <div 
                      className="w-4 h-4 rounded-full"
                      style={{ backgroundColor: item.color }}
                    ></div>
                    <span className="text-sm text-gray-600">{item.name}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <div className="w-24 bg-gray-200 rounded-full h-2">
                      <div 
                        className="h-2 rounded-full"
                        style={{ 
                          backgroundColor: item.color,
                          width: `${(item.value / 32) * 100}%`
                        }}
                      ></div>
                    </div>
                    <span className="text-sm font-medium text-gray-900 w-8 text-right">
                      {item.value}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Gráfico de Vendas por Mês */}
          <div className="bg-white rounded-lg p-6 shadow-sm border">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Vendas por Mês</h3>
            <div className="space-y-3">
              {relatoriosData.vendasPorMes.map((item, index) => (
                <div key={index} className="flex items-center justify-between">
                  <span className="text-sm text-gray-600 w-8">{item.mes}</span>
                  <div className="flex-1 mx-4">
                    <div className="w-full bg-gray-200 rounded-full h-6 relative">
                      <div 
                        className="bg-blue-500 h-6 rounded-full flex items-center justify-end pr-2"
                        style={{ width: `${(item.vendas / 30000) * 100}%` }}
                      >
                        <span className="text-xs text-white font-medium">
                          R$ {(item.vendas / 1000).toFixed(0)}k
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Tabela de Performance dos Vendedores */}
        <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
            <h3 className="text-lg font-medium text-gray-900">Performance dos Vendedores</h3>
            <button className="flex items-center space-x-2 px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors">
              <Download className="w-4 h-4" />
              <span>Exportar PDF</span>
            </button>
          </div>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Vendedor
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Total de Vendas
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Oportunidades
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Ticket Médio
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Taxa de Conversão
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {relatoriosData.performanceVendedores.map((vendedor, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {vendedor.vendedor}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      R$ {vendedor.vendas.toLocaleString('pt-BR')}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {vendedor.oportunidades}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      R$ {vendedor.ticketMedio.toLocaleString('pt-BR')}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      <div className="flex items-center">
                        <span className="text-sm font-medium text-gray-900 mr-2">
                          {vendedor.conversao}%
                        </span>
                        <div className="w-16 bg-gray-200 rounded-full h-2">
                          <div 
                            className="bg-green-500 h-2 rounded-full"
                            style={{ width: `${vendedor.conversao}%` }}
                          ></div>
                        </div>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Relatorios;
