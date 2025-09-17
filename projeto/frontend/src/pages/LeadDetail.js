import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import { 
  ArrowLeft, 
  Mail, 
  RotateCcw, 
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
  const [activeTab, setActiveTab] = useState('atividades');
  const [isActivityModalOpen, setIsActivityModalOpen] = useState(false);
  const [isStatusModalOpen, setIsStatusModalOpen] = useState(false);
  const [atividades, setAtividades] = useState([]);

  // Dados fictícios do lead
  const leadData = {
    id: id,
    nome: 'João Silva',
    empresa: 'Tech Solutions Ltda',
    email: 'joao.silva@techsolutions.com',
    telefone: '(11) 99999-9999',
    endereco: 'Rua das Flores, 123 - São Paulo/SP',
    status: 'Em Andamento',
    vendedor: 'Administrador',
    dataCriacao: '15/09/2024',
    valorEstimado: 'R$ 15.000,00'
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
        leadId: id
      });
      
      if (response.data.sucesso) {
        // Criar nova atividade para exibir na tabela local
        const novaAtividade = {
          id: response.data.id,
          data: activityData.dataCriacao || new Date().toLocaleDateString('pt-BR'),
          descricao: activityData.descricao,
          agendamento: activityData.dataAgendamento && activityData.horarioAgendamento 
            ? `Sim - ${activityData.dataAgendamento}` 
            : 'Não',
          usuario: 'Administrador', // Pode ser dinâmico baseado no usuário logado
          tipo: activityData.tipoAtividade,
          link: activityData.link
        };

        // Adicionar à lista de atividades
        setAtividades(prevAtividades => [novaAtividade, ...prevAtividades]);
        
        setIsActivityModalOpen(false);
        
        // Mostrar mensagem de sucesso
        console.log('Atividade salva com sucesso na agenda!');
      }
    } catch (error) {
      console.error('Erro ao salvar atividade:', error);
      // Ainda adicionar localmente em caso de erro
      const novaAtividade = {
        id: Date.now(),
        data: activityData.dataCriacao || new Date().toLocaleDateString('pt-BR'),
        descricao: activityData.descricao,
        agendamento: activityData.dataAgendamento && activityData.horarioAgendamento 
          ? `Sim - ${activityData.dataAgendamento}` 
          : 'Não',
        usuario: 'Administrador',
        tipo: activityData.tipoAtividade,
        link: activityData.link
      };
      setAtividades(prevAtividades => [novaAtividade, ...prevAtividades]);
      setIsActivityModalOpen(false);
    }
  };

  const handleSaveStatus = (statusData) => {
    console.log('📝 Status alterado para o lead:', id, statusData);
    
    // Preparar dados da mudança de status
    const statusChange = {
      leadId: id,
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
  };

  return (
    <div className="h-full flex flex-col bg-white">
      {/* Header com navegação */}
      <div className="bg-blue-100 px-6 py-4 border-b border-gray-200">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <button 
              onClick={() => navigate('/kanban')}
              className="p-2 hover:bg-blue-200 rounded-lg transition-colors"
            >
              <ArrowLeft className="h-5 w-5 text-gray-600" />
            </button>
            <div>
              <h1 className="text-xl font-bold text-gray-800">Detalhes do Lead</h1>
              <p className="text-sm text-gray-600">{leadData.nome} - {leadData.empresa}</p>
            </div>
          </div>
          
          <div className="flex items-center space-x-2">
            <button className="bg-green-600 hover:bg-green-700 text-white p-2 rounded-lg transition-colors">
              <Mail className="h-5 w-5" />
            </button>
            <button 
              onClick={() => setIsStatusModalOpen(true)}
              className="bg-green-600 hover:bg-green-700 text-white p-2 rounded-lg transition-colors"
              title="Alterar Status do Lead"
            >
              <RotateCcw className="h-5 w-5" />
            </button>
            <button 
              onClick={() => setIsActivityModalOpen(true)}
              className="bg-blue-600 hover:bg-blue-700 text-white p-2 rounded-lg transition-colors"
            >
              <Plus className="h-5 w-5" />
            </button>
          </div>
        </div>
      </div>

      {/* Tabs de navegação */}
      <div className="border-b border-gray-200">
        <nav className="flex space-x-8 px-6">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                activeTab === tab.id
                  ? 'border-blue-600 text-blue-600'
                  : 'border-transparent text-blue-400 hover:text-blue-600 hover:border-gray-300'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </div>

      {/* Conteúdo das abas */}
      <div className="flex-1 p-6">
        {activeTab === 'atividades' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-gray-900">Registro de Atividades</h2>
              <div className="bg-blue-600 text-white px-3 py-1 rounded-full text-sm font-medium">
                {leadData.vendedor}
              </div>
            </div>

            {/* Tabela de atividades */}
            <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
              {atividades.length === 0 ? (
                <div className="text-center py-8 text-gray-500">
                  <div className="w-12 h-12 mx-auto mb-4 bg-gray-200 rounded-lg flex items-center justify-center">
                    <div className="w-6 h-6 bg-gray-400 rounded-sm"></div>
                  </div>
                  <p className="text-sm">Nenhuma atividade registrada ainda</p>
                  <p className="text-xs text-gray-400 mt-1">Clique no botão "+" para adicionar uma atividade</p>
                </div>
              ) : (
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Dt. Atividade
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Tipo
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Descrição
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Agendamento
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Usuário
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {atividades.map((atividade) => (
                      <tr key={atividade.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {atividade.data}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                            {atividade.tipo}
                          </span>
                        </td>
                        <td className="px-6 py-4 text-sm text-gray-900">
                          {atividade.descricao}
                          {atividade.link && (
                            <div className="mt-1">
                              <a 
                                href={atividade.link} 
                                target="_blank" 
                                rel="noopener noreferrer"
                                className="text-blue-600 hover:text-blue-800 text-xs underline"
                              >
                                Ver link
                              </a>
                            </div>
                          )}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {atividade.agendamento}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-blue-100 text-blue-800">
                            {atividade.usuario}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </div>
        )}

        {activeTab === 'dados' && (
          <div className="space-y-6">
            <h2 className="text-lg font-semibold text-gray-900">Dados do Lead</h2>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="bg-gray-50 p-6 rounded-lg">
                <h3 className="text-md font-medium text-gray-900 mb-4">Informações Pessoais</h3>
                <div className="space-y-3">
                  <div className="flex items-center space-x-3">
                    <User className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.nome}</span>
                  </div>
                  <div className="flex items-center space-x-3">
                    <Mail className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.email}</span>
                  </div>
                  <div className="flex items-center space-x-3">
                    <Phone className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.telefone}</span>
                  </div>
                </div>
              </div>

              <div className="bg-gray-50 p-6 rounded-lg">
                <h3 className="text-md font-medium text-gray-900 mb-4">Informações da Empresa</h3>
                <div className="space-y-3">
                  <div className="flex items-center space-x-3">
                    <Building2 className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.empresa}</span>
                  </div>
                  <div className="flex items-center space-x-3">
                    <MapPin className="h-5 w-5 text-gray-400" />
                    <span className="text-sm text-gray-600">{leadData.endereco}</span>
                  </div>
                </div>
              </div>

              <div className="bg-gray-50 p-6 rounded-lg">
                <h3 className="text-md font-medium text-gray-900 mb-4">Status e Vendas</h3>
                <div className="space-y-3">
                  <div>
                    <span className="text-sm font-medium text-gray-500">Status:</span>
                    <span className="ml-2 text-sm text-gray-600">{leadData.status}</span>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-500">Vendedor:</span>
                    <span className="ml-2 text-sm text-gray-600">{leadData.vendedor}</span>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-500">Valor Estimado:</span>
                    <span className="ml-2 text-sm text-gray-600">{leadData.valorEstimado}</span>
                  </div>
                </div>
              </div>

              <div className="bg-gray-50 p-6 rounded-lg">
                <h3 className="text-md font-medium text-gray-900 mb-4">Histórico</h3>
                <div className="space-y-3">
                  <div>
                    <span className="text-sm font-medium text-gray-500">Data de Criação:</span>
                    <span className="ml-2 text-sm text-gray-600">{leadData.dataCriacao}</span>
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
          <div className="space-y-4">
            <h2 className="text-lg font-semibold text-gray-900">Lista de Contatos</h2>
            <div className="bg-gray-50 p-8 rounded-lg text-center">
              <User className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">Nenhum contato adicional cadastrado</p>
            </div>
          </div>
        )}

        {activeTab === 'retomada' && (
          <div className="space-y-4">
            <h2 className="text-lg font-semibold text-gray-900">Retomada de Contato</h2>
            <div className="bg-gray-50 p-8 rounded-lg text-center">
              <Calendar className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <p className="text-gray-500">Nenhuma retomada agendada</p>
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
