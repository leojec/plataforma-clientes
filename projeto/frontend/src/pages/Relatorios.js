import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSidebar } from '../hooks/useSidebar';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { 
  Calendar, 
  Download, 
  DollarSign,
  UserPlus,
  CheckSquare,
  UserMinus,
  Hand,
  CheckCircle,
  Filter,
  RefreshCw,
  TrendingUp,
  TrendingDown,
  BarChart3,
  Maximize2
} from 'lucide-react';

function Relatorios() {
  const navigate = useNavigate();
  const { sidebarExpanded } = useSidebar();
  const [filtroData, setFiltroData] = useState('ultimos-6-meses');
  const [mostrarFiltros, setMostrarFiltros] = useState(false);
  const [filtroStatus, setFiltroStatus] = useState('todos');

  // Buscar dados reais do sistema
  const { data: dashboardStats, isLoading: isLoadingStats } = useQuery(
    'dashboardStats',
    () => api.get('/dashboard/stats').then(res => res.data),
    {
      refetchInterval: 30000,
      retry: 3
    }
  );

  // Buscar dados de oportunidades por status
  const { data: oportunidadesPorStatus, isLoading: isLoadingOportunidades } = useQuery(
    'oportunidadesPorStatus',
    () => api.get('/relatorios/oportunidades-por-status').then(res => res.data),
    {
      refetchInterval: 60000,
    }
  );

  // Buscar dados de vendas por mês
  const { data: vendasPorMes, isLoading: isLoadingVendas } = useQuery(
    'vendasPorMes',
    () => api.get('/relatorios/vendas-por-mes').then(res => res.data),
    {
      refetchInterval: 60000,
    }
  );

  // Buscar dados de performance dos vendedores
  const { data: performanceVendedores, isLoading: isLoadingPerformance } = useQuery(
    'performanceVendedores',
    () => api.get('/relatorios/performance-vendedores').then(res => res.data),
    {
      refetchInterval: 60000,
    }
  );

  // Buscar resumo executivo (usando dados do dashboard)
  const { data: resumoExecutivo, isLoading: isLoadingResumo } = useQuery(
    'resumoExecutivo',
    async () => {
      const stats = await api.get('/dashboard/stats').then(res => res.data);
      const totalExpositores = await api.get('/expositores').then(res => res.data.length).catch(() => 0);
      const totalInteracoes = stats.qtdAtividades || 0;
      
      return {
        totalExpositores,
        totalOportunidades: (stats.qtdGanhos || 0) + (await api.get('/expositores').then(res => res.data.filter(e => e.status === 'ATIVO').length).catch(() => 0)),
        totalInteracoes,
        valorEmAberto: stats.valorPropostasAbertas || 0,
        valorTotalEstimado: (stats.valorPropostasAbertas || 0) + (stats.valorGanho || 0),
        valorGanho: stats.valorGanho || 0,
        metrosVendidos: stats.metrosQuadradosVendidos || 0
      };
    },
    {
      refetchInterval: 60000,
    }
  );

  // Buscar atividades por período com filtro dinâmico
  const { data: atividadesPorPeriodo, isLoading: isLoadingAtividades } = useQuery(
    ['atividadesPorPeriodo', filtroData],
    () => {
      const meses = filtroData === 'ultimos-3-meses' ? 3 : 
                   filtroData === 'ultimo-mes' ? 1 : 
                   filtroData === 'ultimo-ano' ? 12 : 6;
      return api.get(`/relatorios/atividades-por-periodo?meses=${meses}`).then(res => res.data);
    },
    {
      refetchInterval: 60000,
    }
  );

  // Buscar dados do gráfico de atividades
  const { data: graficoAtividades, isLoading: isLoadingGrafico } = useQuery(
    'graficoAtividades',
    () => api.get('/dashboard/atividades-grafico').then(res => res.data),
    {
      refetchInterval: 60000,
    }
  );

  const isLoading = isLoadingStats || isLoadingOportunidades || isLoadingVendas || 
                   isLoadingPerformance || isLoadingResumo || isLoadingAtividades || isLoadingGrafico;

  // Função para exportar relatórios
  const exportarRelatorio = (formato) => {
    const dados = {
      resumoExecutivo,
      oportunidadesPorStatus,
      vendasPorMes,
      performanceVendedores,
      atividadesPorPeriodo,
      dataExportacao: new Date().toLocaleString('pt-BR')
    };

    if (formato === 'excel') {
      // Simular exportação Excel (implementar com biblioteca como xlsx)
      console.log('Exportando Excel:', dados);
      alert('Funcionalidade de exportação Excel será implementada em breve!');
    } else if (formato === 'pdf') {
      // Simular exportação PDF (implementar com biblioteca como jsPDF)
      console.log('Exportando PDF:', dados);
      alert('Funcionalidade de exportação PDF será implementada em breve!');
    }
  };

  // Dados dos cards de resumo (usando dados reais do sistema)
  const summaryCards = [
    {
      id: 1,
      title: 'TOTAL EXPOSITORES',
      value: resumoExecutivo?.totalExpositores || 0,
      icon: UserPlus,
      color: 'bg-blue-500',
      iconBg: 'bg-blue-100'
    },
    {
      id: 2,
      title: 'TOTAL OPORTUNIDADES',
      value: resumoExecutivo?.totalOportunidades || 0,
      icon: CheckSquare,
      color: 'bg-blue-700',
      iconBg: 'bg-blue-100'
    },
    {
      id: 3,
      title: 'TOTAL INTERAÇÕES',
      value: resumoExecutivo?.totalInteracoes || 0,
      icon: UserMinus,
      color: 'bg-purple-500',
      iconBg: 'bg-purple-100'
    },
    {
      id: 4,
      title: 'VALOR EM ABERTO',
      value: `R$ ${(resumoExecutivo?.valorEmAberto || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`,
      icon: Hand,
      color: 'bg-orange-500',
      iconBg: 'bg-orange-100'
    },
    {
      id: 5,
      title: 'M² FECHADO',
      value: `${(resumoExecutivo?.metrosVendidos || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })} m²`,
      icon: Maximize2,
      color: 'bg-purple-600',
      iconBg: 'bg-purple-100'
    },
    {
      id: 6,
      title: 'VALOR GANHO',
      value: `R$ ${(resumoExecutivo?.valorGanho || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`,
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
      <div className={`bg-gradient-to-r from-blue-50 to-indigo-50 py-6 flex items-center justify-between transition-all duration-200 ease-out border-b border-blue-100 ${sidebarExpanded ? 'px-6' : 'px-8'}`}>
        <div className="flex items-center space-x-4">
          <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center shadow-lg">
            <BarChart3 className="w-5 h-5 text-white" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900 tracking-tight">Relatórios e Analytics</h1>
            <p className="text-sm text-gray-600 mt-0.5">Análise completa do seu negócio</p>
          </div>
        </div>

        <div className="flex items-center space-x-2">
          <button 
            onClick={() => setMostrarFiltros(!mostrarFiltros)}
            className="btn-secondary flex items-center space-x-2"
          >
            <Filter className="w-4 h-4" />
            <span>Filtros</span>
          </button>
          <button 
            onClick={() => window.location.reload()}
            className="btn-ghost p-2"
            title="Atualizar Dados"
          >
            <RefreshCw className="w-5 h-5" />
          </button>
          <button 
            onClick={() => navigate('/agenda')}
            className="p-2 hover:bg-blue-200 rounded-lg transition-colors"
            title="Abrir Agenda"
          >
            <Calendar className="w-5 h-5 text-gray-600" />
          </button>
        </div>
      </div>

      {/* Filtros */}
      {mostrarFiltros && (
        <div className="bg-white border-b border-gray-200 px-6 py-4">
          <div className="flex items-center space-x-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Período:</label>
              <select 
                value={filtroData} 
                onChange={(e) => setFiltroData(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="ultimos-6-meses">Últimos 6 meses</option>
                <option value="ultimos-3-meses">Últimos 3 meses</option>
                <option value="ultimo-mes">Último mês</option>
                <option value="ultimo-ano">Último ano</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Status das Oportunidades:</label>
              <select 
                value={filtroStatus} 
                onChange={(e) => setFiltroStatus(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="todos">Todos os Status</option>
                <option value="ativas">Apenas Ativas</option>
                <option value="fechadas">Apenas Fechadas</option>
              </select>
            </div>
            <div className="flex items-end">
              <button 
                onClick={() => {
                  // Refetch dos dados com novos filtros
                  window.location.reload();
                }}
                className="px-4 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700 transition-colors"
              >
                Aplicar Filtros
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Content */}
      <div className={`flex-1 overflow-y-auto transition-all duration-200 ease-out ${sidebarExpanded ? 'p-6' : 'p-8'}`}>
        {/* Cards de Resumo */}
        <div className={`grid gap-4 mb-8 transition-all duration-200 ease-out ${sidebarExpanded ? 'grid-cols-2 md:grid-cols-3 lg:grid-cols-6' : 'grid-cols-3 md:grid-cols-4 lg:grid-cols-7 xl:grid-cols-8'}`}>
          {summaryCards.map((card) => {
            const IconComponent = card.icon;
            return (
              <div key={card.id} className="card card-hover p-4 fade-in">
                <div className="flex items-center justify-between mb-3">
                  <div className={`p-3 rounded-xl shadow-lg ${card.iconBg}`}>
                    <IconComponent className={`w-5 h-5 ${card.color.replace('bg-', 'text-')}`} />
                  </div>
                </div>
                <div className="space-y-1">
                  <p className="text-xs font-semibold text-gray-600 uppercase tracking-wide">
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

        {/* Métricas de Crescimento */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="card p-6 fade-in">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold text-gray-900">Taxa de Conversão</h3>
              <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                <TrendingUp className="w-5 h-5 text-green-600" />
              </div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-green-600 mb-2">
                {resumoExecutivo?.totalOportunidades > 0 ? 
                  Math.round((resumoExecutivo?.valorGanho / resumoExecutivo?.valorTotalEstimado) * 100) : 0}%
              </div>
              <p className="text-sm text-gray-600">Taxa de fechamento de oportunidades</p>
            </div>
          </div>

          <div className="card p-6 fade-in">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold text-gray-900">Ticket Médio</h3>
              <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                <DollarSign className="w-5 h-5 text-blue-600" />
              </div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-blue-600 mb-2">
                R$ {resumoExecutivo?.totalOportunidades > 0 ? 
                  Math.round(resumoExecutivo?.valorTotalEstimado / resumoExecutivo?.totalOportunidades).toLocaleString('pt-BR') : 0}
              </div>
              <p className="text-sm text-gray-600">Valor médio por oportunidade</p>
            </div>
          </div>

          <div className="card p-6 fade-in">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold text-gray-900">Pipeline Ativo</h3>
              <div className="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center">
                <BarChart3 className="w-5 h-5 text-purple-600" />
              </div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-purple-600 mb-2">
                R$ {resumoExecutivo?.valorEmAberto?.toLocaleString('pt-BR', { minimumFractionDigits: 0 }) || 0}
              </div>
              <p className="text-sm text-gray-600">Valor em oportunidades ativas</p>
            </div>
          </div>
        </div>

        {/* Gráficos */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Gráfico de Oportunidades por Status */}
          <div className="bg-white rounded-lg p-6 shadow-sm border">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Oportunidades por Status</h3>
            <div className="space-y-4">
              {oportunidadesPorStatus?.dados?.map((item, index) => {
                const totalOportunidades = oportunidadesPorStatus.dados.reduce((sum, op) => sum + op.value, 0);
                return (
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
                            width: totalOportunidades > 0 ? `${(item.value / totalOportunidades) * 100}%` : '0%'
                          }}
                        ></div>
                      </div>
                      <span className="text-sm font-medium text-gray-900 w-8 text-right">
                        {item.value}
                      </span>
                    </div>
                  </div>
                );
              }) || (
                <div className="text-center text-gray-500 py-4">
                  Nenhuma oportunidade encontrada
                </div>
              )}
            </div>
          </div>

          {/* Gráfico de Vendas por Mês */}
          <div className="bg-white rounded-lg p-6 shadow-sm border">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Vendas por Mês</h3>
            <div className="space-y-3">
              {vendasPorMes?.dados?.map((item, index) => {
                const maxVendas = Math.max(...vendasPorMes.dados.map(v => v.vendas));
                return (
                  <div key={index} className="flex items-center justify-between">
                    <span className="text-sm text-gray-600 w-8">{item.mes}</span>
                    <div className="flex-1 mx-4">
                      <div className="w-full bg-gray-200 rounded-full h-6 relative">
                        <div 
                          className="bg-blue-500 h-6 rounded-full flex items-center justify-end pr-2"
                          style={{ width: maxVendas > 0 ? `${(item.vendas / maxVendas) * 100}%` : '0%' }}
                        >
                          <span className="text-xs text-white font-medium">
                            R$ {(item.vendas / 1000).toFixed(0)}k
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                );
              }) || (
                <div className="text-center text-gray-500 py-4">
                  Nenhuma venda encontrada
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Resumo Executivo Detalhado */}
        <div className="bg-gradient-to-r from-blue-600 to-purple-600 rounded-lg p-6 text-white mb-8">
          <h3 className="text-xl font-bold mb-4">Resumo Executivo</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center">
              <div className="text-3xl font-bold mb-2">{resumoExecutivo?.totalOportunidades || 0}</div>
              <div className="text-blue-100">Oportunidades Ativas</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold mb-2">
                R$ {(resumoExecutivo?.valorGanho || 0).toLocaleString('pt-BR', { minimumFractionDigits: 0 })}
              </div>
              <div className="text-blue-100">Receita Confirmada</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold mb-2">
                {resumoExecutivo?.totalOportunidades > 0 ? 
                  Math.round((resumoExecutivo?.valorGanho / resumoExecutivo?.valorTotalEstimado) * 100) : 0}%
              </div>
              <div className="text-blue-100">Taxa de Conversão</div>
            </div>
          </div>
        </div>

        {/* Atividades por Período */}
        <div className="bg-white rounded-lg p-6 shadow-sm border mb-8">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Atividades por Período</h3>
          <div className="space-y-3">
            {atividadesPorPeriodo?.dados?.map((item, index) => {
              const maxAtividades = Math.max(...atividadesPorPeriodo.dados.map(a => a.atividades));
              return (
                <div key={index} className="flex items-center justify-between">
                  <span className="text-sm text-gray-600 w-8">{item.mes}</span>
                  <div className="flex-1 mx-4">
                    <div className="w-full bg-gray-200 rounded-full h-6 relative">
                      <div 
                        className="bg-indigo-500 h-6 rounded-full flex items-center justify-end pr-2"
                        style={{ width: maxAtividades > 0 ? `${(item.atividades / maxAtividades) * 100}%` : '0%' }}
                      >
                        <span className="text-xs text-white font-medium">
                          {item.atividades}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              );
            }) || (
              <div className="text-center text-gray-500 py-4">
                Nenhuma atividade encontrada
              </div>
            )}
          </div>
          {atividadesPorPeriodo?.totalAtividades && (
            <div className="mt-4 text-center">
              <p className="text-sm text-gray-600">
                Total de atividades no período: <span className="font-semibold">{atividadesPorPeriodo.totalAtividades}</span>
              </p>
            </div>
          )}
        </div>

        {/* Tabela de Performance dos Vendedores */}
        <div className="bg-white rounded-lg shadow-sm border overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
            <div>
              <h3 className="text-lg font-medium text-gray-900">Performance dos Vendedores</h3>
              <p className="text-sm text-gray-600 mt-1">Métricas de vendas e conversão por vendedor</p>
            </div>
            <div className="flex items-center space-x-2">
              <button 
                onClick={() => exportarRelatorio('excel')}
                className="flex items-center space-x-2 px-4 py-2 bg-green-600 text-white text-sm rounded-lg hover:bg-green-700 transition-colors"
              >
                <Download className="w-4 h-4" />
                <span>Exportar Excel</span>
              </button>
              <button 
                onClick={() => exportarRelatorio('pdf')}
                className="flex items-center space-x-2 px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors"
              >
                <Download className="w-4 h-4" />
                <span>Exportar PDF</span>
              </button>
            </div>
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
                {performanceVendedores?.dados?.map((vendedor, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {vendedor.vendedor}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      R$ {vendedor.vendas.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {vendedor.oportunidades}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      R$ {vendedor.ticketMedio.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
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
                )) || (
                  <tr>
                    <td colSpan="5" className="px-6 py-4 text-center text-gray-500">
                      Nenhum vendedor encontrado
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Resumo Executivo */}
        <div className="bg-gradient-to-r from-blue-600 to-purple-600 rounded-lg p-6 text-white mt-8">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="text-xl font-semibold">Resumo Executivo</h3>
              <p className="text-blue-100 mt-1">Visão geral do desempenho do CRM</p>
            </div>
            <div className="text-right">
              <p className="text-sm text-blue-100">Última atualização</p>
              <p className="text-sm font-medium">{new Date().toLocaleDateString('pt-BR')}</p>
            </div>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <div className="flex items-center justify-between mb-2">
                <UserPlus className="w-5 h-5" />
                <span className="text-sm text-blue-100">Expositores</span>
              </div>
              <div className="text-2xl font-bold">{resumoExecutivo?.totalExpositores || 0}</div>
              <div className="text-xs text-blue-100">Total cadastrado</div>
            </div>
            
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <div className="flex items-center justify-between mb-2">
                <CheckSquare className="w-5 h-5" />
                <span className="text-sm text-blue-100">Oportunidades</span>
              </div>
              <div className="text-2xl font-bold">{resumoExecutivo?.totalOportunidades || 0}</div>
              <div className="text-xs text-blue-100">Pipeline ativo</div>
            </div>
            
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <div className="flex items-center justify-between mb-2">
                <DollarSign className="w-5 h-5" />
                <span className="text-sm text-blue-100">Faturamento</span>
              </div>
              <div className="text-2xl font-bold">
                R$ {resumoExecutivo?.valorGanho?.toLocaleString('pt-BR', { minimumFractionDigits: 0 }) || 0}
              </div>
              <div className="text-xs text-blue-100">Realizado</div>
            </div>
            
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <div className="flex items-center justify-between mb-2">
                <TrendingUp className="w-5 h-5" />
                <span className="text-sm text-blue-100">Meta</span>
              </div>
              <div className="text-2xl font-bold">
                {resumoExecutivo?.totalOportunidades > 0 ? 
                  Math.round((resumoExecutivo?.valorGanho / resumoExecutivo?.valorTotalEstimado) * 100) : 0}%
              </div>
              <div className="text-xs text-blue-100">Taxa de conversão</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Relatorios;
