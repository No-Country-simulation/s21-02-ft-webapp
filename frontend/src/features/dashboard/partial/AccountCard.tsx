import {
    FaCopy,
    FaWallet,
    FaDollarSign,
  } from 'react-icons/fa';
  import walletIcon from '../../../assets/icons/wallet.png';
  import { useState } from 'react';
  import { Card } from '../../../components/ui/Card';
  import { AccountActions } from './AccountActions';
  
  interface AccountCardProps {
    account: {
      currency: string;
      balance: number;
      alias: string;
      cbu: string;
    };
  }
  
  export const AccountCard = ({ account }: AccountCardProps) => {
    const [showInfo, setShowInfo] = useState<'cbu' | 'alias'>('alias');
    const [isCopied, setIsCopied] = useState(false);
  
    const { currency, balance, alias, cbu } = account;
    const isARS = currency === 'ARS';
    const symbol = isARS ? '$' : 'US$';
    const locale = isARS ? 'es-AR' : 'en-US';
  
    const copyToClipboard = (text: string) => {
      navigator.clipboard.writeText(text);
      setIsCopied(true);
      setTimeout(() => setIsCopied(false), 1500);
    };
  
    return (
      <div className="lg:flex gap-4 items-stretch mb-6">
        {/* Card de saldo */}
        <Card className="bg-white p-6 rounded-lg border border-gray-200 mb-4 lg:mb-0 shadow-md lg:w-[35%]">
          <div className="flex justify-between h-full">
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-2">
                {isARS ? <FaWallet className="text-cyan-600 text-lg" /> : <FaDollarSign className="text-green-600 text-lg" />}
                <p className="text-gray-500 text-sm md:text-base">Saldo actual ({currency})</p>
              </div>
              <h2 className={`text-3xl md:text-4xl font-bold ${isARS ? 'text-gray-600' : 'text-green-600'}`}>
                {symbol} {balance.toLocaleString(locale, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </h2>
  
              {/* CBU/Alias */}
              <div className="mt-3 flex items-center gap-2">
                <div className="flex space-x-1">
                  {['alias', 'cbu'].map(type => (
                    <button
                      key={type}
                      onClick={() => setShowInfo(type as 'alias' | 'cbu')}
                      className={`text-xs px-2 py-1 rounded ${showInfo === type ? 'bg-cyan-100 text-cyan-700' : 'bg-gray-100 text-gray-600'}`}
                    >
                      {type.toUpperCase()}
                    </button>
                  ))}
                </div>
                <div className="flex-1 flex items-center bg-gray-50 rounded px-2 py-1 gap-1">
                  <p className={`text-sm truncate ${showInfo === 'cbu' ? 'font-mono' : 'font-medium'}`}>
                    {showInfo === 'alias' ? alias : cbu}
                  </p>
                  <button onClick={() => copyToClipboard(showInfo === 'alias' ? alias : cbu)} className="text-cyan-600 hover:text-cyan-800 p-0.5">
                    <FaCopy size={12} />
                  </button>
                  {isCopied && <span className="text-green-500 text-xs ml-1 whitespace-nowrap">¡Copiado!</span>}
                </div>
              </div>
            </div>
            <div className="flex-shrink-0 ml-4 self-center">
              <img src={walletIcon} alt={currency} className="h-16 w-16 md:h-20 md:w-20 object-contain" />
            </div>
          </div>
        </Card>
  
        {/* Acciones */}
        <AccountActions currency={currency} />
      </div>
    );
  };
  