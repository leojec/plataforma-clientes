import React, { useState } from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { 
  Home, 
  List, 
  TrendingUp, 
  BarChart3, 
  Settings,
  Menu, 
  X, 
  LogOut,
  User,
  Search
} from 'lucide-react';

function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { user, logout } = useAuth();
  const location = useLocation();

  const navigation = [
    { name: 'Dashboard', href: '/', icon: Home },
    { name: 'Lista', href: '/kanban', icon: List },
    { name: 'Oportunidades', href: '/oportunidades', icon: TrendingUp },
    { name: 'Relatórios', href: '/relatorios', icon: BarChart3 },
    { name: 'Configurações', href: '/configuracoes', icon: Settings },
  ];

  const isCurrentPath = (path) => {
    if (path === '/') {
      return location.pathname === '/';
    }
    return location.pathname.startsWith(path);
  };

  return (
    <div className="h-screen flex overflow-hidden bg-gray-100">
      {/* Sidebar Mobile */}
      {sidebarOpen && (
        <div className="fixed inset-0 flex z-40 md:hidden">
          <div className="fixed inset-0 bg-gray-600 bg-opacity-75" onClick={() => setSidebarOpen(false)} />
          <div className="relative flex-1 flex flex-col max-w-xs w-full bg-slate-800">
            <div className="absolute top-0 right-0 -mr-12 pt-2">
              <button
                className="ml-1 flex items-center justify-center h-10 w-10 rounded-full focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
                onClick={() => setSidebarOpen(false)}
              >
                <X className="h-6 w-6 text-white" />
              </button>
            </div>
            <SidebarContent navigation={navigation} isCurrentPath={isCurrentPath} />
          </div>
        </div>
      )}

      {/* Sidebar Desktop - CRM Shot Style */}
      <div className="hidden md:flex md:flex-shrink-0">
        <div className="flex flex-col w-64 bg-slate-800">
          <SidebarContent navigation={navigation} isCurrentPath={isCurrentPath} />
        </div>
      </div>

      {/* Main Content */}
      <div className="flex flex-col w-0 flex-1 overflow-hidden">
        {/* Top Navigation */}
        <div className="relative z-10 flex-shrink-0 flex h-16 bg-white shadow">
          <button
            className="px-4 border-r border-gray-200 text-gray-500 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-primary-500 md:hidden"
            onClick={() => setSidebarOpen(true)}
          >
            <Menu className="h-6 w-6" />
          </button>
          
          <div className="flex-1 px-4 flex justify-between">
            <div className="flex-1 flex">
              <div className="w-full flex md:ml-0">
                <div className="relative w-full text-gray-400 focus-within:text-gray-600">
                  <div className="absolute inset-y-0 left-0 flex items-center pointer-events-none">
                    <span className="text-sm font-medium text-gray-900">
                      CRM Shot
                    </span>
                  </div>
                </div>
              </div>
            </div>
            
            <div className="ml-4 flex items-center md:ml-6">
              <div className="ml-3 relative">
                <div className="flex items-center space-x-3">
                  <div className="flex items-center space-x-2">
                    <User className="h-5 w-5 text-gray-400" />
                    <span className="text-sm font-medium text-gray-700">
                      {user?.nome || 'Usuário'}
                    </span>
                  </div>
                  <button
                    onClick={logout}
                    className="flex items-center space-x-1 text-sm text-gray-500 hover:text-gray-700"
                  >
                    <LogOut className="h-4 w-4" />
                    <span>Sair</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Page Content */}
        <main className="flex-1 relative overflow-y-auto focus:outline-none">
          <div className="h-full">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
}

function SidebarContent({ navigation, isCurrentPath }) {
  return (
    <div className="flex flex-col h-full bg-slate-800">
      {/* Header com Logo CRM Shot */}
      <div className="flex items-center px-6 py-4 border-b border-slate-700">
        {/* Logo CRM Shot */}
        <div className="flex items-center">
          <div className="w-8 h-8 bg-red-500 rounded-lg flex items-center justify-center relative">
            {/* Speech bubble icon */}
            <div className="w-6 h-6 border-2 border-white rounded-lg relative">
              {/* Growth bars inside */}
              <div className="absolute bottom-1 left-1 flex items-end space-x-0.5">
                <div className="w-1 h-2 bg-white"></div>
                <div className="w-1 h-3 bg-white"></div>
                <div className="w-1 h-4 bg-white"></div>
              </div>
              {/* Growth arrow */}
              <div className="absolute top-0 right-0 w-0 h-0 border-l-2 border-b-2 border-white transform rotate-45"></div>
            </div>
          </div>
          <div className="ml-3">
            <div className="text-white font-bold text-sm leading-none">CRM</div>
            <div className="text-red-400 font-bold text-xs leading-none">SHOT</div>
          </div>
        </div>
      </div>

      {/* Barra de Pesquisa */}
      <div className="px-6 py-4 border-b border-slate-700">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-slate-400" />
          <input
            type="text"
            placeholder=""
            className="w-full pl-10 pr-4 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>
      </div>

      {/* Navigation Menu */}
      <nav className="flex-1 px-4 py-4 space-y-2">
        {navigation.map((item) => {
          const Icon = item.icon;
          return (
            <Link
              key={item.name}
              to={item.href}
              className={`${
                isCurrentPath(item.href)
                  ? 'bg-slate-700 text-white'
                  : 'text-slate-300 hover:bg-slate-700 hover:text-white'
              } group flex items-center px-3 py-3 text-sm font-medium rounded-lg transition-colors`}
            >
              <Icon
                className={`${
                  isCurrentPath(item.href) ? 'text-white' : 'text-slate-400 group-hover:text-white'
                } mr-3 h-5 w-5`}
              />
              {item.name}
            </Link>
          );
        })}
      </nav>
    </div>
  );
}

export default Layout;
