// src/components/layout/Sidebar.tsx
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  FaHome, FaWallet, FaExchangeAlt, FaUser, 
  FaSignOutAlt, FaBell, FaBars, FaSearch, 
  FaHandHoldingUsd, FaQrcode, FaEnvelope, 
  FaPhone, FaMapMarkerAlt, FaInfoCircle,
  FaQuestionCircle, FaShieldAlt, FaLock,
  FaFacebook, FaTwitter, FaInstagram, FaLinkedin
} from 'react-icons/fa';
import { useAuthStore } from '../../features/auth/store/authStore';
import { LoginForm } from '../../features/auth/components/LoginForms';

export const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { isAuthenticated, user, logout } = useAuthStore();
  const navigate = useNavigate();

  const toggleSidebar = () => setIsOpen(!isOpen);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (!isAuthenticated()) {
    return (
      <div className="min-h-screen flex flex-col bg-gray-100">
        <div className="flex-grow flex items-center justify-center">
          <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
            <LoginForm />
            <div className="mt-4 text-center">
              <p className="text-gray-600">¿No tienes cuenta? <Link to="/register" className="text-cyan-600 hover:underline">Regístrate</Link></p>
            </div>
          </div>
        </div>
        <div className="py-3 text-center text-xs text-gray-500 bg-white border-t">
          © {new Date().getFullYear()} POC WCS. Todos los derechos reservados.
        </div>
      </div>
    );
  }

  return (
    <div className="flex flex-col min-h-screen">
      {/* Fixed Navbar - Height: 4rem (64px) */}
      <nav className="bg-white border-b border-gray-300 fixed w-full h-16 z-30">
        <div className="flex justify-between items-center h-full px-9">
          <button onClick={toggleSidebar} className="focus:outline-none">
            <FaBars className="text-cyan-500 text-lg" />
          </button>

          <div className="ml-1">
            <img 
              src="https://www.emprenderconactitud.com/img/POC%20WCS%20(1).png" 
              alt="logo" 
              className="h-12"
            />
          </div>

          <div className="flex items-center space-x-4">
            <span className="text-sm font-medium text-gray-700">{user}</span>
            <button className="focus:outline-none">
              <FaBell className="text-cyan-500 text-lg" />
            </button>
            <Link to="/profile" className="focus:outline-none">
              <FaUser className="text-cyan-500 text-lg" />
            </Link>
          </div>
        </div>
      </nav>

      {/* Fixed Sidebar - Starts right below navbar */}
      <div className={`${isOpen ? 'block' : 'hidden'} lg:block bg-white w-64 fixed top-16 left-0 bottom-0 z-20 border-r border-gray-200 overflow-y-auto`}>
        <div className="p-4 space-y-4">
          {/* Main Navigation Links */}
          <Link 
            to="/dashboard" 
            className="relative px-4 py-3 flex items-center space-x-4 rounded-lg text-white bg-gradient-to-r from-sky-600 to-cyan-400"
            onClick={() => setIsOpen(false)}
          >
            <FaHome className="text-white" />
            <span className="-mr-1 font-medium">Inicio</span>
          </Link>

          <Link 
            to="/wallet" 
            className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}
          >
            <FaWallet />
            <span>Billetera</span>
          </Link>
          
          <Link 
            to="/transactions" 
            className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}
          >
            <FaExchangeAlt />
            <span>Transacciones</span>
          </Link>
          
          <Link 
            to="/account" 
            className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}
          >
            <FaUser />
            <span>Mi cuenta</span>
          </Link>

          {/* Information Section */}
          <div className="pt-4 border-t border-gray-200">
            <h3 className="px-4 py-2 text-sm font-semibold text-gray-500 uppercase tracking-wider">
              Información
            </h3>
            
            <div className="space-y-3">
              {/* Contact Info */}
              <div className="px-4 py-2">
                <h4 className="text-xs font-semibold text-gray-400 mb-1">Contacto</h4>
                <ul className="space-y-1 text-gray-600">
                  <li className="flex items-center">
                    <FaEnvelope className="mr-2 text-gray-400 text-xs" />
                    <span className="text-sm">contacto@empresa.com</span>
                  </li>
                  <li className="flex items-center">
                    <FaPhone className="mr-2 text-gray-400 text-xs" />
                    <span className="text-sm">+1 234 567 890</span>
                  </li>
                  <li className="flex items-center">
                    <FaMapMarkerAlt className="mr-2 text-gray-400 text-xs" />
                    <span className="text-sm">Ciudad, País</span>
                  </li>
                </ul>
              </div>

              {/* Quick Links */}
              <div className="px-4 py-2">
                <h4 className="text-xs font-semibold text-gray-400 mb-1">Enlaces</h4>
                <ul className="space-y-1">
                  <li>
                    <Link 
                      to="/faq" 
                      className="flex items-center text-gray-600 hover:text-cyan-500 text-sm"
                      onClick={() => setIsOpen(false)}
                    >
                      <FaQuestionCircle className="mr-2 text-gray-400 text-xs" />
                      Preguntas Frecuentes
                    </Link>
                  </li>
                  <li>
                    <Link 
                      to="/terms" 
                      className="flex items-center text-gray-600 hover:text-cyan-500 text-sm"
                      onClick={() => setIsOpen(false)}
                    >
                      <FaShieldAlt className="mr-2 text-gray-400 text-xs" />
                      Términos y Condiciones
                    </Link>
                  </li>
                  <li>
                    <Link 
                      to="/privacy" 
                      className="flex items-center text-gray-600 hover:text-cyan-500 text-sm"
                      onClick={() => setIsOpen(false)}
                    >
                      <FaLock className="mr-2 text-gray-400 text-xs" />
                      Privacidad
                    </Link>
                  </li>
                </ul>
              </div>

              {/* Social Media */}
              <div className="px-4 py-2">
                <h4 className="text-xs font-semibold text-gray-400 mb-1">Redes Sociales</h4>
                <div className="flex space-x-3">
                  <a href="#" className="text-gray-400 hover:text-cyan-500">
                    <FaFacebook size={14} />
                  </a>
                  <a href="#" className="text-gray-400 hover:text-cyan-500">
                    <FaTwitter size={14} />
                  </a>
                  <a href="#" className="text-gray-400 hover:text-cyan-500">
                    <FaInstagram size={14} />
                  </a>
                  <a href="#" className="text-gray-400 hover:text-cyan-500">
                    <FaLinkedin size={14} />
                  </a>
                </div>
              </div>
            </div>
          </div>
          
          <button 
            onClick={handleLogout}
            className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100 w-full text-left mt-4"
          >
            <FaSignOutAlt />
            <span>Cerrar sesión</span>
          </button>
        </div>
      </div>

      {/* Main Content - Adjusted for fixed navbar and sidebar */}
      <div className={`flex-grow pt-25 ${isOpen ? 'lg:ml-64' : ''}`}>
        <div className="mx-4 lg:mx-6">
          {/* Scrollable Content */}
          <div className="pb-20">
            {/* Balance Cards */}
            <div className="lg:flex gap-4 items-stretch">
              <div className="bg-white md:p-2 p-6 rounded-lg border border-gray-200 mb-4 lg:mb-0 shadow-md lg:w-[35%]">
                <div className="flex justify-center items-center space-x-5 h-full">
                  <div>
                    <p className="text-gray-600">Saldo actual</p>
                    <h2 className="text-4xl font-bold text-gray-600">50.365</h2>
                    <p className="text-gray-600">25.365 $</p>
                  </div>
                  <img 
                    src="https://www.emprenderconactitud.com/img/Wallet.png" 
                    alt="wallet"
                    className="h-24 md:h-20 w-38"
                  />
                </div>
              </div>

              <div className="bg-white p-4 rounded-lg xs:mb-4 max-w-full shadow-md lg:w-[65%]">
                <div className="flex flex-wrap justify-between h-full">
                  <Link
                    to="/deposit"
                    className="flex-1 bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 m-2 hover:shadow-lg transition-shadow"
                  >
                    <FaHandHoldingUsd className="text-white text-4xl" />
                    <p className="text-white">Depositar</p>
                  </Link>

                  <Link
                    to="/transfer"
                    className="flex-1 bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 m-2 hover:shadow-lg transition-shadow"
                  >
                    <FaExchangeAlt className="text-white text-4xl" />
                    <p className="text-white">Transferir</p>
                  </Link>

                  <Link
                    to="/redeem"
                    className="flex-1 bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 m-2 hover:shadow-lg transition-shadow"
                  >
                    <FaQrcode className="text-white text-4xl" />
                    <p className="text-white">Canjear</p>
                  </Link>
                </div>
              </div>
            </div>

            {/* Transactions Table */}
            <div className="bg-white rounded-lg p-4 shadow-md my-4">
              <table className="table-auto w-full">
               
              </table>
            </div>
          </div>
        </div>
      </div>

      {/* Fixed Footer */}
      <footer className="bg-white border-t border-gray-200 py-2 fixed bottom-0 left-0 right-0 z-10">
        <div className="text-center text-xs text-gray-500">
          © {new Date().getFullYear()} POC WCS. Todos los derechos reservados.
        </div>
      </footer>

      {/* Mobile Overlay - Adjusted z-index */}
      {isOpen && (
        <div 
          className="fixed inset-0 bg-black bg-opacity-50 z-15 lg:hidden"
          onClick={toggleSidebar}
        />
      )}
    </div>
  );
};