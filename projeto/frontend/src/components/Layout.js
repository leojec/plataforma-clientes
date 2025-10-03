import React, { useState, useEffect } from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { 
  Home, 
  List, 
  BarChart3, 
  Settings,
  Calendar,
  Menu, 
  X, 
  LogOut,
  User,
  Search,
  ChevronLeft,
  ChevronRight
} from 'lucide-react';

function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [sidebarCollapsed, setSidebarCollapsed] = useState(true); // Começar minimizada
  const [sidebarHovered, setSidebarHovered] = useState(false);
  const { user, logout } = useAuth();
  const location = useLocation();

  const navigation = [
    { name: 'Dashboard', href: '/', icon: Home },
    { name: 'Lista', href: '/kanban', icon: List },
    { name: 'Relatórios', href: '/relatorios', icon: BarChart3 },
    { name: 'Agenda', href: '/agenda', icon: Calendar },
    { name: 'Configurações', href: '/configuracoes', icon: Settings },
  ];

  const isCurrentPath = (path) => {
    if (path === '/') {
      return location.pathname === '/';
    }
    return location.pathname.startsWith(path);
  };

  // Determinar se a sidebar deve estar expandida (hover ou não collapsed)
  const isExpanded = !sidebarCollapsed || sidebarHovered;
  
  // Delay para hover para evitar animações muito bruscas
  const [hoverTimeout, setHoverTimeout] = useState(null);
  
  const handleMouseEnter = () => {
    if (hoverTimeout) {
      clearTimeout(hoverTimeout);
    }
    setSidebarHovered(true);
  };
  
  const handleMouseLeave = () => {
    const timeout = setTimeout(() => {
      setSidebarHovered(false);
    }, 30); // Delay mínimo para evitar flickering mas manter fluidez
    setHoverTimeout(timeout);
  };

  // Cleanup do timeout quando o componente for desmontado
  useEffect(() => {
    return () => {
      if (hoverTimeout) {
        clearTimeout(hoverTimeout);
      }
    };
  }, [hoverTimeout]);

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
        <div 
          className={`flex flex-col bg-slate-800 transition-all duration-200 sidebar-transition sidebar-smooth ${isExpanded ? 'w-64' : 'w-16'} relative`}
          style={{
            transitionProperty: 'width, transform',
            willChange: 'width, transform'
          }}
          onMouseEnter={handleMouseEnter}
          onMouseLeave={handleMouseLeave}
        >
          <SidebarContent 
            navigation={navigation} 
            isCurrentPath={isCurrentPath} 
            collapsed={sidebarCollapsed}
            expanded={isExpanded}
            onToggleCollapse={() => setSidebarCollapsed(!sidebarCollapsed)}
          />
        </div>
      </div>

      {/* Main Content */}
      <div className="flex flex-col w-0 flex-1 overflow-hidden">
        {/* Top Navigation */}
        <div className="relative z-10 flex-shrink-0 flex h-16 bg-white/95 backdrop-blur-sm border-b border-gray-200/50 shadow-sm">
          <button
            className="px-4 border-r border-gray-200 text-gray-500 hover:text-gray-700 hover:bg-gray-50 transition-all duration-200 ease-out md:hidden"
            onClick={() => setSidebarOpen(true)}
          >
            <Menu className="h-6 w-6" />
          </button>
          
          <div className="flex-1 px-6 flex justify-between items-center">
            <div className="flex-1 flex">
              <div className="w-full flex md:ml-0">
                <div className="relative w-full text-gray-400 focus-within:text-gray-600">
                  <div className="absolute inset-y-0 left-0 flex items-center pointer-events-none">
                    <span className="text-lg font-semibold text-gray-900 tracking-tight">
                      CRM Shot
                    </span>
                  </div>
                </div>
              </div>
            </div>
            
            <div className="ml-4 flex items-center md:ml-6">
              <div className="ml-3 relative">
                <div className="flex items-center space-x-4">
                  <div className="flex items-center space-x-3 px-3 py-2 rounded-lg bg-gray-50">
                    <div className="w-8 h-8 bg-gradient-to-br from-blue-500 to-blue-600 rounded-full flex items-center justify-center">
                      <User className="h-4 w-4 text-white" />
                    </div>
                    <span className="text-sm font-medium text-gray-900">
                      {user?.nome || 'Usuário'}
                    </span>
                  </div>
                  <button
                    onClick={logout}
                    className="flex items-center space-x-2 px-3 py-2 text-sm text-gray-600 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all duration-200 ease-out"
                  >
                    <LogOut className="h-4 w-4" />
                    <span className="font-medium">Sair</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Page Content */}
        <main className={`flex-1 relative overflow-y-auto focus:outline-none transition-all duration-200 ease-out ${isExpanded ? 'ml-0' : 'ml-0'}`}>
          <div className="h-full">
            <Outlet context={{ sidebarExpanded: isExpanded }} />
          </div>
        </main>
      </div>
    </div>
  );
}

