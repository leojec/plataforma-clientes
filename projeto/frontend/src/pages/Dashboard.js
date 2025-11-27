import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSidebar } from '../hooks/useSidebar';
import { 
  Calendar, 
  UserPlus,
  CheckSquare,
  UserMinus,
  Hand,
  CheckCircle,
  DollarSign,
  TrendingUp,
  TrendingDown,
  Maximize2
} from 'lucide-react';
import { useQuery } from 'react-query';
import { api } from '../services/api';

function Dashboard() {
  const navigate = useNavigate();
  const { sidebarExpanded } = useSidebar();

  // Buscar dados do dashboard da API
  const { data: dashboardStats, isLoading, error } = useQuery(
    'dashboardStats',
    () => api.get('/dashboard/stats').then(res => res.data),
    {
      refetchInterval: 30000, // Atualizar a cada 30 segundos
      retry: 3
    }
  );

  // Buscar dados do gráfico
  const { data: graficoData } = useQuery(
    'dashboardGrafico',
    () => api.get('/dashboard/atividades-grafico').then(res => res.data),
    {
      refetchInterval: 60000, // Atualizar a cada 1 minuto
    }
  );

  // Dados dos cards de resumo (dinâmicos)
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
      title: 'M² VENDIDOS',
      value: `${(dashboardStats?.metrosQuadradosVendidos || 0).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })} m²`,
      icon: Maximize2,
      color: 'bg-purple-600',
      iconBg: 'bg-purple-100'
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

  // Processar dados do gráfico vindos do backend
  const processChartData = () => {
    if (!graficoData || !graficoData.dados) return {};
    
    const chartData = {};
    const usuarios = graficoData.usuarios || [];
    
    // Inicializar arrays para cada usuário
    usuarios.forEach(usuario => {
      chartData[usuario] = [];
    });
    
    // Processar cada ponto de dados
    graficoData.dados.forEach(ponto => {
      const [dia, mes] = ponto.data.split('/');
      const ano = new Date().getFullYear();
      const data = new Date(ano, parseInt(mes) - 1, parseInt(dia));
      
      usuarios.forEach(usuario => {
        const quantidade = ponto[usuario] || 0;
        chartData[usuario].push({
          x: data,
          y: quantidade
        });
      });
    });
    
    return chartData;
  };
  
  const chartData = processChartData();

  // Inicializar o gráfico quando o componente montar
  useEffect(() => {
    const initializeChart = () => {
      if (window.CanvasJS && window.CanvasJS.Chart && document.getElementById("chartContainer")) {
        try {
          const chart = new window.CanvasJS.Chart("chartContainer", {
            animationEnabled: true,
            title: {
              text: "Atividades Realizadas",
              fontFamily: "Arial",
              fontSize: 16,
              fontWeight: "bold"
            },
            axisX: {
              title: "Dias",
              valueFormatString: "DD/MM"
            },
            axisY: {
              title: "Qtd. Atividades",
              minimum: 0,
              maximum: 30,
              interval: 10
            },
            data: Object.keys(chartData).map((usuario, index) => ({
              type: "line",
              name: usuario.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase()), // Formatar nome
              showInLegend: true,
              dataPoints: chartData[usuario] || [],
              color: index === 0 ? "#4285F4" : "#EA4335" // Cores diferentes para cada usuário
            })),
            legend: {
              cursor: "pointer",
              itemclick: function (e) {
                if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
                  e.dataSeries.visible = false;
                } else {
                  e.dataSeries.visible = true;
                }
                chart.render();
              }
            }
          });
          chart.render();
        } catch (error) {
          console.warn('Erro ao renderizar gráfico:', error);
        }
      } else {
        // Se CanvasJS não estiver carregado, tenta novamente em 100ms
        setTimeout(initializeChart, 100);
      }
    };

    // Aguarda um pouco para garantir que o CanvasJS esteja carregado
    setTimeout(initializeChart, 500);
  }, [graficoData, chartData]); // Re-renderizar quando os dados do gráfico mudarem

  // Loading state
  if (isLoading) {
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Carregando dados do dashboard...</p>
        </div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="text-red-500 text-6xl mb-4">⚠️</div>
          <p className="text-red-600 mb-2">Erro ao carregar dados</p>
          <p className="text-gray-500 text-sm">
            {typeof error.message === 'string' ? error.message : 'Erro desconhecido'}
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="h-full flex flex-col bg-gray-50">
      {/* Header */}
      <div className={`bg-gradient-to-r from-blue-50 to-indigo-50 py-6 flex items-center justify-between transition-all duration-200 ease-out border-b border-blue-100 ${sidebarExpanded ? 'px-6' : 'px-8'}`}>
        <div className="flex items-center space-x-4">
          <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center shadow-lg">
            <Calendar className="w-5 h-5 text-white" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900 tracking-tight">Dashboard</h1>
            <p className="text-sm text-gray-600 mt-0.5">Visão geral do seu negócio</p>
          </div>
        </div>

        <div className="flex items-center space-x-3">
          <button 
            onClick={() => navigate('/agenda')}
            className="btn-ghost flex items-center space-x-2"
            title="Abrir Agenda"
          >
            <Calendar className="w-4 h-4" />
            <span className="hidden sm:inline">Agenda</span>
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className={`flex-1 space-y-6 overflow-y-auto transition-all duration-200 ease-out ${sidebarExpanded ? 'p-6' : 'p-8'}`}>
        {/* Summary Cards */}
        <div className={`grid gap-8 transition-all duration-200 ease-out grid-cols-1 md:grid-cols-2 lg:grid-cols-3`}>
          {summaryCards.map((card) => (
            <div key={card.id} className="dashboard-card p-8 fade-in min-h-[200px]">
              <div className="flex flex-col h-full">
                {/* Header com ícone e trend */}
                <div className="flex items-start justify-between mb-6">
                  <div className={`p-4 rounded-2xl shadow-lg ${card.color}`}>
                    <card.icon className="w-8 h-8 text-white" />
                  </div>
                  
                  {/* Trend indicator */}
                  <div className="flex items-center">
                    <span className={`badge ${card.tendencia === 'up' ? 'badge-success' : 'badge-danger'} flex items-center space-x-1 px-3 py-1`}>
                      {card.tendencia === 'up' ? <TrendingUp className="w-4 h-4" /> : <TrendingDown className="w-4 h-4" />}
                      <span className="text-xs font-medium">{card.variacao}</span>
                    </span>
                  </div>
                </div>
                
                {/* Conteúdo principal */}
                <div className="flex-1">
                  <h3 className="text-lg font-semibold text-gray-700 mb-4 leading-tight">{card.title}</h3>
                  <p className="text-4xl font-bold text-gray-900 mb-2">{card.value}</p>
                  <p className="text-sm text-gray-500">vs. período anterior</p>
                </div>
                
                {/* Footer com gradiente sutil */}
                <div className="mt-6 pt-4 border-t border-gray-100">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-medium text-gray-500 uppercase tracking-wide">Última atualização</span>
                    <span className="text-xs text-gray-400">Agora</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Chart */}
        <div className="card p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="text-lg font-semibold text-gray-900">Atividades dos Últimos 30 Dias</h3>
              <p className="text-sm text-gray-600 mt-1">Performance da equipe</p>
            </div>
            <div className="flex items-center space-x-2">
              <div className="w-3 h-3 bg-blue-500 rounded-full"></div>
              <span className="text-sm text-gray-600">Atividades</span>
            </div>
          </div>
          <div id="chartContainer" style={{ height: "400px" }} className="rounded-lg"></div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;