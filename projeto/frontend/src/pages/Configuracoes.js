import React from 'react';
import { Settings, User, Bell, Shield, Database, Palette, Calendar } from 'lucide-react';

function Configuracoes() {
  const configSections = [
    {
      title: 'Perfil do Usuário',
      icon: User,
      items: [
        'Informações pessoais',
        'Alterar senha',
        'Preferências de notificação'
      ]
    },
    {
      title: 'Sistema',
      icon: Settings,
      items: [
        'Configurações gerais',
        'Integrações',
        'Backup e restauração'
      ]
    },
    {
      title: 'Notificações',
      icon: Bell,
      items: [
        'Email',
        'Push notifications',
        'Alertas de sistema'
      ]
    },
    {
      title: 'Segurança',
      icon: Shield,
      items: [
        'Controle de acesso',
        'Logs de auditoria',
        'Políticas de senha'
      ]
    },
    {
      title: 'Banco de Dados',
      icon: Database,
      items: [
        'Configurações de conexão',
        'Limpeza de dados',
        'Exportação'
      ]
    },
    {
      title: 'Aparência',
      icon: Palette,
      items: [
        'Tema',
        'Idioma',
        'Layout personalizado'
      ]
    }
  ];

  return (
    <div className="h-full flex flex-col bg-gray-50">
      {/* Header */}
      <div className="bg-blue-100 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <h1 className="text-xl font-semibold text-gray-800">Configurações</h1>
          <span className="text-sm text-gray-600">Gerencie as configurações do sistema</span>
        </div>

        <div className="flex items-center justify-center">
          <button className="p-2 hover:bg-blue-200 rounded-lg transition-colors">
            <Calendar className="w-5 h-5 text-gray-600" />
          </button>
        </div>
      </div>

      {/* Content */}
      <div className="flex-1 overflow-y-auto p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {configSections.map((section, index) => {
            const Icon = section.icon;
            return (
              <div key={index} className="bg-white rounded-lg shadow-sm border p-4 hover:shadow-md transition-shadow">
                <div className="flex items-center mb-3">
                  <div className="p-2 bg-blue-100 rounded-lg">
                    <Icon className="h-5 w-5 text-blue-600" />
                  </div>
                  <h3 className="ml-3 text-base font-semibold text-gray-900">
                    {section.title}
                  </h3>
                </div>
                <ul className="space-y-1">
                  {section.items.map((item, itemIndex) => (
                    <li key={itemIndex} className="text-sm text-gray-600 hover:text-blue-600 cursor-pointer transition-colors">
                      • {item}
                    </li>
                  ))}
                </ul>
              </div>
            );
          })}
        </div>

        {/* Configurações Avançadas */}
        <div className="mt-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Configurações Avançadas</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <div className="bg-white p-4 border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow">
              <h4 className="font-medium text-gray-900 mb-2">API Keys</h4>
              <p className="text-sm text-gray-600 mb-3">Gerencie suas chaves de API</p>
              <button className="text-blue-600 hover:text-blue-800 text-sm font-medium">
                Configurar →
              </button>
            </div>
            <div className="bg-white p-4 border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow">
              <h4 className="font-medium text-gray-900 mb-2">Webhooks</h4>
              <p className="text-sm text-gray-600 mb-3">Configure notificações webhook</p>
              <button className="text-blue-600 hover:text-blue-800 text-sm font-medium">
                Configurar →
              </button>
            </div>
            <div className="bg-white p-4 border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow">
              <h4 className="font-medium text-gray-900 mb-2">Logs do Sistema</h4>
              <p className="text-sm text-gray-600 mb-3">Visualize logs de erro e atividade</p>
              <button className="text-blue-600 hover:text-blue-800 text-sm font-medium">
                Visualizar →
              </button>
            </div>
            <div className="bg-white p-4 border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition-shadow">
              <h4 className="font-medium text-gray-900 mb-2">Manutenção</h4>
              <p className="text-sm text-gray-600 mb-3">Modo de manutenção e limpeza</p>
              <button className="text-blue-600 hover:text-blue-800 text-sm font-medium">
                Acessar →
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Configuracoes;