function SidebarContent({ navigation, isCurrentPath, collapsed, expanded, onToggleCollapse }) {
  return (
    <div className="flex flex-col h-full bg-slate-800">
      {/* Header com Logo CRM Shot */}
      <div className="flex items-center justify-between px-4 py-5 border-b border-slate-700">
        {/* Logo CRM Shot */}
        <div className="flex items-center min-w-0">
          <div className="w-9 h-9 bg-gradient-to-br from-red-500 to-red-600 rounded-xl flex items-center justify-center relative flex-shrink-0 shadow-lg">
            {/* Speech bubble icon */}
            <div className="w-7 h-7 border-2 border-white rounded-lg relative">
              {/* Growth bars inside */}
              <div className="absolute bottom-1 left-1 flex items-end space-x-0.5">
                <div className="w-1 h-2 bg-white rounded-sm"></div>
                <div className="w-1 h-3 bg-white rounded-sm"></div>
                <div className="w-1 h-4 bg-white rounded-sm"></div>
              </div>
              {/* Growth arrow */}
              <div className="absolute top-0 right-0 w-0 h-0 border-l-2 border-b-2 border-white transform rotate-45"></div>
            </div>
          </div>
          
          {/* Texto do logo com animação */}
          <div 
            className={`ml-3 transition-all duration-200 ease-in-out overflow-hidden ${expanded ? 'w-auto opacity-100' : 'w-0 opacity-0'}`}
            style={{
              transitionProperty: 'width, opacity, transform',
              willChange: 'width, opacity, transform'
            }}
          >
            <div className="text-white font-bold text-base leading-none whitespace-nowrap tracking-tight">CRM</div>
            <div className="text-red-300 font-bold text-xs leading-none whitespace-nowrap tracking-wider">SHOT</div>
          </div>
        </div>
        
        {/* Botão para minimizar/expandir - só aparece quando não está em hover */}
        <button
          onClick={onToggleCollapse}
          className={`p-2 rounded-lg text-slate-400 hover:text-white hover:bg-slate-700/50 transition-all duration-200 ease-out backdrop-blur-sm ${expanded && !collapsed ? 'opacity-100 scale-100' : 'opacity-0 scale-95 pointer-events-none'}`}
          title={collapsed ? "Expandir sidebar" : "Minimizar sidebar"}
        >
          <ChevronLeft className="h-4 w-4" />
        </button>
      </div>

      {/* Barra de Pesquisa */}
      <div className="px-4 py-4 border-b border-slate-700">
        <div className="relative">
          {expanded ? (
            <>
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-slate-400" />
              <input
                type="text"
                placeholder="Buscar..."
                className="w-full pl-10 pr-4 py-2.5 bg-slate-700/50 border border-slate-600/50 rounded-xl text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-transparent transition-all duration-200 ease-in-out backdrop-blur-sm"
                style={{
                  transitionProperty: 'all',
                  willChange: 'background-color, border-color'
                }}
              />
            </>
          ) : (
            <button
              className="w-full p-2.5 bg-slate-700/50 border border-slate-600/50 rounded-xl text-slate-400 hover:text-white hover:bg-slate-600/50 transition-all duration-200 ease-out backdrop-blur-sm"
              title="Buscar"
            >
              <Search className="h-4 w-4 mx-auto" />
            </button>
          )}
        </div>
      </div>

      {/* Navigation Menu */}
      <nav className="flex-1 px-4 py-4 space-y-1">
        {navigation.map((item) => {
          const Icon = item.icon;
          return (
            <Link
              key={item.name}
              to={item.href}
              className={`${
                isCurrentPath(item.href)
                  ? 'bg-blue-500/20 text-blue-100 border border-blue-400/30'
                  : 'text-slate-300 hover:bg-slate-700/50 hover:text-white hover:border-slate-600/50'
              } group flex items-center px-3 py-2.5 text-sm font-medium rounded-xl transition-all duration-200 ease-in-out border border-transparent backdrop-blur-sm`}
              title={!expanded ? item.name : undefined}
            >
              <Icon
                className={`${
                  isCurrentPath(item.href) ? 'text-blue-200' : 'text-slate-400 group-hover:text-white'
                } h-5 w-5 flex-shrink-0 transition-all duration-200 ease-in-out ${expanded ? 'mr-3' : 'mr-0'}`}
                style={{
                  transitionProperty: 'margin-right, color',
                  willChange: 'margin-right, color'
                }}
              />
              <span 
                className={`transition-all duration-200 ease-in-out overflow-hidden whitespace-nowrap font-medium ${expanded ? 'w-auto opacity-100 translate-x-0' : 'w-0 opacity-0 -translate-x-1'}`}
                style={{
                  transitionProperty: 'width, opacity, transform',
                  willChange: 'width, opacity, transform'
                }}
              >
                {item.name}
              </span>
            </Link>
          );
        })}
      </nav>
    </div>
  );
}

export default Layout;
