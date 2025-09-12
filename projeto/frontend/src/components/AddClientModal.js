import React, { useState } from 'react';
import { X } from 'lucide-react';
import toast from 'react-hot-toast';

function AddClientModal({ isOpen, onClose, onAddClient }) {
  const [formData, setFormData] = useState({
    cnpj: '',
    telefone: '',
    telefoneAlternativo: '',
    razaoSocial: '',
    nomeFantasia: '',
    tipoEndereco: '',
    logradouro: '',
    numero: '',
    complemento: '',
    bairro: '',
    cidade: '',
    uf: '',
    email: '',
    site: '',
    redeSocial: '',
    dtInicioAtividade: '',
    cnaePrincipal: '',
    textoCnaePrincipal: '',
    faturamentoEstimado: '',
    quadroFuncionarios: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Validação básica
    if (!formData.razaoSocial || !formData.telefone || !formData.email) {
      toast.error('Preencha os campos obrigatórios: Razão Social, Telefone e E-mail');
      return;
    }

    // Criar novo cliente
    const newClient = {
      id: Date.now(), // ID único temporário
      nome: formData.razaoSocial,
      nomeFantasia: formData.nomeFantasia,
      telefone: formData.telefone,
      telefoneAlternativo: formData.telefoneAlternativo,
      email: formData.email,
      cnpj: formData.cnpj,
      endereco: `${formData.logradouro}, ${formData.numero} - ${formData.bairro}, ${formData.cidade}/${formData.uf}`,
      tipoEndereco: formData.tipoEndereco,
      complemento: formData.complemento,
      site: formData.site,
      redeSocial: formData.redeSocial,
      dtInicioAtividade: formData.dtInicioAtividade,
      cnaePrincipal: formData.cnaePrincipal,
      textoCnaePrincipal: formData.textoCnaePrincipal,
      faturamentoEstimado: formData.faturamentoEstimado,
      quadroFuncionarios: formData.quadroFuncionarios,
      dataCadastro: new Date().toLocaleString('pt-BR')
    };

    onAddClient(newClient);
    
    // Limpar formulário
    setFormData({
      cnpj: '',
      telefone: '',
      telefoneAlternativo: '',
      razaoSocial: '',
      nomeFantasia: '',
      tipoEndereco: '',
      logradouro: '',
      numero: '',
      complemento: '',
      bairro: '',
      cidade: '',
      uf: '',
      email: '',
      site: '',
      redeSocial: '',
      dtInicioAtividade: '',
      cnaePrincipal: '',
      textoCnaePrincipal: '',
      faturamentoEstimado: '',
      quadroFuncionarios: ''
    });

    toast.success('Cliente cadastrado com sucesso!');
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 className="text-xl font-semibold text-gray-900">
            Cadastro de Empresa Prospectada
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X className="w-6 h-6" />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {/* Identificação da Empresa */}
            <div className="md:col-span-2 lg:col-span-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Identificação da Empresa</h3>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">CNPJ</label>
              <input
                type="text"
                name="cnpj"
                value={formData.cnpj}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="00.000.000/0000-00"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Telefone</label>
              <input
                type="text"
                name="telefone"
                value={formData.telefone}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="(11) 99999-9999"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Telefone Alternativo</label>
              <input
                type="text"
                name="telefoneAlternativo"
                value={formData.telefoneAlternativo}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="(11) 88888-8888"
              />
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">Razão Social</label>
              <input
                type="text"
                name="razaoSocial"
                value={formData.razaoSocial}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Nome da empresa conforme registro"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Nome Fantasia</label>
              <input
                type="text"
                name="nomeFantasia"
                value={formData.nomeFantasia}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Nome comercial"
              />
            </div>

            {/* Endereço */}
            <div className="md:col-span-2 lg:col-span-3 mt-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Endereço</h3>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Tipo de Endereço</label>
              <input
                type="text"
                name="tipoEndereco"
                value={formData.tipoEndereco}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Comercial, Residencial"
              />
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">Logradouro</label>
              <input
                type="text"
                name="logradouro"
                value={formData.logradouro}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Rua, Avenida, etc."
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Número</label>
              <input
                type="text"
                name="numero"
                value={formData.numero}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="123"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Complemento</label>
              <input
                type="text"
                name="complemento"
                value={formData.complemento}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Sala, Andar, etc."
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Bairro</label>
              <input
                type="text"
                name="bairro"
                value={formData.bairro}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Centro, Vila Nova"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Cidade</label>
              <input
                type="text"
                name="cidade"
                value={formData.cidade}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="São Paulo"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">UF</label>
              <input
                type="text"
                name="uf"
                value={formData.uf}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="SP"
                maxLength="2"
              />
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">E-mail</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="contato@empresa.com.br"
                required
              />
            </div>

            {/* Informações Adicionais */}
            <div className="md:col-span-2 lg:col-span-3 mt-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Informações Adicionais</h3>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Site</label>
              <input
                type="url"
                name="site"
                value={formData.site}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="https://www.empresa.com.br"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Rede Social</label>
              <input
                type="url"
                name="redeSocial"
                value={formData.redeSocial}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="https://linkedin.com/company/empresa"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Dt. Início Atividade</label>
              <input
                type="date"
                name="dtInicioAtividade"
                value={formData.dtInicioAtividade}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">CNAE Principal</label>
              <input
                type="text"
                name="cnaePrincipal"
                value={formData.cnaePrincipal}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="1234-5/67"
              />
            </div>

            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-1">Texto CNAE Principal</label>
              <input
                type="text"
                name="textoCnaePrincipal"
                value={formData.textoCnaePrincipal}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Descrição da atividade principal"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Faturamento Estimado</label>
              <input
                type="text"
                name="faturamentoEstimado"
                value={formData.faturamentoEstimado}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="R$ 1.000.000,00"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Quadro de Funcionários</label>
              <input
                type="text"
                name="quadroFuncionarios"
                value={formData.quadroFuncionarios}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="50 funcionários"
              />
            </div>
          </div>

          {/* Botão Salvar */}
          <div className="flex justify-center mt-8">
            <button
              type="submit"
              className="bg-green-600 text-white px-8 py-3 rounded-lg font-medium hover:bg-green-700 transition-colors"
            >
              Salvar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddClientModal;
