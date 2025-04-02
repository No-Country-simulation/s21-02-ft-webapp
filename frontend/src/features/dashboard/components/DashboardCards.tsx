// src/components/dashboard/DashboardCards.tsx
import { FaHandHoldingUsd, FaExchangeAlt, FaQrcode } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import walletIcon from '../../../assets/icons/wallet.png';


export const DashboardCards = () => {
  return (
    <div className="lg:flex gap-4 items-stretch">
      {/* Caja Grande - Tarjeta de Saldo */}
      <div className="bg-white p-6 rounded-lg border border-gray-200 mb-4 lg:mb-0 shadow-md lg:w-[35%]">
        <div className="flex justify-between items-center h-full">
          <div>
            <p className="text-gray-500 text-sm md:text-base">Saldo actual</p>
            <h2 className="text-3xl md:text-4xl font-bold text-gray-600">50.365</h2>
            <p className="text-gray-500 text-sm md:text-base">25.365 $</p>
          </div>
          <img 
  src={walletIcon} 
  alt="wallet"
  className="h-20 w-20 md:h-24 md:w-24"
/>
        </div>
      </div>

      {/* Caja de Acciones */}
      <div className="bg-white p-4 rounded-lg shadow-md lg:w-[65%]">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          <Link
            to="/deposit"
            className="bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 hover:shadow-lg transition-shadow min-h-[120px]"
          >
            <FaHandHoldingUsd className="text-white text-3xl md:text-4xl" />
            <p className="text-white font-medium">Depositar</p>
          </Link>

          <Link
            to="/transfer"
            className="bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 hover:shadow-lg transition-shadow min-h-[120px]"
          >
            <FaExchangeAlt className="text-white text-3xl md:text-4xl" />
            <p className="text-white font-medium">Transferir</p>
          </Link>

          <Link
            to="/redeem"
            className="bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 hover:shadow-lg transition-shadow min-h-[120px]"
          >
            <FaQrcode className="text-white text-3xl md:text-4xl" />
            <p className="text-white font-medium">Canjear</p>
          </Link>
        </div>
      </div>
    </div>
  );
};