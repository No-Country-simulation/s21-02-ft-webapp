import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  FaHome, FaWallet, FaExchangeAlt, FaMoneyBillWave, FaHandHoldingUsd, 
  FaCreditCard, FaClipboardList, FaChartLine, FaUser, 
  FaSignOutAlt, FaBell, FaBars,FaEnvelope, FaPhone, FaMapMarkerAlt
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
            <LoginForm />
        </div>
        <div className="py-3 text-center text-xs text-gray-500 bg-white border-t">
          © {new Date().getFullYear()} POC WCS. Todos los derechos reservados.
        </div>
      </div>
    );
  }

  return (
    <div className="flex flex-col max-h-screen">
      {/* Fixed Navbar */}
      <nav className="bg-white border-b border-gray-300 fixed w-full h-16 z-30">
        <div className="flex justify-between items-center h-full px-9">
          <button onClick={toggleSidebar} className="focus:outline-none">
            <FaBars className="text-cyan-500 text-lg" />
          </button>

          <div className="ml-1">
            <img src="/src/assets/icons/logo.svg" alt="logo" className="h-12" />
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

      {/* Sidebar */}
      <div className={`${isOpen ? 'block' : 'hidden'} lg:block bg-white w-64 fixed top-16 left-0 bottom-0 z-20 border-r border-gray-200 overflow-y-auto`}>
        <div className="p-4 space-y-4">
          
          {/* Menú de navegación */}
          <Link to="/dashboard" className="relative px-4 py-3 flex items-center space-x-4 rounded-lg text-white bg-gradient-to-r from-sky-600 to-cyan-400"
            onClick={() => setIsOpen(false)}>
            <FaHome className="text-white" />
            <span className="-mr-1 font-medium">Inicio</span>
          </Link>

          <Link to="/wallet" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaWallet />
            <span>Tu dinero</span>
          </Link>

          <Link to="/transfer" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaExchangeAlt />
            <span>Transferir</span>
          </Link>

          <Link to="/deposit" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaHandHoldingUsd />
            <span>Depositar</span>
          </Link>

          <Link to="/transactions" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaMoneyBillWave />
            <span>Transacciones</span>
          </Link>

          <Link to="/movements" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaClipboardList />
            <span>Movimientos</span>
          </Link>

          <Link to="/reservations" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaChartLine />
            <span>Reservas</span>
          </Link>

          <Link to="/cards" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaCreditCard />
            <span>Tarjetas</span>
          </Link>

          <Link to="/quote" className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100"
            onClick={() => setIsOpen(false)}>
            <FaChartLine />
            <span>Cotización</span>
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
              </div>
              </div>

          <button onClick={handleLogout} className="px-4 py-3 flex items-center space-x-4 rounded-md text-gray-600 hover:bg-gray-100 w-full text-left mt-4">
            <FaSignOutAlt />
            <span>Cerrar sesión</span>
          </button>
          
        </div>
      </div>
    </div>
  );
};
