// src/components/shared/Navbar.tsx
import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuthStore } from '../../features/auth/store/authStore';

interface NavbarProps {
  isMenuOpen: boolean;
  setIsMenuOpen: (isOpen: boolean) => void;
}

export const Navbar = ({ isMenuOpen, setIsMenuOpen }: NavbarProps) => {
  const { isAuthenticated, logout, user } = useAuthStore();
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
  const location = useLocation();

  // Paleta de colores profesional
  const primaryColor = 'bg-[#1A365D]';
  const hoverPrimary = 'hover:bg-[#2C5282]';
  const textColor = 'text-white';
  const secondaryColor = 'bg-[#EBF8FF]';
  const secondaryText = 'text-[#2B6CB0]';
  const accentColor = 'bg-[#38B2AC]';
  const activeLinkColor = 'bg-[#2C5282] bg-opacity-50';

  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);
  const toggleProfileMenu = () => setIsProfileMenuOpen(!isProfileMenuOpen);

  const closeAllMenus = () => {
    setIsMenuOpen(false);
    setIsProfileMenuOpen(false);
  };

  const isActive = (path: string) => location.pathname === path;

  return (
    <>
      {/* Navbar Superior */}
      <nav className={`${primaryColor} ${textColor} shadow-md fixed w-full z-30`}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            {/* Logo y botón hamburguesa */}
            <div className="flex items-center">
              <button 
                onClick={toggleMenu}
                className="mr-4 p-1 rounded-md focus:outline-none md:hidden"
                aria-label="Menú principal"
              >
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              </button>
              
              <Link to="/" className="flex items-center" onClick={closeAllMenus}>
                <svg className="h-8 w-8" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z" fill="currentColor"/>
                  <path d="M12 6c-3.31 0-6 2.69-6 6s2.69 6 6 6 6-2.69 6-6-2.69-6-6-6zm0 10c-2.21 0-4-1.79-4-4s1.79-4 4-4 4 1.79 4 4-1.79 4-4 4z" fill="currentColor"/>
                </svg>
                <span className="ml-2 text-xl font-bold">Wallex</span>
              </Link>
            </div>

            {/* Menú desktop (centro) */}
            <div className="hidden md:flex items-center space-x-1">
              <Link 
                to="/dashboard" 
                onClick={closeAllMenus}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${isActive('/dashboard') ? activeLinkColor : `hover:bg-white hover:bg-opacity-20`}`}
              >
                Inicio
              </Link>
              <Link 
                to="/transactions" 
                onClick={closeAllMenus}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${isActive('/transactions') ? activeLinkColor : `hover:bg-white hover:bg-opacity-20`}`}
              >
                Transacciones
              </Link>
              <Link 
                to="/exchange" 
                onClick={closeAllMenus}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${isActive('/exchange') ? activeLinkColor : `hover:bg-white hover:bg-opacity-20`}`}
              >
                Cambio
              </Link>
            </div>

            {/* User actions */}
            <div className="flex items-center">
              {isAuthenticated() ? (
                <div className="flex items-center space-x-4">
                  <div className="hidden md:flex items-center space-x-2 bg-white bg-opacity-20 px-4 py-1 rounded-full">
                    <svg className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span className="text-sm font-medium">$12,450.00</span>
                  </div>
                  <div className="relative">
                    <button 
                      className="flex items-center space-x-1 focus:outline-none"
                      onClick={toggleProfileMenu}
                    >
                      <span className="hidden md:inline text-sm font-medium">{user}</span>
                      <div className="h-8 w-8 rounded-full bg-white bg-opacity-20 flex items-center justify-center">
                        <svg className="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                          <path fillRule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clipRule="evenodd" />
                        </svg>
                      </div>
                    </button>
                    
                    {isProfileMenuOpen && (
                      <div className="origin-top-right absolute right-0 mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-40">
                        <div className="py-1">
                          <Link 
                            to="/profile" 
                            onClick={closeAllMenus}
                            className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                          >
                            Mi perfil
                          </Link>
                          <button 
                            onClick={() => {
                              logout();
                              closeAllMenus();
                            }}
                            className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                          >
                            Cerrar sesión
                          </button>
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              ) : (
                <div className="flex items-center space-x-3">
                  <Link 
                    to="/login" 
                    onClick={closeAllMenus}
                    className={`px-4 py-2 rounded-md text-sm font-medium ${secondaryColor} ${secondaryText} hover:bg-opacity-80 transition-colors`}
                  >
                    Ingresar
                  </Link>
                  <Link 
                    to="/register" 
                    onClick={closeAllMenus}
                    className={`px-4 py-2 rounded-md text-sm font-medium ${textColor} ${accentColor} hover:bg-opacity-90 shadow-sm transition-colors`}
                  >
                    Crear cuenta
                  </Link>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>

      {/* Menú Lateral */}
      <div className={`fixed inset-y-0 left-0 transform ${isMenuOpen ? 'translate-x-0' : '-translate-x-full'} 
          w-64 z-40 transition duration-300 ease-in-out ${primaryColor} ${textColor} shadow-xl pt-16`}>
        <div className="flex flex-col h-full">
          <div className="flex-1 overflow-y-auto">
            <nav className="px-2 py-4 space-y-1">
              <Link 
                to="/dashboard" 
                onClick={closeAllMenus}
                className={`flex items-center px-4 py-3 rounded-lg mx-2 transition-colors ${isActive('/dashboard') ? 'bg-white bg-opacity-20' : 'hover:bg-white hover:bg-opacity-10'}`}
              >
                <svg className="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                </svg>
                Inicio
              </Link>
              
              <Link 
                to="/transactions" 
                onClick={closeAllMenus}
                className={`flex items-center px-4 py-3 rounded-lg mx-2 transition-colors ${isActive('/transactions') ? 'bg-white bg-opacity-20' : 'hover:bg-white hover:bg-opacity-10'}`}
              >
                <svg className="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
                </svg>
                Transacciones
              </Link>
              
              <Link 
                to="/exchange" 
                onClick={closeAllMenus}
                className={`flex items-center px-4 py-3 rounded-lg mx-2 transition-colors ${isActive('/exchange') ? 'bg-white bg-opacity-20' : 'hover:bg-white hover:bg-opacity-10'}`}
              >
                <svg className="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M9 19l3 3m0 0l3-3m-3 3V10" />
                </svg>
                Cambio
              </Link>
            </nav>
          </div>
          
          <div className="px-4 py-4 border-t border-white border-opacity-20">
            {isAuthenticated() ? (
              <button 
                onClick={() => {
                  logout();
                  closeAllMenus();
                }}
                className="flex items-center w-full px-4 py-3 rounded-lg hover:bg-white hover:bg-opacity-10 transition-colors"
              >
                <svg className="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
                Cerrar sesión
              </button>
            ) : (
              <div className="space-y-2">
                <Link 
                  to="/login" 
                  onClick={closeAllMenus}
                  className="flex items-center justify-center px-4 py-2 rounded-lg hover:bg-white hover:bg-opacity-10 transition-colors"
                >
                  Iniciar sesión
                </Link>
                <Link 
                  to="/register" 
                  onClick={closeAllMenus}
                  className={`flex items-center justify-center px-4 py-2 rounded-lg ${accentColor} hover:bg-opacity-90 transition-colors`}
                >
                  Crear cuenta
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Overlay para menú lateral */}
      {isMenuOpen && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-30 z-30 md:hidden"
          onClick={closeAllMenus}
        />
      )}
    </>
  );
};