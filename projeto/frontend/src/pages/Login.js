import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { Eye, EyeOff, LogIn, UserPlus, X } from 'lucide-react';
import { api } from '../services/api';

function Login() {
  const [formData, setFormData] = useState({
    email: '',
    senha: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showCreateUser, setShowCreateUser] = useState(false);
  const [createUserData, setCreateUserData] = useState({
    nome: '',
    email: '',
    senha: '',
    confirmarSenha: ''
  });
  const [creatingUser, setCreatingUser] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleCreateUserChange = (e) => {
    setCreateUserData({
      ...createUserData,
      [e.target.name]: e.target.value
    });
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    
    if (createUserData.senha !== createUserData.confirmarSenha) {
      toast.error('As senhas não coincidem');
      return;
    }

    if (createUserData.senha.length < 6) {
      toast.error('A senha deve ter pelo menos 6 caracteres');
      return;
    }

    setCreatingUser(true);
    try {
      await api.post('/auth/register', {
        nome: createUserData.nome,
        email: createUserData.email,
        senha: createUserData.senha,
        perfil: 'VENDEDOR'
      });
      
      toast.success('Usuário criado com sucesso! Faça login para continuar.');
      setShowCreateUser(false);
      setCreateUserData({ nome: '', email: '', senha: '', confirmarSenha: '' });
    } catch (error) {
      const errorMsg = error.response?.data?.message || 'Erro ao criar usuário';
      toast.error(typeof errorMsg === 'string' ? errorMsg : 'Erro ao criar usuário');
    } finally {
      setCreatingUser(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const result = await login(formData.email, formData.senha);
      
      if (result.success) {
        toast.success('Login realizado com sucesso!');
        navigate('/');
      } else {
        const errorMsg = result.message || 'Erro ao fazer login';
        toast.error(typeof errorMsg === 'string' ? errorMsg : 'Erro ao fazer login');
      }
    } catch (error) {
      const errorMessage = error?.message || 'Erro ao fazer login';
      toast.error(typeof errorMessage === 'string' ? errorMessage : 'Erro ao fazer login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div>
          {/* Logo CRM Shot */}
          <div className="mx-auto flex items-center justify-center">
            <div className="w-16 h-16 bg-red-500 rounded-lg flex items-center justify-center relative">
              {/* Speech bubble icon */}
              <div className="w-12 h-12 border-4 border-white rounded-lg relative">
                {/* Growth bars inside */}
                <div className="absolute bottom-2 left-2 flex items-end space-x-1">
                  <div className="w-1.5 h-4 bg-white"></div>
                  <div className="w-1.5 h-6 bg-white"></div>
                  <div className="w-1.5 h-8 bg-white"></div>
                </div>
                {/* Growth arrow */}
                <div className="absolute top-1 right-1 w-0 h-0 border-l-4 border-b-4 border-white transform rotate-45"></div>
              </div>
            </div>
            <div className="ml-4">
              <div className="text-gray-900 font-bold text-2xl leading-none">CRM</div>
              <div className="text-red-500 font-bold text-lg leading-none">SHOT</div>
            </div>
          </div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            CRM Shot Fair Brasil3
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600">
            Faça login em sua conta
          </p>
        </div>
        
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-4">
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                Email
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                className="input-field mt-1"
                placeholder="seu@email.com"
                value={formData.email}
                onChange={handleChange}
              />
            </div>
            
            <div>
              <label htmlFor="senha" className="block text-sm font-medium text-gray-700">
                Senha
              </label>
              <div className="mt-1 relative">
                <input
                  id="senha"
                  name="senha"
                  type={showPassword ? 'text' : 'password'}
                  required
                  className="input-field pr-10"
                  placeholder="Sua senha"
                  value={formData.senha}
                  onChange={handleChange}
                />
                <button
                  type="button"
                  className="absolute inset-y-0 right-0 pr-3 flex items-center"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? (
                    <EyeOff className="h-5 w-5 text-gray-400" />
                  ) : (
                    <Eye className="h-5 w-5 text-gray-400" />
                  )}
                </button>
              </div>
            </div>
          </div>

          <div>
            <button
              type="submit"
              disabled={loading}
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
          </div>

          <div className="text-center">
            <button
              type="button"
              onClick={() => setShowCreateUser(true)}
              className="text-sm text-blue-600 hover:text-blue-800 font-medium"
            >
              Criar conta de vendedor
            </button>
          </div>
        </form>
      </div>

      {/* Modal de Criar Usuário */}
      {showCreateUser && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg shadow-xl w-full max-w-md">
            <div className="flex items-center justify-between p-6 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-gray-900">Criar Conta de Vendedor</h3>
              <button
                onClick={() => setShowCreateUser(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleCreateUser} className="p-6 space-y-4">
              <div>
                <label htmlFor="nome" className="block text-sm font-medium text-gray-700">
                  Nome Completo
                </label>
                <input
                  id="nome"
                  name="nome"
                  type="text"
                  required
                  className="input-field mt-1"
                  placeholder="Seu nome completo"
                  value={createUserData.nome}
                  onChange={handleCreateUserChange}
                />
              </div>

              <div>
                <label htmlFor="emailCadastro" className="block text-sm font-medium text-gray-700">
                  Email
                </label>
                <input
                  id="emailCadastro"
                  name="email"
                  type="email"
                  required
                  className="input-field mt-1"
                  placeholder="seu@email.com"
                  value={createUserData.email}
                  onChange={handleCreateUserChange}
                />
              </div>

              <div>
                <label htmlFor="senhaCadastro" className="block text-sm font-medium text-gray-700">
                  Senha
                </label>
                <input
                  id="senhaCadastro"
                  name="senha"
                  type="password"
                  required
                  className="input-field mt-1"
                  placeholder="Mínimo 6 caracteres"
                  value={createUserData.senha}
                  onChange={handleCreateUserChange}
                />
              </div>

              <div>
                <label htmlFor="confirmarSenha" className="block text-sm font-medium text-gray-700">
                  Confirmar Senha
                </label>
                <input
                  id="confirmarSenha"
                  name="confirmarSenha"
                  type="password"
                  required
                  className="input-field mt-1"
                  placeholder="Confirme sua senha"
                  value={createUserData.confirmarSenha}
                  onChange={handleCreateUserChange}
                />
              </div>

              <div className="flex space-x-3 pt-4">
                <button
                  type="button"
                  onClick={() => setShowCreateUser(false)}
                  className="flex-1 py-2 px-4 border border-gray-300 text-gray-700 text-sm font-medium rounded-md hover:bg-gray-50"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  disabled={creatingUser}
                  className="flex-1 py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50"
                >
                  {creatingUser ? 'Criando...' : 'Criar Conta'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Login;
