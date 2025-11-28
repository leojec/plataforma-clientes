import React, { useState } from 'react';
import { X, Search } from 'lucide-react';
import toast from 'react-hot-toast';
import { api } from '../services/api';

function AddClientModal({ isOpen, onClose, onAddClient }) {
  const [loading, setLoading] = useState(false);
  const [buscandoCNPJ, setBuscandoCNPJ] = useState(false);
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


  const formatarCNPJ = (cnpj) => {
    const apenasNumeros = cnpj.replace(/\D/g, '');
    return apenasNumeros.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5');
  };


  const buscarDadosCNPJ = async (cnpj) => {

    const cnpjLimpo = cnpj.replace(/\D/g, '');


    if (cnpjLimpo.length !== 14) {
      return;
    }

    setBuscandoCNPJ(true);

    try {

      const response = await api.get(`/cnpj/${cnpjLimpo}`);
      const dados = response.data;

      if (dados.status === 'ERROR') {
        toast.error(dados.message || 'CNPJ inválido ou não encontrado');
        return;
      }


      setFormData(prev => ({
        ...prev,
        cnpj: formatarCNPJ(cnpjLimpo),
        razaoSocial: dados.nome || '',
        nomeFantasia: dados.fantasia || '',
        telefone: dados.telefone ? dados.telefone.replace(/\D/g, '') : '',
        email: dados.email || '',
        logradouro: dados.logradouro || '',
        numero: dados.numero || '',
        complemento: dados.complemento || '',
        bairro: dados.bairro || '',
        cidade: dados.municipio || '',
        uf: dados.uf || '',
        cnaePrincipal: dados.atividade_principal?.[0]?.code || '',
        textoCnaePrincipal: dados.atividade_principal?.[0]?.text || '',
        dtInicioAtividade: dados.abertura ? formatarDataParaInput(dados.abertura) : '',
        faturamentoEstimado: dados.capital_social || '',
        quadroFuncionarios: dados.qsa?.length ? `${dados.qsa.length} sócios` : ''
      }));

      toast.success('Dados da empresa carregados com sucesso!');

    } catch (error) {
      console.error('Erro ao buscar CNPJ:', error);
      toast.error('Erro ao buscar dados do CNPJ. Tente novamente.');
    } finally {
      setBuscandoCNPJ(false);
    }
  };


  const formatarDataParaInput = (data) => {

    const partes = data.split('/');
    if (partes.length === 3) {
      return `${partes[2]}-${partes[1]}-${partes[0]}`;
    }
    return '';
  };


  const handleCNPJChange = (e) => {
    const { value } = e.target;
    const cnpjLimpo = value.replace(/\D/g, '');


    setFormData(prev => ({
      ...prev,
      cnpj: value
    }));


    if (cnpjLimpo.length === 14 && !buscandoCNPJ) {
      buscarDadosCNPJ(value);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();


    if (!formData.razaoSocial || !formData.cnpj || !formData.email) {
      toast.error('Preencha os campos obrigatórios: Razão Social, CNPJ e E-mail');
      return;
    }

    setLoading(true);
    try {

      const expositorData = {
        razaoSocial: formData.razaoSocial,
        nomeFantasia: formData.nomeFantasia || null,
        cnpj: formData.cnpj || null,
        email: formData.email,
        telefone: formData.telefone,
        celular: formData.telefoneAlternativo || null,
        endereco: formData.logradouro && formData.numero ?
          `${formData.logradouro}, ${formData.numero}${formData.complemento ? ` - ${formData.complemento}` : ''} - ${formData.bairro}, ${formData.cidade}/${formData.uf}` : null,
        cidade: formData.cidade || null,
        estado: formData.uf || null,
        site: formData.site || null,
        descricao: `CNAE: ${formData.cnaePrincipal} - ${formData.textoCnaePrincipal}. Faturamento: ${formData.faturamentoEstimado}. Funcionários: ${formData.quadroFuncionarios}` || null,
        status: 'POTENCIAL'
      };


      const response = await api.post('/expositores', expositorData);


      if (onAddClient) {
        onAddClient(response.data);
      }


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

      toast.success('Expositor cadastrado com sucesso!');
      onClose();

    } catch (error) {
      console.error('Erro ao cadastrar expositor:', error);


      if (error.response?.status === 400) {
        const errorData = error.response.data;

        if (typeof errorData === 'object' && errorData !== null) {

          const errorMessages = Object.values(errorData).join(', ');
          toast.error(`Erro de validação: ${errorMessages}`);
        } else if (typeof errorData === 'string') {
          toast.error(errorData);
        } else {
          toast.error('Erro de validação nos dados enviados');
        }
      } else {
        const errorMsg = error.response?.data?.message || error.message || 'Erro ao cadastrar expositor';
        toast.error(typeof errorMsg === 'string' ? errorMsg : 'Erro ao cadastrar expositor');
      }
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        {}
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

        {}
        <form onSubmit={handleSubmit} className="p-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {}
            <div className="md:col-span-2 lg:col-span-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Identificação da Empresa</h3>
            </div>

            <div className="relative">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                CNPJ
                {buscandoCNPJ && (
                  <span className="ml-2 text-xs text-blue-600">
                    🔍 Buscando dados...
                  </span>
                )}
              </label>
              <div className="relative">
                <input
                  type="text"
                  name="cnpj"
                  value={formData.cnpj}
                  onChange={handleCNPJChange}
                  className="w-full px-3 py-2 pr-10 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                  placeholder="00.000.000/0000-00"
                  maxLength="18"
                />
                {buscandoCNPJ && (
                  <div className="absolute right-3 top-2.5">
                    <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-blue-600"></div>
                  </div>
                )}
                {!buscandoCNPJ && formData.cnpj.length > 0 && (
                  <Search className="absolute right-3 top-2.5 h-5 w-5 text-gray-400" />
                )}
              </div>
              <p className="text-xs text-gray-500 mt-1">
                Digite o CNPJ completo para buscar dados automaticamente
              </p>
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

            {}
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

            {}
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

          {}
          <div className="flex justify-center mt-8">
            <button
              type="submit"
              disabled={loading}
              className="bg-green-600 text-white px-8 py-3 rounded-lg font-medium hover:bg-green-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Salvando...' : 'Salvar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default AddClientModal;
