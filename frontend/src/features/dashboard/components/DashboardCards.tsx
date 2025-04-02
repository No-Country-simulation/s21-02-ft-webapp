// src/components/dashboard/DashboardCards.tsx
import { FaHandHoldingUsd, FaExchangeAlt, FaQrcode } from 'react-icons/fa';

export const DashboardCards = () => {
  return (
    <div className="lg:flex gap-4 items-stretch">
      {/* Caja Grande */}
      <div className="bg-white md:p-2 p-6 rounded-lg border border-gray-200 mb-4 lg:mb-0 shadow-md lg:w-[35%]">
        <div className="flex justify-center items-center space-x-5 h-full">
          <div>
            <p className="text-gray-500">Saldo actual</p>
            <h2 className="text-4xl font-bold text-gray-600">50.365</h2>
            <p className="text-gray-500">25.365 $</p>
          </div>
          <img 
            src="https://www.emprenderconactitud.com/img/Wallet.png" 
            alt="wallet"
            className="h-24 md:h-20 w-38"
          />
        </div>
      </div>

      {/* Caja Blanca */}
      <div className="bg-white p-4 rounded-lg xs:mb-4 max-w-full shadow-md lg:w-[65%]">
        <div className="flex flex-wrap justify-between h-full">
          {/* Caja pequeña 1 */}
          <button className="flex-1 bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 m-2 hover:opacity-90 transition-opacity">
            <FaHandHoldingUsd className="text-white text-4xl" />
            <p className="text-white">Depositar</p>
          </button>

          {/* Caja pequeña 2 */}
          <button className="flex-1 bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 m-2 hover:opacity-90 transition-opacity">
            <FaExchangeAlt className="text-white text-4xl" />
            <p className="text-white">Transferir</p>
          </button>

          {/* Caja pequeña 3 */}
          <button className="flex-1 bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 m-2 hover:opacity-90 transition-opacity">
            <FaQrcode className="text-white text-4xl" />
            <p className="text-white">Canjear</p>
          </button>
        </div>
      </div>
    </div>
  );
};