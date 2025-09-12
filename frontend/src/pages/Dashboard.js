import React from 'react';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { Users, Building2, TrendingUp, DollarSign } from 'lucide-react';

function Dashboard() {
  const { data: stats, isLoading } = useQuery('dashboard-stats', async () => {
    // Aqui você faria chamadas para buscar estatísticas reais
    // Por enquanto, retornamos dados mockados
    return {
      totalExpositores: 45,
      totalOportunidades: 23,
      valorTotal: 125000,
      taxaConversao: 68
    };
  });

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  const cards = [
    {
      title: 'Total de Expositores',
      value: stats?.totalExpositores || 0,
      icon: Building2,
      color: 'bg-blue-500',
      change: '+12%'
    },
    {
      title: 'Oportunidades Ativas',
      value: stats?.totalOportunidades || 0,
      icon: TrendingUp,
      color: 'bg-green-500',
      change: '+8%'
    },
    {
      title: 'Valor Total (R$)',
      value: new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      }).format(stats?.valorTotal || 0),
      icon: DollarSign,
      color: 'bg-yellow-500',
      change: '+15%'
    },
    {
      title: 'Taxa de Conversão',
      value: `${stats?.taxaConversao || 0}%`,
      icon: Users,
      color: 'bg-purple-500',
      change: '+5%'
    }
  ];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600">Visão geral do seu CRM</p>
      </div>

      {/* Cards de Estatísticas */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {cards.map((card, index) => (
          <div key={index} className="card p-6">
            <div className="flex items-center">
              <div className={`p-3 rounded-lg ${card.color}`}>
                <card.icon className="h-6 w-6 text-white" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">{card.title}</p>
                <p className="text-2xl font-semibold text-gray-900">{card.value}</p>
                <p className="text-sm text-green-600">{card.change}</p>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Gráficos e Tabelas */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Oportunidades por Status</h3>
          <div className="space-y-3">
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-600">Prospecção</span>
              <span className="text-sm font-medium">8</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-600">Qualificação</span>
              <span className="text-sm font-medium">5</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-600">Proposta</span>
              <span className="text-sm font-medium">7</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="text-sm text-gray-600">Negociação</span>
              <span className="text-sm font-medium">3</span>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Atividades Recentes</h3>
          <div className="space-y-3">
            <div className="flex items-center space-x-3">
              <div className="w-2 h-2 bg-green-500 rounded-full"></div>
              <span className="text-sm text-gray-600">Nova oportunidade criada</span>
            </div>
            <div className="flex items-center space-x-3">
              <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
              <span className="text-sm text-gray-600">Expositor atualizado</span>
            </div>
            <div className="flex items-center space-x-3">
              <div className="w-2 h-2 bg-yellow-500 rounded-full"></div>
              <span className="text-sm text-gray-600">Follow-up agendado</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
