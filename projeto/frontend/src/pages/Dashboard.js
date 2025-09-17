import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Calendar, 
  UserPlus,
  CheckSquare,
  UserMinus,
  Hand,
  CheckCircle,
  DollarSign
} from 'lucide-react';
import { useQuery } from 'react-query';
import { api } from '../services/api';

function Dashboard() {
  const navigate = useNavigate();

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
  }, [graficoData]); // Re-renderizar quando os dados do gráfico mudarem

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
      <div className="bg-blue-100 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <h1 className="text-xl font-semibold text-gray-800">DashBoard</h1>
        </div>

        <div className="flex items-center justify-center">
          <button 
            onClick={() => navigate('/agenda')}
            className="p-2 hover:bg-blue-200 rounded-lg transition-colors"
            title="Abrir Agenda"
          >
            <Calendar className="w-5 h-5 text-gray-600" />
          </button>
        </div>

      </div>

      {/* Main Content */}
      <div className="flex-1 p-6 space-y-6 overflow-y-auto">
        {/* Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {summaryCards.map((card) => (
            <div key={card.id} className="bg-white rounded-lg shadow-sm p-6">
              <div className="flex items-center">
                <div className={`p-3 rounded-lg ${card.color}`}>
                  <card.icon className="w-6 h-6 text-white" />
                </div>
                <div className="ml-4">
                  <p className="text-sm font-medium text-gray-600">{card.title}</p>
                  <p className="text-2xl font-bold text-gray-900">{card.value}</p>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Chart */}
        <div className="bg-white rounded-lg shadow-sm p-6">
          <div id="chartContainer" style={{ height: "400px" }}></div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;