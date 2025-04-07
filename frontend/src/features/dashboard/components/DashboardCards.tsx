// src/components/dashboard/DashboardCards.tsx
import { FaHandHoldingUsd, FaExchangeAlt, FaCopy, FaWallet, FaDollarSign, FaChevronDown, FaChevronUp, FaPlus } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import walletIcon from '../../../assets/icons/wallet.png';
import walletIconUSD from '../../../assets/icons/wallet.png';
import { FaRegCreditCard } from "react-icons/fa6";
import { useState } from 'react';
import { Card } from '../../../components/ui/Card';
import { useAccounts } from '../../../features/dashboard/hooks/useAccounts';
import { Spinner } from '../../../components/ui/Spinner';

export const DashboardCards = () => {
  const [showInfo, setShowInfo] = useState<'cbu' | 'alias'>('alias');
  const [isCopied, setIsCopied] = useState(false);
  const [copiedAccount, setCopiedAccount] = useState<string | null>(null);
  const [showOtherAccounts, setShowOtherAccounts] = useState(false);
  const { accounts, loading, error } = useAccounts();

  const arsAccount = accounts.find(acc => acc.currency === 'ARS');
  const otherAccounts = accounts.filter(acc => acc.currency !== 'ARS');

  const copyToClipboard = (text: string, currency: string) => {
    navigator.clipboard.writeText(text);
    setIsCopied(true);
    setCopiedAccount(currency);
    setTimeout(() => {
      setIsCopied(false);
      setCopiedAccount(null);
    }, 1500);
  };

  if (loading) {
    return <div className="flex justify-center items-center h-64"><Spinner size="lg" /></div>;
  }

  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 p-4 rounded-lg">
        Error al cargar los datos: {error}
      </div>
    );
  }

  const renderAccountSection = (account: typeof arsAccount, currency: string) => {
    if (!account) return null;

    const isARS = currency === 'ARS';
    const icon = isARS ? <FaWallet className="text-cyan-600 text-lg" /> : <FaDollarSign className="text-green-600 text-lg" />;
    const symbol = isARS ? '$' : 'US$';
    const locale = isARS ? 'es-AR' : 'en-US';

    return (
      <div className={`lg:flex gap-4 items-stretch mb-6 ${!isARS && !showOtherAccounts ? 'hidden' : ''}`}>
        {/* Card de Saldo */}
        <Card className="bg-white p-6 rounded-lg border border-gray-200 mb-4 lg:mb-0 shadow-md lg:w-[35%]">
          <div className="flex justify-between h-full">
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2">
                {icon}
                <p className="text-gray-500 text-sm md:text-base">Saldo actual ({currency})</p>
              </div>
              <h2 className={`text-3xl md:text-4xl font-bold ${isARS ? 'text-gray-600' : 'text-green-600'}`}>
                {symbol} {account.balance.toLocaleString(locale, {
                  minimumFractionDigits: 2,
                  maximumFractionDigits: 2
                })}
              </h2>

              {/* Información CBU/Alias */}
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
                    {account[showInfo]}
                  </p>
                  <button
                    onClick={() => copyToClipboard(account[showInfo], currency)}
                    className="text-cyan-600 hover:text-cyan-800 p-0.5"
                    aria-label="Copiar"
                  >
                    <FaCopy size={12} />
                  </button>
                  {isCopied && copiedAccount === currency && (
                    <span className="text-green-500 text-xs ml-1 whitespace-nowrap">
                      ¡Copiado!
                    </span>
                  )}
                </div>
              </div>
            </div>

            <div className="flex-shrink-0 ml-4 self-center">
              <img
                src={isARS ? walletIcon : walletIconUSD}
                alt={isARS ? 'Pesos' : 'Dólares'}
                className="h-16 w-16 md:h-20 md:w-20 object-contain"
              />
            </div>
          </div>
        </Card>

        {/* Acciones */}
        <div className="bg-white p-4 rounded-lg shadow-md lg:w-[65%]">
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
            <Link
              to={`/deposit?currency=${currency}`}
              className="bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 hover:shadow-lg transition-shadow min-h-[120px]"
            >
              <FaHandHoldingUsd className="text-white text-3xl md:text-4xl" />
              <p className="text-white font-medium">Depositar {currency}</p>
            </Link>

            <Link
              to={`/transfer?currency=${currency}`}
              className="bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 hover:shadow-lg transition-shadow min-h-[120px]"
            >
              <FaExchangeAlt className="text-white text-3xl md:text-4xl" />
              <p className="text-white font-medium">Transferir {currency}</p>
            </Link>

            <Link
              to="/redeem"
              className="bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 hover:shadow-lg transition-shadow min-h-[120px]"
            >
              <FaRegCreditCard className="text-white text-3xl md:text-4xl" />
              <p className="text-white font-medium">Tarjetas {currency}</p>
            </Link>
          </div>
        </div>
      </div>
    );
  };

  return (
    <div className="space-y-6">
      {/* Encabezado con botones */}
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold text-gray-700">Mis Cuentas</h2>
        <div className="flex gap-3">
          {otherAccounts.length > 0 && (
            <button
              onClick={() => setShowOtherAccounts(!showOtherAccounts)}
              className="flex items-center gap-2 text-cyan-600 hover:text-cyan-800 font-medium px-3 py-1 rounded-lg border border-cyan-200 hover:bg-cyan-50 transition-colors"
            >
              {showOtherAccounts ? (
                <>
                  <FaChevronUp /> Ocultar cuentas
                </>
              ) : (
                <>
                  <FaChevronDown /> Ver otras cuentas
                </>
              )}
            </button>
          )}
          
          <Link
            to="/accounts/create"
            className="flex items-center gap-2 bg-cyan-600 hover:bg-cyan-700 text-white font-medium px-3 py-1 rounded-lg transition-colors"
          >
            <FaPlus /> Crear cuenta
          </Link>
        </div>
      </div>

      {arsAccount && renderAccountSection(arsAccount, 'ARS')}

      {showOtherAccounts && otherAccounts.length > 0 && (
        <div className="mt-4 space-y-6">
          {otherAccounts.map(account => (
            renderAccountSection(account, account.currency)
          ))}
        </div>
      )}

      {!arsAccount && otherAccounts.length === 0 && (
        <div className="bg-yellow-50 border border-yellow-200 text-yellow-700 p-4 rounded-lg">
          No se encontraron cuentas disponibles
          <div className="mt-3">
            <Link
              to="/accounts/new"
              className="inline-flex items-center gap-2 bg-cyan-600 hover:bg-cyan-700 text-white font-medium px-3 py-1 rounded-lg transition-colors"
            >
              <FaPlus /> Crear mi primera cuenta
            </Link>
          </div>
        </div>
      )}
    </div>
  );
};