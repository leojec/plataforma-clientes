import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { 
  DndContext, 
  DragOverlay, 
  closestCenter,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
} from '@dnd-kit/core';
import {
  arrayMove,
  SortableContext,
  sortableKeyboardCoordinates,
  verticalListSortingStrategy,
} from '@dnd-kit/sortable';
import {
  useSortable,
} from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import { 
  Phone, 
  MapPin, 
  Calendar,
  Filter,
  Plus
} from 'lucide-react';
import AddClientModal from '../components/AddClientModal';

function KanbanBoard() {
  const navigate = useNavigate();
  
  // Buscar expositores do banco de dados
  const { data: expositoresData, isLoading, refetch } = useQuery(
    'expositores',
    () => api.get('/expositores').then(res => res.data),
    {
      refetchInterval: 30000,
    }
  );

  // Organizar expositores por status
  const [leads, setLeads] = useState({
    lead: [],
    emAndamento: [],
    emNegociacao: [],
    standFechado: []
  });

  // Atualizar leads quando os dados do backend chegarem
  useEffect(() => {
    if (expositoresData?.content) {
      const leadsPorStatus = {
        lead: [],
        emAndamento: [],
        emNegociacao: [],
        standFechado: []
      };

      expositoresData.content.forEach(expositor => {
        const leadData = {
          id: `lead-${expositor.id}`,
          nome: expositor.nomeFantasia || expositor.razaoSocial,
          endereco: expositor.endereco || 'Endereço não informado',
          telefone: expositor.telefone || expositor.celular || 'Telefone não informado',
          expositorId: expositor.id
        };

        // Mapear status do expositor para colunas do Kanban
        switch (expositor.status) {
          case 'POTENCIAL':
            leadsPorStatus.lead.push(leadData);
            break;
          case 'ATIVO':
            leadsPorStatus.emAndamento.push(leadData);
            break;
          case 'INATIVO':
            leadsPorStatus.standFechado.push(leadData);
            break;
          default:
            leadsPorStatus.lead.push(leadData);
        }
      });

      setLeads(leadsPorStatus);
    }
  }, [expositoresData]);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [activeId, setActiveId] = useState(null);

  // Função para mover lead entre colunas
  const moveLeadToColumn = (leadId, newStatus) => {
    console.log('🔄 Movendo lead:', leadId, 'para status:', newStatus);
    
    // Mapear status para colunas
    const statusToColumn = {
      'Lead': 'lead',
      'Em Andamento': 'emAndamento',
      'Em Negociação': 'emNegociacao',
      'Stand Fechado': 'standFechado'
    };
    
    const targetColumn = statusToColumn[newStatus];
    console.log('🎯 Coluna de destino:', targetColumn);
    
    if (!targetColumn) {
      console.log('❌ Status não mapeado:', newStatus);
      return;
    }
    
    setLeads(prevLeads => {
      console.log('📊 Estado atual:', prevLeads);
      
      // Encontrar o lead
      let leadToMove = null;
      let sourceColumn = null;
      
      for (const [columnName, leadsInColumn] of Object.entries(prevLeads)) {
        const foundLead = leadsInColumn.find(lead => lead.id === leadId);
        if (foundLead) {
          leadToMove = foundLead;
          sourceColumn = columnName;
          console.log(`✅ Lead encontrado em: ${columnName}`);
          break;
        }
      }
      
      if (!leadToMove) {
        console.log('❌ Lead não encontrado:', leadId);
        return prevLeads;
      }
      
      if (sourceColumn === targetColumn) {
        console.log('ℹ️ Lead já está na coluna correta');
        return prevLeads;
      }
      
      console.log(`🚀 Movendo de ${sourceColumn} para ${targetColumn}`);
      
      // Criar novo estado
      const newLeads = { ...prevLeads };
      
      // Remover da coluna atual
      newLeads[sourceColumn] = newLeads[sourceColumn].filter(lead => lead.id !== leadId);
      
      // Adicionar à nova coluna
      newLeads[targetColumn] = [...newLeads[targetColumn], leadToMove];
      
      console.log('✨ Novo estado:', newLeads);
      return newLeads;
    });
  };

  // Escutar mudanças de status
  useEffect(() => {
    const handleStatusChange = (event) => {
      console.log('📨 Evento recebido:', event.detail);
      const { leadId, newStatus } = event.detail;
      moveLeadToColumn(leadId, newStatus);
    };

    window.addEventListener('statusChanged', handleStatusChange);
    return () => window.removeEventListener('statusChanged', handleStatusChange);
  }, []);

  const sensors = useSensors(
    useSensor(PointerSensor),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  );

  const handleDragStart = (event) => {
    setActiveId(event.active.id);
  };

  const handleDragEnd = (event) => {
    const { active, over } = event;
    setActiveId(null);

    if (!over) return;

    const activeId = active.id;
    const overId = over.id;

    // Verificar se overId é uma string
    if (typeof overId !== 'string') return;

    // Encontrar a coluna de origem
    let sourceColumn = null;
    for (const [columnId, items] of Object.entries(leads)) {
      if (items.find(item => item.id === activeId)) {
        sourceColumn = columnId;
        break;
      }
    }

    // Encontrar a coluna de destino
    let targetColumn = null;
    if (overId.startsWith('column-')) {
      targetColumn = overId.replace('column-', '');
    } else {
      for (const [columnId, items] of Object.entries(leads)) {
        if (items.find(item => item.id === overId)) {
          targetColumn = columnId;
          break;
        }
      }
    }

    if (!sourceColumn || !targetColumn) return;

    // Permitir mover apenas para a próxima coluna sequencialmente
    const columnOrder = ['lead', 'emAndamento', 'emNegociacao', 'standFechado'];
    const sourceIndex = columnOrder.indexOf(sourceColumn);
    const targetIndex = columnOrder.indexOf(targetColumn);

    if (targetIndex !== sourceIndex + 1) {
      return; // Não permite pular colunas
    }

    // Remover da coluna de origem
    const sourceItems = leads[sourceColumn].filter(item => item.id !== activeId);
    const draggedItem = leads[sourceColumn].find(item => item.id === activeId);

    // Adicionar à coluna de destino
    const targetItems = [...leads[targetColumn]];
    
    // Se está sendo movido para "emAndamento", adicionar responsável
    if (targetColumn === 'emAndamento') {
      draggedItem.responsavel = 'Simoni Jarschel';
      draggedItem.dataContato = new Date().toLocaleString('pt-BR');
    }

    setLeads(prev => ({
      ...prev,
      [sourceColumn]: sourceItems,
      [targetColumn]: targetItems
    }));
  };

  const handleAddClient = (newExpositor) => {
    // Refetch dos dados para atualizar com dados reais do banco
    refetch();
  };

  // Componente LeadCard (simplificado para teste)
  const LeadCard = ({ lead, columnId }) => {
    const getResponsavelColor = (responsavel) => {
      return responsavel === 'Simoni Jarschel' ? 'bg-blue-500' : 'bg-green-500';
    };

    const handleCardClick = () => {
      console.log('Clicando no lead:', lead.id); // Debug
      navigate(`/lead/${lead.id}`);
    };

    return (
      <div
        className="p-3 sm:p-4 mb-3 bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow cursor-pointer"
        onClick={handleCardClick}
      >
        <div className="flex items-center justify-between mb-2">
          <h3 className="font-semibold text-gray-900 text-sm">{lead.nome}</h3>
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
          <div className="flex items-center mb-2 text-xs text-blue-600">
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

  if (isLoading) {
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <DndContext
      sensors={sensors}
      collisionDetection={closestCenter}
      onDragStart={handleDragStart}
      onDragEnd={handleDragEnd}
    >
      <div className="h-full flex flex-col bg-gray-50">
        {/* Header com fundo azul igual ao Dashboard */}
        <div className="bg-blue-100 px-4 sm:px-6 py-4 border-b border-gray-200">
          <div className="flex flex-col sm:flex-row sm:justify-between sm:items-center gap-4">
            <div>
              <h1 className="text-xl sm:text-2xl font-bold text-gray-800">Pipeline de Vendas</h1>
              <p className="text-sm sm:text-base text-gray-600">Gerencie seus leads e oportunidades</p>
            </div>
            <button 
              onClick={() => setIsModalOpen(true)}
              className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg flex items-center justify-center space-x-2 transition-colors w-full sm:w-auto"
            >
              <Plus className="h-5 w-5" />
              <span>Adicionar Cliente</span>
            </button>
          </div>
        </div>

        {/* Kanban Board - Área principal */}
        <div className="flex-1 p-4 sm:p-6 overflow-hidden">
          <div className="h-full overflow-x-auto">
            <div className="flex space-x-4 sm:space-x-6 min-w-max h-full">
              {columns.map((column) => (
                <div key={column.id} className={`flex-shrink-0 w-72 sm:w-80 ${column.bgColor} rounded-lg border-2 ${column.borderColor} flex flex-col`}>
                  {/* Column Header */}
                  <div className="p-4 border-b border-gray-200 flex-shrink-0">
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
                  <div className="flex-1 p-4 overflow-y-auto">
                    {leads[column.id].map((lead) => (
                      <LeadCard key={lead.id} lead={lead} columnId={column.id} />
                    ))}
                    
                    {/* Empty state */}
                    {leads[column.id].length === 0 && (
                      <div className="text-center py-8 text-gray-500 h-full flex flex-col items-center justify-center">
                        <div className="w-12 h-12 mb-2 bg-gray-200 rounded-lg flex items-center justify-center">
                          <div className="w-6 h-6 bg-gray-400 rounded-sm"></div>
                        </div>
                        <p className="text-sm">Nenhum lead nesta etapa</p>
                      </div>
                    )}
                  </div>

                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Modal de Adicionar Cliente */}
      <AddClientModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onAddClient={handleAddClient}
      />

      {/* Drag Overlay */}
      <DragOverlay>
        {activeId ? (
          <div className="p-4 mb-3 bg-white rounded-lg shadow-lg border border-gray-200 opacity-90">
            <div className="font-semibold text-gray-900 text-sm">
              {leads.lead.find(l => l.id === activeId)?.nome ||
               leads.emAndamento.find(l => l.id === activeId)?.nome ||
               leads.emNegociacao.find(l => l.id === activeId)?.nome ||
               leads.standFechado.find(l => l.id === activeId)?.nome}
            </div>
          </div>
        ) : null}
      </DragOverlay>
    </DndContext>
  );
}

export default KanbanBoard;