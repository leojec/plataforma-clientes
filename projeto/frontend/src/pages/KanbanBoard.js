import React, { useState } from 'react';
import { 
  Phone, 
  MapPin, 
  Calendar,
  User,
  Filter,
  ChevronDown
} from 'lucide-react';

function KanbanBoard() {
  const [leads, setLeads] = useState({
    lead: [
      { id: 1, nome: 'Empresa A', telefone: '(11) 99999-9999', endereco: 'São Paulo/SP' },
      { id: 2, nome: 'Empresa B', telefone: '(21) 88888-8888', endereco: 'Rio de Janeiro/RJ' },
      { id: 3, nome: 'Empresa C', telefone: '(31) 77777-7777', endereco: 'Belo Horizonte/MG' },
      { id: 4, nome: 'Empresa D', telefone: '(41) 66666-6666', endereco: 'Curitiba/PR' },
      { id: 5, nome: 'Empresa E', telefone: '(51) 55555-5555', endereco: 'Porto Alegre/RS' }
    ],
    emAndamento: [
      { 
        id: 6, 
        nome: 'Empresa F', 
        telefone: '(11) 44444-4444', 
        endereco: 'Rua das Flores, 123 - São Paulo/SP',
        dataContato: '15/09/2024 14:30',
        responsavel: 'Simoni Jarschel',
        urgente: true
      },
      { 
        id: 7, 
        nome: 'Empresa G', 
        telefone: '(21) 33333-3333', 
        endereco: 'Av. Central, 456 - Rio de Janeiro/RJ',
        dataContato: '16/09/2024 10:00',
        responsavel: 'Leonardo',
        urgente: false
      }
    ],
    emNegociacao: [
      { 
        id: 8, 
        nome: 'Empresa H', 
        telefone: '(31) 22222-2222', 
        endereco: 'Praça da Liberdade, 789 - Belo Horizonte/MG',
        dataContato: '18/09/2024 09:30',
        responsavel: 'Simoni Jarschel',
        urgente: true
      },
      { 
        id: 9, 
        nome: 'Empresa I', 
        telefone: '(41) 11111-1111', 
        endereco: 'Rua XV de Novembro, 321 - Curitiba/PR',
        dataContato: '20/09/2024 15:45',
        responsavel: 'Leonardo',
        urgente: false
      }
    ],
    standFechado: [
      { 
        id: 10, 
        nome: 'Empresa J', 
        telefone: '(51) 99999-0000', 
        endereco: 'Av. Ipiranga, 654 - Porto Alegre/RS',
        responsavel: 'Simoni Jarschel'
      },
      { 
        id: 11, 
        nome: 'Empresa K', 
        telefone: '(11) 88888-0000', 
        endereco: 'Rua Augusta, 987 - São Paulo/SP',
        responsavel: 'Leonardo'
      }
    ]
  });

  const columns = [
    { 
      id: 'lead', 
      title: 'Lead', 
      count: leads.lead.length,
      bgColor: 'bg-gray-50',
      borderColor: 'border-gray-200'
    },
    { 
      id: 'emAndamento', 
      title: 'Em Andamento', 
      count: leads.emAndamento.length,
      bgColor: 'bg-blue-50',
      borderColor: 'border-blue-200'
    },
    { 
      id: 'emNegociacao', 
      title: 'Em Negociação', 
      count: leads.emNegociacao.length,
      bgColor: 'bg-yellow-50',
      borderColor: 'border-yellow-200'
    },
    { 
      id: 'standFechado', 
      title: 'Stand Fechado', 
      count: leads.standFechado.length,
      bgColor: 'bg-green-50',
      borderColor: 'border-green-200'
    }
  ];

  const LeadCard = ({ lead, columnId }) => {
    const getResponsavelColor = (responsavel) => {
      return responsavel === 'Simoni Jarschel' ? 'bg-blue-500' : 'bg-green-500';
    };

    return (
      <div className={`p-4 mb-3 bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow cursor-pointer ${
        lead.urgente ? 'border-red-300 bg-red-50' : ''
      }`}>
        <div className="flex items-center justify-between mb-2">
          <h3 className="font-semibold text-gray-900 text-sm">{lead.nome}</h3>
          {lead.urgente && (
            <div className="w-2 h-2 bg-red-500 rounded-full"></div>
          )}
        </div>
        
        {lead.endereco && (
          <div className="flex items-center mb-2 text-xs text-gray-600">
            <MapPin className="w-3 h-3 mr-1" />
            <span className="truncate">{lead.endereco}</span>
          </div>
        )}
        
        <div className="flex items-center mb-2 text-xs text-gray-600">
          <Phone className="w-3 h-3 mr-1" />
          <span>{lead.telefone}</span>
        </div>
        
        {lead.dataContato && (
          <div className={`flex items-center mb-2 text-xs ${
            lead.urgente ? 'text-red-600' : 'text-blue-600'
          }`}>
            <Calendar className="w-3 h-3 mr-1" />
            <span>{lead.dataContato}</span>
          </div>
        )}
        
        {lead.responsavel && (
          <div className="flex items-center justify-end">
            <span className={`px-2 py-1 rounded-full text-xs text-white ${getResponsavelColor(lead.responsavel)}`}>
              {lead.responsavel}
            </span>
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-16 bg-gray-800 transition-all duration-300 flex flex-col">
        {/* Logo */}
        <div className="p-4 flex items-center justify-center">
          <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center text-white font-bold text-sm">
            A
          </div>
        </div>

        {/* Navigation Icons */}
        <nav className="flex-1 px-2 py-4 space-y-2">
          {[
            { icon: 'List', label: 'Lista', active: true },
            { icon: 'FileText', label: 'Documentos' },
            { icon: 'Shield', label: 'Segurança' },
            { icon: 'Settings', label: 'Configurações' },
            { icon: 'Printer', label: 'Impressão' },
            { icon: 'Bot', label: 'Automação' },
            { icon: 'User', label: 'Usuários' },
            { icon: 'FileText', label: 'Relatórios' },
            { icon: 'Calendar', label: 'Agenda' },
            { icon: 'BarChart', label: 'Gráficos' }
          ].map((item, index) => (
            <button
              key={index}
              className={`w-full p-3 rounded-lg transition-colors flex items-center justify-center ${
                item.active 
                  ? 'bg-gray-700 text-white' 
                  : 'text-gray-300 hover:text-white hover:bg-gray-700'
              }`}
            >
              <div className="w-5 h-5 bg-current rounded-sm"></div>
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
              <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
            </button>
            <h1 className="text-xl font-semibold text-gray-800">
              Segmentação de Leads - Clientes e Prospects SFB (809)
            </h1>
          </div>

          <div className="flex items-center space-x-4">
            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
            </button>
            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
            </button>
            <div className="relative">
              <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
                <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
              </button>
              <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                59
              </span>
            </div>
            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
            </button>
            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
            </button>
            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
            </button>
            <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
              <div className="w-5 h-5 bg-gray-600 rounded-sm"></div>
            </button>
          </div>
        </header>

        {/* Kanban Board */}
        <main className="flex-1 p-6 bg-white overflow-x-auto">
          <div className="flex space-x-6 min-w-max">
            {columns.map((column) => (
              <div key={column.id} className={`flex-shrink-0 w-80 ${column.bgColor} rounded-lg border-2 ${column.borderColor}`}>
                {/* Column Header */}
                <div className="p-4 border-b border-gray-200">
                  <div className="flex items-center justify-between">
                    <h2 className="font-semibold text-gray-900">
                      {column.title} ({column.count})
                    </h2>
                    <button className="p-1 hover:bg-gray-200 rounded">
                      <Filter className="w-4 h-4 text-gray-500" />
                    </button>
                  </div>
                </div>

                {/* Column Content */}
                <div className="p-4 max-h-96 overflow-y-auto">
                  {leads[column.id].map((lead) => (
                    <LeadCard key={lead.id} lead={lead} columnId={column.id} />
                  ))}
                  
                  {/* Empty state */}
                  {leads[column.id].length === 0 && (
                    <div className="text-center py-8 text-gray-500">
                      <div className="w-12 h-12 mx-auto mb-2 bg-gray-200 rounded-lg flex items-center justify-center">
                        <div className="w-6 h-6 bg-gray-400 rounded-sm"></div>
                      </div>
                      <p className="text-sm">Nenhum lead nesta etapa</p>
                    </div>
                  )}
                </div>

                {/* Column Footer */}
                {column.id === 'lead' && (
                  <div className="p-4 border-t border-gray-200">
                    <div className="flex items-center justify-between text-sm text-gray-600">
                      <span>Próxima edição(1)</span>
                      <button className="p-1 hover:bg-gray-200 rounded">
                        <Filter className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                )}

                {column.id === 'standFechado' && (
                  <div className="p-4 border-t border-gray-200">
                    <div className="flex items-center justify-between text-sm text-gray-600">
                      <span>Perdido(2)</span>
                      <button className="p-1 hover:bg-gray-200 rounded">
                        <Filter className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </main>
      </div>
    </div>
  );
}

export default KanbanBoard;
