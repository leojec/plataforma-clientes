import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from 'react-query';
import { api } from '../services/api';
import { useSidebar } from '../hooks/useSidebar';
import { 
  ArrowLeft, 
  Mail, 
  Settings2, 
  Plus,
  Calendar,
  User,
  Phone,
  MapPin,
  Building2
} from 'lucide-react';
import ActivityModal from '../components/ActivityModal';
import StatusModal from '../components/StatusModal';

function LeadDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { sidebarExpanded } = useSidebar();
  const [activeTab, setActiveTab] = useState('atividades');
  const [isActivityModalOpen, setIsActivityModalOpen] = useState(false);
  const [isStatusModalOpen, setIsStatusModalOpen] = useState(false);
  const [atividades, setAtividades] = useState([]);

  // Buscar dados reais do expositor
  const { data: leadData, isLoading, isError } = useQuery(
    ['lead', id],
    async () => {
      const res = await api.get(`/expositores/${id}`);
      return res.data;
    },
    { enabled: !!id, refetchOnWindowFocus: false, retry: 1 }
  );

  // Buscar atividades do lead
  const { data: atividadesData, isLoading: isLoadingAtividades, refetch: refetchAtividades } = useQuery(
    ['atividades', id],
    async () => {
      const res = await api.get(`/agenda/atividades/lead/${id}`);
      return res.data;
    },
    { 
      enabled: !!id, 
      refetchOnWindowFocus: false, 
      retry: 1,
      onSuccess: (data) => {
        setAtividades(data || []);
      }
    }
  );
  if (isLoading) {
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (isError || !leadData) {
    return (
      <div className="h-full flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="text-red-500 text-6xl mb-4">⚠️</div>
          <p className="text-red-600 mb-2">Erro ao carregar lead</p>
          <button
            onClick={() => navigate('/kanban')}
            className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Voltar ao Kanban
          </button>
        </div>
      </div>
    );
  }

  const leadInfo = {
    id: leadData.id,
    nome: leadData.nomeFantasia || leadData.razaoSocial,
    empresa: leadData.razaoSocial || '',
    email: leadData.email || '',
    telefone: leadData.telefone || leadData.celular || '',
    endereco: [leadData.endereco, leadData.cidade, leadData.estado].filter(Boolean).join(' - '),
    status: leadData.status,
    vendedor: leadData.vendedor?.nome || 'Não atribuído',
    dataCriacao: leadData.dataCadastro ? new Date(leadData.dataCadastro).toLocaleDateString('pt-BR') : '',
    valorEstimado: ''
  };


  const tabs = [
    { id: 'atividades', label: 'Atividades' },
    { id: 'dados', label: 'Dados' },
    { id: 'contatos', label: 'Lista Contatos' },
    { id: 'retomada', label: 'Retomada' }
  ];

  const handleSaveActivity = async (activityData) => {
    console.log('Atividade salva para o lead:', id, activityData);
    
    try {
      // Salvar atividade no backend
      const response = await api.post('/agenda/atividades', {
        ...activityData,
        leadId: `lead-${id}`
      });
      
      if (response.data.sucesso) {
        // Recarregar atividades do banco de dados
        refetchAtividades();
        
        setIsActivityModalOpen(false);
        
        // Mostrar mensagem de sucesso
        console.log('Atividade salva com sucesso na agenda!');
      }
    } catch (error) {
      console.error('Erro ao salvar atividade:', error);
      alert('Erro ao salvar atividade. Tente novamente.');
      setIsActivityModalOpen(false);
    }
  };

  const handleSaveStatus = async (statusData) => {
    console.log('📝 Status alterado para o lead:', id, statusData);
    
    try {
      // Atualizar status no backend
      const response = await api.put(`/expositores/${id}/status`, {
        status: statusData.newStatus
      });
      
      if (response.data.sucesso) {
        console.log('✅ Status atualizado no backend:', response.data);
        
        // Preparar dados da mudança de status
        const statusChange = {
          leadId: id.toString(),
          newStatus: statusData.newStatus,
          timestamp: new Date().toISOString()
        };
        
        console.log('🚀 Disparando evento statusChanged:', statusChange);
        
        // Disparar evento customizado para notificar o Kanban
        const event = new CustomEvent('statusChanged', { 
          detail: statusChange 
        });
        window.dispatchEvent(event);
        
        // Salvar no localStorage como backup
        localStorage.setItem('statusChange', JSON.stringify(statusChange));
        
        // Fechar modal
        setIsStatusModalOpen(false);
        
        // Navegar de volta para o Kanban para ver a mudança
        setTimeout(() => {
          console.log('🔄 Navegando de volta para o Kanban...');
          navigate('/kanban');
        }, 200);
      } else {
        console.error('❌ Erro ao atualizar status:', response.data);
        alert('Erro ao atualizar status. Tente novamente.');
      }
    } catch (error) {
      console.error('❌ Erro ao atualizar status:', error);
      alert('Erro ao atualizar status. Tente novamente.');
    }
  };

  return (
    <div className="h-full flex flex-col bg-white">
      {/* Header com navegação */}
      <div className={`bg-gradient-to-r from-blue-50 to-indigo-50 py-6 flex items-center justify-between transition-all duration-200 ease-out border-b border-blue-100 ${sidebarExpanded ? 'px-6' : 'px-8'}`}>
        <div className="flex items-center space-x-4">
          <button 
            onClick={() => navigate('/kanban')}
            className="btn-ghost p-2"
          >
            <ArrowLeft className="h-5 w-5" />
          </button>
          <div>
            <h1 className="text-2xl font-bold text-gray-900 tracking-tight">Detalhes do Lead</h1>
            <p className="text-sm text-gray-600 mt-0.5">{leadData.nomeFantasia || leadData.razaoSocial} - {leadData.razaoSocial}</p>
          </div>
        </div>
        
        <div className="flex items-center space-x-3">
          <button className="btn-success p-2">
            <Mail className="h-4 w-4" />
          </button>
          <button 
            onClick={() => setIsStatusModalOpen(true)}
            className="btn-secondary p-2"
            title="Alterar Status do Lead"
          >
            <Settings2 className="h-4 w-4" />
          </button>
          <button 
            onClick={() => setIsActivityModalOpen(true)}
            className="btn-primary p-2"
          >
            <Plus className="h-4 w-4" />
          </button>
        </div>
      </div>

      {/* Tabs de navegação */}
      <div className="border-b border-gray-200 bg-white">
        <nav className={`flex space-x-8 transition-all duration-200 ease-out ${sidebarExpanded ? 'px-6' : 'px-8'}`}>
          {tabs.map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`py-4 px-1 border-b-2 font-semibold text-sm transition-all duration-200 ease-out ${
                activeTab === tab.id
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </div>

      {/* Conteúdo das abas */}
      <div className={`flex-1 overflow-y-auto transition-all duration-200 ease-out ${sidebarExpanded ? 'p-6' : 'p-8'}`}>
        {activeTab === 'atividades' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900">Registro de Atividades</h2>
              <div className="bg-blue-600 text-white px-3 py-1 rounded-full text-sm font-medium">
                {leadData.vendedor?.nome || 'Não atribuído'}
              </div>
            </div>

            {/* Tabela de atividades */}
            <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
              {isLoadingAtividades ? (
                <div className="text-center py-8 text-gray-500">
                  <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto mb-4"></div>
                  <p className="text-sm">Carregando atividades...</p>
                </div>
              ) : atividades.length === 0 ? (
                <div className="text-center py-16">
                  <div className="w-16 h-16 mx-auto mb-6 bg-gradient-to-br from-blue-100 to-blue-200 rounded-2xl flex items-center justify-center shadow-lg">
                    <Settings2 className="w-8 h-8 text-blue-600" />
                  </div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">Nenhuma atividade registrada ainda</h3>
                  <p className="text-sm text-gray-500 mb-6">Clique no botão "+" para adicionar uma atividade</p>
                  <button 
                    onClick={() => setIsActivityModalOpen(true)}
                    className="btn-primary inline-flex items-center space-x-2"
                  >
                    <Plus className="w-4 h-4" />
                    <span>Adicionar Atividade</span>
                  </button>
                </div>
              ) : (
                <div className="table-container">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="table-header">
                      <tr>
                        <th className="table-cell">
                          Dt. Atividade
                        </th>
                        <th className="table-cell">
                          Tipo
                        </th>
                        <th className="table-cell">
                          Descrição
                        </th>
                        <th className="table-cell">
                          Agendamento
                        </th>
                        <th className="table-cell">
                          Usuário
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {atividades.map((atividade) => (
                        <tr key={atividade.id} className="table-row">
                          <td className="table-cell font-medium">
                            {atividade.data}
                          </td>
                          <td className="table-cell">
                            <span className="badge badge-success">
                              {atividade.tipo}
                            </span>
                          </td>
                          <td className="table-cell">
                            {atividade.descricao}
                            {atividade.link && (
                              <div className="mt-1">
                                <a 
                                  href={atividade.link} 
                                  target="_blank" 
                                  rel="noopener noreferrer"
                                  className="text-blue-600 hover:text-blue-800 text-xs font-medium"
                                >
                                  Ver link
                                </a>
                              </div>
                            )}
                          </td>
                          <td className="table-cell">
                            {atividade.agendamento}
                          </td>
                          <td className="table-cell">
                            <span className="badge badge-primary">
                              {atividade.usuario}
                            </span>
                          </td>
                      </tr>
                    ))}
                  </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === 'dados' && (
          <div className="space-y-6">
            <h2 className="text-xl font-bold text-gray-900">Dados do Lead</h2>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="card p-6 fade-in">
                <h3 className="text-lg font-semibold text-gray-900 mb-6">Informações Pessoais</h3>
                <div className="space-y-3">
                  <div className="flex items-center space-x-3">
                    <User className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.nomeFantasia || leadData.razaoSocial}</span>
                  </div>
                  <div className="flex items-center space-x-3">
                    <Mail className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.email}</span>
                  </div>
                  <div className="flex items-center space-x-3">
                    <Phone className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.telefone || leadData.celular}</span>
                  </div>
                </div>
              </div>

              <div className="card p-6 fade-in">
                <h3 className="text-lg font-semibold text-gray-900 mb-6">Informações da Empresa</h3>
                <div className="space-y-3">
                  <div className="flex items-center space-x-3">
                    <Building2 className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.razaoSocial}</span>
                  </div>
                  <div className="flex items-center space-x-3">
                    <MapPin className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{[leadData.endereco, leadData.cidade, leadData.estado].filter(Boolean).join(' - ')}</span>
                  </div>
                </div>
              </div>

              <div className="card p-6 fade-in">
                <h3 className="text-lg font-semibold text-gray-900 mb-6">Status e Vendas</h3>
                <div className="space-y-3">
                  <div>
                    <span className="text-sm font-medium text-gray-500">Status:</span>
                    <span className="ml-2 text-sm text-gray-600">{leadData.status}</span>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-500">Vendedor:</span>
                    <span className="ml-2 text-sm text-gray-600">{leadData.vendedor?.nome || 'Não atribuído'}</span>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-500">Valor Estimado:</span>
                    <span className="ml-2 text-sm text-gray-600">R$ 0,00</span>
                  </div>
                </div>
              </div>

              <div className="card p-6 fade-in">
                <h3 className="text-lg font-semibold text-gray-900 mb-6">Histórico</h3>
                <div className="space-y-3">
                  <div>
                    <span className="text-sm font-medium text-gray-500">Data de Criação:</span>
                    <span className="ml-2 text-sm text-gray-600">{leadData.dataCadastro ? new Date(leadData.dataCadastro).toLocaleDateString('pt-BR') : 'N/A'}</span>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-500">Última Atividade:</span>
                    <span className="ml-2 text-sm text-gray-600">18/09/2024</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'contatos' && (
          <div className="space-y-6">
            <h2 className="text-xl font-bold text-gray-900">Lista de Contatos</h2>
            <div className="card p-12 text-center">
              <div className="w-16 h-16 mx-auto mb-6 bg-gradient-to-br from-gray-100 to-gray-200 rounded-2xl flex items-center justify-center shadow-lg">
                <User className="w-8 h-8 text-gray-500" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Nenhum contato adicional cadastrado</h3>
              <p className="text-gray-500">Adicione contatos para ter mais informações sobre este lead</p>
            </div>
          </div>
        )}

        {activeTab === 'retomada' && (
          <div className="space-y-6">
            <h2 className="text-xl font-bold text-gray-900">Retomada de Contato</h2>
            <div className="card p-12 text-center">
              <div className="w-16 h-16 mx-auto mb-6 bg-gradient-to-br from-blue-100 to-blue-200 rounded-2xl flex items-center justify-center shadow-lg">
                <Calendar className="w-8 h-8 text-blue-600" />
              </div>
              <h3 className="text-lg font-semibold text-gray-900 mb-2">Nenhuma retomada agendada</h3>
              <p className="text-gray-500">Agende uma retomada de contato para acompanhar este lead</p>
            </div>
          </div>
        )}
      </div>

      {/* Modal de Cadastro de Atividade */}
      <ActivityModal
        isOpen={isActivityModalOpen}
        onClose={() => setIsActivityModalOpen(false)}
        onSave={handleSaveActivity}
        leadId={id}
      />

      {/* Modal de Alterar Status */}
      <StatusModal
        isOpen={isStatusModalOpen}
        onClose={() => setIsStatusModalOpen(false)}
        onSave={handleSaveStatus}
        leadId={id}
        currentStatus={leadData.status}
      />
    </div>
  );
}

export default LeadDetail;
