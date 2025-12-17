import { FaChevronDown, FaChevronUp, FaPlus } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { useAccountStore } from '../../../features/account/stores/useAccountStore';
import { useState } from 'react';
import { Spinner } from '../../../components/ui/Spinner';
import { AccountCard } from '../partial/AccountCard';
import { useEffect } from 'react';

export const DashboardCards = () => {
  const [showOtherAccounts, setShowOtherAccounts] = useState(false);
  const { accounts, loading, error, fetchAccounts } = useAccountStore();

  useEffect(() => {
    fetchAccounts();
  }, [fetchAccounts]);

  const arsAccount = accounts.find(acc => acc.currency === 'ARS');
  const otherAccounts = accounts.filter(acc => acc.currency !== 'ARS');

  if (loading) return <div className="flex justify-center items-center h-64"><Spinner size="lg" /></div>;

  if (error && accounts.length !== 0) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 p-4 rounded-lg">
        Error al cargar los datos: {error}
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold text-gray-700">Mis Cuentas</h2>
        <div className="flex gap-3">
          {otherAccounts.length > 0 && (
            <button
              onClick={() => setShowOtherAccounts(prev => !prev)}
              className="flex items-center gap-2 text-cyan-600 hover:text-cyan-800 font-medium px-3 py-1 rounded-lg border border-cyan-200 hover:bg-cyan-50 transition-colors"
            >
              {showOtherAccounts ? <><FaChevronUp /> Ocultar</> : <><FaChevronDown /> Ver otras cuentas</>}
            </button>
          )}
          {accounts.length < 2 && accounts.length > 0 && (
            <Link
              to="/account/create"
              className="flex items-center gap-2 bg-cyan-600 hover:bg-cyan-700 text-white font-medium px-3 py-1 rounded-lg transition-colors"
            >
              <FaPlus /> Crear cuenta
            </Link>
          )}
        </div>
      </div>

      {arsAccount && <AccountCard account={arsAccount} />}
      {showOtherAccounts && otherAccounts.map(acc => (
        <AccountCard key={acc.currency} account={acc} />
      ))}

      {!arsAccount && otherAccounts.length === 0 && (
        <div className="bg-yellow-50 border border-yellow-200 text-yellow-700 p-4 rounded-lg">
          No se encontraron cuentas disponibles
          <div className="mt-3">
            <Link
              to="/account/create"
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