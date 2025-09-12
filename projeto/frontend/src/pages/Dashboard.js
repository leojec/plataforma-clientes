import React, { useState, useEffect } from 'react';
import { 
  Menu, 
  RefreshCw, 
  Calendar, 
  List, 
  User, 
  FileText, 
  Shield, 
  Settings, 
  Printer, 
  Bot,
  UserPlus,
  CheckSquare,
  UserMinus,
  HandCoins,
  CheckCircle,
  DollarSign
} from 'lucide-react';

function Dashboard() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [selectedPeriod, setSelectedPeriod] = useState('Dia');

  // Dados dos cards de resumo
  const summaryCards = [
    {
      id: 1,
      title: 'NOVOS CLIENTES',
      value: '10',
      icon: UserPlus,
      color: 'bg-blue-500',
      iconBg: 'bg-blue-100'
    },
    {
      id: 2,
      title: 'QTD.ATIVIDADES',
      value: '7',
      icon: CheckSquare,
      color: 'bg-blue-700',
      iconBg: 'bg-blue-100'
    },
    {
      id: 3,
      title: 'QTD.PERDA',
      value: '0',
      icon: UserMinus,
      color: 'bg-red-500',
      iconBg: 'bg-red-100'
    },
    {
      id: 4,
      title: 'VALOR PROPOSTAS EM ABERTO',
      value: 'R$ 0,00',
      icon: HandCoins,
      color: 'bg-green-500',
      iconBg: 'bg-green-100'
    },
    {
      id: 5,
      title: 'QTD.GANHO',
      value: '26',
      icon: CheckCircle,
      color: 'bg-green-500',
      iconBg: 'bg-green-100'
    },
    {
      id: 6,
      title: 'VALOR GANHO',
      value: 'R$ 1.512.219,00',
      icon: DollarSign,
      color: 'bg-green-700',
      iconBg: 'bg-green-100'
    }
  ];

  // Dados do gráfico
  const chartData = {
    "Simoni Jarschel": [
      { x: new Date(2024, 7, 12), y: 5 },
      { x: new Date(2024, 7, 14), y: 8 },
      { x: new Date(2024, 7, 16), y: 12 },
      { x: new Date(2024, 7, 18), y: 28 },
      { x: new Date(2024, 7, 20), y: 15 },
      { x: new Date(2024, 7, 22), y: 10 },
      { x: new Date(2024, 7, 24), y: 6 },
      { x: new Date(2024, 7, 26), y: 8 },
      { x: new Date(2024, 7, 28), y: 12 },
      { x: new Date(2024, 7, 30), y: 18 },
      { x: new Date(2024, 8, 1), y: 22 },
      { x: new Date(2024, 8, 3), y: 15 },
      { x: new Date(2024, 8, 5), y: 8 },
      { x: new Date(2024, 8, 7), y: 12 },
      { x: new Date(2024, 8, 9), y: 20 },
      { x: new Date(2024, 8, 11), y: 25 }
    ],
    "Leonardo": [
      { x: new Date(2024, 7, 12), y: 2 },
      { x: new Date(2024, 7, 14), y: 5 },
      { x: new Date(2024, 7, 16), y: 3 },
      { x: new Date(2024, 7, 18), y: 4 },
      { x: new Date(2024, 7, 20), y: 2 },
      { x: new Date(2024, 7, 22), y: 3 },
      { x: new Date(2024, 7, 24), y: 1 },
      { x: new Date(2024, 7, 26), y: 2 },
      { x: new Date(2024, 7, 28), y: 3 },
      { x: new Date(2024, 7, 30), y: 4 },
      { x: new Date(2024, 8, 1), y: 2 },
      { x: new Date(2024, 8, 3), y: 5 },
      { x: new Date(2024, 8, 5), y: 3 },
      { x: new Date(2024, 8, 7), y: 2 },
      { x: new Date(2024, 8, 9), y: 4 },
      { x: new Date(2024, 8, 11), y: 5 }
    ]
  };

  // Inicializar o gráfico quando o componente montar
  useEffect(() => {
    if (window.CanvasJS) {
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
        data: [
          {
            type: "line",
            name: "Simoni Jarschel",
            showInLegend: true,
            dataPoints: chartData["Simoni Jarschel"]
          },
          {
            type: "line",
            name: "Leonardo",
            showInLegend: true,
            dataPoints: chartData["Leonardo"]
          }
        ],
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
    }
  }, []);

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className={`${sidebarOpen ? 'w-64' : 'w-16'} bg-gray-800 transition-all duration-300 flex flex-col`}>
        {/* Logo */}
        <div className="p-4 flex items-center">
          <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center text-white font-bold text-sm">
            A
          </div>
          {sidebarOpen && (
            <span className="ml-3 text-white font-bold text-lg">APLEADS</span>
          )}
        </div>

        {/* Menu Toggle */}
        <button
          onClick={() => setSidebarOpen(!sidebarOpen)}
          className="p-2 text-white hover:bg-gray-700 transition-colors"
        >
          <Menu className="w-5 h-5" />
        </button>

        {/* Navigation Icons */}
        <nav className="flex-1 px-2 py-4 space-y-2">
          {[
            { icon: List, label: 'Lista' },
            { icon: FileText, label: 'Documentos' },
            { icon: Shield, label: 'Segurança' },
            { icon: FileText, label: 'Relatórios' },
            { icon: Settings, label: 'Configurações' },
            { icon: Printer, label: 'Impressão' },
            { icon: Bot, label: 'Automação' }
          ].map((item, index) => (
            <button
              key={index}
              className="w-full p-3 text-gray-300 hover:text-white hover:bg-gray-700 rounded-lg transition-colors flex items-center"
            >
              <item.icon className="w-5 h-5" />
              {sidebarOpen && <span className="ml-3 text-sm">{item.label}</span>}
            </button>
          ))}
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col">
        {/* Header */}
        <header className="bg-blue-100 px-6 py-4 flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <Menu className="w-5 h-5 text-gray-600" />
            </button>
            <h1 className="text-xl font-semibold text-gray-800">DashBoard</h1>
          </div>

          <div className="flex items-center space-x-4">
            {/* Notification Badge */}
            <div className="relative">
              <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
                <RefreshCw className="w-5 h-5 text-gray-600" />
              </button>
              <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                59
              </span>
            </div>

            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <Calendar className="w-5 h-5 text-gray-600" />
            </button>

            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <List className="w-5 h-5 text-gray-600" />
            </button>

            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <User className="w-5 h-5 text-gray-600" />
            </button>
          </div>

          {/* Period Selector */}
          <div className="flex bg-white rounded-lg p-1">
            {['Dia', 'Semana', 'Mês'].map((period) => (
              <button
                key={period}
                onClick={() => setSelectedPeriod(period)}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${
                  selectedPeriod === period
                    ? 'bg-blue-600 text-white'
                    : 'text-gray-600 hover:text-gray-800'
                }`}
              >
                {period}
              </button>
            ))}
          </div>
        </header>

        {/* Main Content Area */}
        <main className="flex-1 p-6 bg-gray-50">
          {/* Summary Cards */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
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
        </main>
      </div>
    </div>
  );
}

export default Dashboard;