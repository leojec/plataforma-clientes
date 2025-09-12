import React, { useState } from 'react';
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
  const [leads, setLeads] = useState({
    lead: [],
    emAndamento: [],
    emNegociacao: [],
    standFechado: []
  });

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [activeId, setActiveId] = useState(null);

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

  const handleAddClient = (newClient) => {
    setLeads(prev => ({
      ...prev,
      lead: [...prev.lead, newClient]
    }));
  };

  // Componente SortableLeadCard
  const SortableLeadCard = ({ lead, columnId }) => {
    const {
      attributes,
      listeners,
      setNodeRef,
      transform,
      transition,
      isDragging,
    } = useSortable({ id: lead.id });

    const style = {
      transform: CSS.Transform.toString(transform),
      transition,
      opacity: isDragging ? 0.5 : 1,
    };

    const getResponsavelColor = (responsavel) => {
      return responsavel === 'Simoni Jarschel' ? 'bg-blue-500' : 'bg-green-500';
    };

    return (
      <div
        ref={setNodeRef}
        style={style}
        {...attributes}
        {...listeners}
        className={`p-4 mb-3 bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow cursor-grab active:cursor-grabbing ${
          lead.urgente ? 'border-red-300 bg-red-50' : ''
        }`}
      >
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

  return (
    <DndContext
      sensors={sensors}
      collisionDetection={closestCenter}
      onDragStart={handleDragStart}
      onDragEnd={handleDragEnd}
    >
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
                LISTA
              </h1>
            </div>

            <div className="flex items-center space-x-4">
              <button 
                onClick={() => setIsModalOpen(true)}
                className="flex items-center space-x-2 bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors"
              >
                <Plus className="w-4 h-4" />
                <span>Adicionar Cliente</span>
              </button>
              
              {/* Usuário */}
              <div className="flex items-center space-x-2">
                <button className="flex items-center space-x-2 bg-gray-100 hover:bg-gray-200 px-3 py-2 rounded-lg transition-colors">
                  <div className="w-6 h-6 bg-blue-600 rounded-full flex items-center justify-center text-white text-sm font-medium">
                    A
                  </div>
                  <span className="text-gray-700 font-medium">Admin</span>
                </button>
              </div>
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
                    <SortableContext 
                      items={leads[column.id].map(lead => lead.id)} 
                      strategy={verticalListSortingStrategy}
                    >
                      {leads[column.id].map((lead) => (
                        <SortableLeadCard key={lead.id} lead={lead} columnId={column.id} />
                      ))}
                    </SortableContext>
                    
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