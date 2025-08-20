// src/components/dashboard/DashboardCards.tsx
import { FaHandHoldingUsd, FaExchangeAlt, FaCopy, FaWallet } from 'react-icons/fa';
import { LiaIdCardSolid } from "react-icons/lia";
import { Link } from 'react-router-dom';
import walletIcon from '../../../assets/icons/wallet.png';
import { useState } from 'react';
import { Card } from '../../../components/ui/Card';

export const DashboardCards = () => {
  const [showInfo, setShowInfo] = useState<'cbu' | 'alias'>('alias');
  const [isCopied, setIsCopied] = useState(false);

  const accountData = {
    cbu: '1234567890123456789012',
    alias: 'TU.ALIAS.BANCARIO'
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
    setIsCopied(true);
    setTimeout(() => setIsCopied(false), 1500);
  };

  return (
    <div className="lg:flex gap-4 items-stretch">
      {/* Caja Grande - Tarjeta de Saldo - Alto fijo exactamente como lo quieres */}
      <Card className="bg-white p-6 rounded-lg border border-gray-200 mb-4 lg:mb-0 shadow-md lg:w-[35%]">
        <div className="flex justify-between h-full">
          <div className="flex-1 min-w-0">
            <div className="flex items-center gap-2">
              <FaWallet className="text-cyan-600 text-lg" />
              <p className="text-gray-500 text-sm md:text-base">Saldo actual</p>
            </div>
            <h2 className="text-3xl md:text-4xl font-bold text-gray-600">$ 50.365</h2>

            {/* Información CBU/Alias con mensaje de copiado a la derecha */}
            <div className="mt-3 flex items-center gap-2">
              <div className="flex space-x-1">
                <button
                  onClick={() => setShowInfo('alias')}
                  className={`text-xs px-2 py-1 rounded ${showInfo === 'alias' ? 'bg-cyan-100 text-cyan-700' : 'bg-gray-100 text-gray-600'}`}
                >
                  Alias
                </button>
                <button
                  onClick={() => setShowInfo('cbu')}
                  className={`text-xs px-2 py-1 rounded ${showInfo === 'cbu' ? 'bg-cyan-100 text-cyan-700' : 'bg-gray-100 text-gray-600'}`}
                >
                  CBU
                </button>
              </div>

              <div className="flex-1 min-w-0 flex items-center bg-gray-50 rounded px-2 py-1 gap-1">
                <p className={`text-sm truncate ${showInfo === 'cbu' ? 'font-mono' : 'font-medium'}`}>
                  {accountData[showInfo]}
                </p>
                <button
                  onClick={() => copyToClipboard(accountData[showInfo])}
                  className="text-cyan-600 hover:text-cyan-800 p-0.5"
                  aria-label="Copiar"
                >
                  <FaCopy size={12} />
                </button>
                {isCopied && (
                  <span className="text-green-500 text-xs ml-1 whitespace-nowrap">
                    ¡Copiado!
                  </span>
                )}
              </div>
            </div>
          </div>

          {/* Icono de wallet ajustado para no desbordar */}
          <div className="flex-shrink-0 ml-4 self-center">
            <img
              src={walletIcon}
              alt="wallet"
              className="h-16 w-16 md:h-20 md:w-20 object-contain"
            />
          </div>
        </div>
      </Card>

      {/* Caja de Acciones (se mantiene igual) */}
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
            <LiaIdCardSolid className="text-white text-3xl md:text-4xl" />
            <p className="text-white font-medium">Mis Tarjetas</p>
          </Link>
        </div>
      </div>
    </div>
  );
};