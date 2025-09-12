import React from 'react';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { Download, TrendingUp, Users, DollarSign } from 'lucide-react';

function Relatorios() {
  const { data: relatorios, isLoading } = useQuery('relatorios', async () => {
    // Dados mockados para demonstração
    return {
      oportunidadesPorStatus: [
        { name: 'Prospecção', value: 8, color: '#3B82F6' },
        { name: 'Qualificação', value: 5, color: '#F59E0B' },
        { name: 'Proposta', value: 7, color: '#EF4444' },
        { name: 'Negociação', value: 3, color: '#8B5CF6' },
        { name: 'Fechada', value: 12, color: '#10B981' }
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
        { vendedor: 'João Silva', vendas: 45000, oportunidades: 15 },
        { vendedor: 'Maria Santos', vendas: 38000, oportunidades: 12 },
        { vendedor: 'Pedro Costa', vendas: 32000, oportunidades: 10 },
        { vendedor: 'Ana Oliveira', vendas: 28000, oportunidades: 8 }
      ]
    };
  });

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
          <h1 className="text-2xl font-bold text-gray-900">Relatórios</h1>
          <p className="text-gray-600">Análise de performance e vendas</p>
        </div>
        <button className="btn-primary flex items-center space-x-2">
          <Download className="h-5 w-5" />
          <span>Exportar PDF</span>
        </button>
      </div>

      {/* Cards de Resumo */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-blue-500">
              <TrendingUp className="h-6 w-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Total de Vendas</p>
              <p className="text-2xl font-semibold text-gray-900">R$ 150.000</p>
              <p className="text-sm text-green-600">+12% vs mês anterior</p>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-green-500">
              <Users className="h-6 w-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Oportunidades Ativas</p>
              <p className="text-2xl font-semibold text-gray-900">23</p>
              <p className="text-sm text-green-600">+3 novas esta semana</p>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-yellow-500">
              <DollarSign className="h-6 w-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Ticket Médio</p>
              <p className="text-2xl font-semibold text-gray-900">R$ 6.500</p>
              <p className="text-sm text-green-600">+8% vs mês anterior</p>
            </div>
          </div>
        </div>

        <div className="card p-6">
          <div className="flex items-center">
            <div className="p-3 rounded-lg bg-purple-500">
              <TrendingUp className="h-6 w-6 text-white" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Taxa de Conversão</p>
              <p className="text-2xl font-semibold text-gray-900">68%</p>
              <p className="text-sm text-green-600">+5% vs mês anterior</p>
            </div>
          </div>
        </div>
      </div>

      {/* Gráficos */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Gráfico de Oportunidades por Status */}
        <div className="card p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Oportunidades por Status</h3>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={relatorios?.oportunidadesPorStatus}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, value }) => `${name}: ${value}`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {relatorios?.oportunidadesPorStatus.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Gráfico de Vendas por Mês */}
        <div className="card p-6">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Vendas por Mês</h3>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={relatorios?.vendasPorMes}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="mes" />
                <YAxis />
                <Tooltip formatter={(value) => [`R$ ${value.toLocaleString()}`, 'Vendas']} />
                <Bar dataKey="vendas" fill="#3B82F6" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Tabela de Performance dos Vendedores */}
      <div className="card overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">Performance dos Vendedores</h3>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="table-header">Vendedor</th>
                <th className="table-header">Total de Vendas</th>
                <th className="table-header">Oportunidades</th>
                <th className="table-header">Ticket Médio</th>
                <th className="table-header">Taxa de Conversão</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {relatorios?.performanceVendedores.map((vendedor, index) => (
                <tr key={index} className="hover:bg-gray-50">
                  <td className="table-cell font-medium text-gray-900">
                    {vendedor.vendedor}
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    R$ {vendedor.vendas.toLocaleString()}
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {vendedor.oportunidades}
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    R$ {(vendedor.vendas / vendedor.oportunidades).toLocaleString()}
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {Math.round((vendedor.oportunidades / (vendedor.oportunidades + 5)) * 100)}%
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default Relatorios;
