import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { api } from '../services/api';
import { Plus, Search, Edit, Trash2 } from 'lucide-react';
import toast from 'react-hot-toast';

function Expositores() {
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingExpositor, setEditingExpositor] = useState(null);
  const queryClient = useQueryClient();

  const { data: expositores = [], isLoading } = useQuery(
    ['expositores', searchTerm],
    async () => {
      const response = await api.get('/expositores', {
        params: { nome: searchTerm }
      });
      return response.data;
    }
  );

  const deleteMutation = useMutation(
    async (id) => {
      await api.delete(`/expositores/${id}`);
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries('expositores');
        toast.success('Expositor excluído com sucesso!');
      },
      onError: () => {
        toast.error('Erro ao excluir expositor');
      }
    }
  );

  const handleDelete = (id) => {
    if (window.confirm('Tem certeza que deseja excluir este expositor?')) {
      deleteMutation.mutate(id);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'ATIVO':
        return 'bg-green-100 text-green-800';
      case 'INATIVO':
        return 'bg-gray-100 text-gray-800';
      case 'BLOQUEADO':
        return 'bg-red-100 text-red-800';
      case 'POTENCIAL':
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Expositores</h1>
          <p className="text-gray-600">Gerencie seus expositores</p>
        </div>
        <button
          onClick={() => setShowModal(true)}
          className="btn-primary flex items-center space-x-2"
        >
          <Plus className="h-5 w-5" />
          <span>Novo Expositor</span>
        </button>
      </div>

      {}
      <div className="card p-4">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
          <input
            type="text"
            placeholder="Pesquisar expositores..."
            className="input-field pl-10"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {}
      <div className="card overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="table-header">Razão Social</th>
                <th className="table-header">CNPJ</th>
                <th className="table-header">Email</th>
                <th className="table-header">Status</th>
                <th className="table-header">Vendedor</th>
                <th className="table-header">Ações</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {expositores?.map((expositor) => (
                <tr key={expositor.id} className="hover:bg-gray-50">
                  <td className="table-cell">
                    <div>
                      <div className="font-medium text-gray-900">
                        {expositor.razaoSocial}
                      </div>
                      {expositor.nomeFantasia && (
                        <div className="text-sm text-gray-500">
                          {expositor.nomeFantasia}
                        </div>
                      )}
                    </div>
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {expositor.cnpj}
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {expositor.email}
                  </td>
                  <td className="table-cell">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(expositor.status)}`}>
                      {expositor.status}
                    </span>
                  </td>
                  <td className="table-cell text-sm text-gray-900">
                    {expositor.vendedor?.nome || '-'}
                  </td>
                  <td className="table-cell text-sm font-medium">
                    <div className="flex space-x-2">
                      <button
                        onClick={() => setEditingExpositor(expositor)}
                        className="text-primary-600 hover:text-primary-900"
                      >
                        <Edit className="h-4 w-4" />
                      </button>
                      <button
                        onClick={() => handleDelete(expositor.id)}
                        className="text-red-600 hover:text-red-900"
                      >
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {}
      {showModal && (
        <ExpositorModal
          expositor={editingExpositor}
          onClose={() => {
            setShowModal(false);
            setEditingExpositor(null);
          }}
        />
      )}
    </div>
  );
}

function ExpositorModal({ expositor, onClose }) {
  const queryClient = useQueryClient();
  const [formData, setFormData] = useState({
    razaoSocial: expositor?.razaoSocial || '',
    nomeFantasia: expositor?.nomeFantasia || '',
    cnpj: expositor?.cnpj || '',
    email: expositor?.email || '',
    telefone: expositor?.telefone || '',
    celular: expositor?.celular || '',
    endereco: expositor?.endereco || '',
    cidade: expositor?.cidade || '',
    estado: expositor?.estado || '',
    cep: expositor?.cep || '',
    site: expositor?.site || '',
    descricao: expositor?.descricao || '',
    status: expositor?.status || 'ATIVO'
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const createMutation = useMutation(
    async (data) => {
      const response = await api.post('/expositores', data);
      return response.data;
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries('expositores');
        toast.success('Expositor criado com sucesso!');
        onClose();
      },
      onError: (error) => {
        const errorMsg = error.response?.data?.message || error.message || 'Erro ao criar expositor';
        toast.error(typeof errorMsg === 'string' ? 'Erro ao criar expositor: ' + errorMsg : 'Erro ao criar expositor');
      }
    }
  );

  const handleSubmit = async (e) => {
    e.preventDefault();
    createMutation.mutate(formData);
  };

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="mt-3">
          <h3 className="text-lg font-medium text-gray-900 mb-4">
            {expositor ? 'Editar Expositor' : 'Novo Expositor'}
          </h3>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Razão Social *</label>
              <input
                type="text"
                name="razaoSocial"
                required
                className="input-field mt-1"
                value={formData.razaoSocial}
                onChange={handleChange}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Nome Fantasia</label>
              <input
                type="text"
                name="nomeFantasia"
                className="input-field mt-1"
                value={formData.nomeFantasia}
                onChange={handleChange}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">CNPJ *</label>
              <input
                type="text"
                name="cnpj"
                required
                className="input-field mt-1"
                value={formData.cnpj}
                onChange={handleChange}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Email</label>
              <input
                type="email"
                name="email"
                className="input-field mt-1"
                value={formData.email}
                onChange={handleChange}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Telefone</label>
              <input
                type="text"
                name="telefone"
                className="input-field mt-1"
                value={formData.telefone}
                onChange={handleChange}
              />
            </div>

            <div className="flex space-x-3 pt-4">
              <button
                type="submit"
                className="btn-primary flex-1"
                disabled={createMutation.isLoading}
              >
                {createMutation.isLoading ? 'Salvando...' : (expositor ? 'Atualizar' : 'Criar')}
              </button>
              <button
                type="button"
                onClick={onClose}
                className="btn-secondary flex-1"
              >
                Cancelar
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Expositores;
