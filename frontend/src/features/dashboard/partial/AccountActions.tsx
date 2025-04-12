import { Link } from 'react-router-dom';
import { FaHandHoldingUsd, FaExchangeAlt } from 'react-icons/fa';
import { FaRegCreditCard } from 'react-icons/fa6';
import { useAccounts } from '../hooks/useAccounts';

interface Props {
  currency: string;
}

export const AccountActions = ({ currency }: Props) => {
  const { accounts } = useAccounts();

  const getAccountId = (currency: string) => {
    const account = accounts.find(acc => acc.currency === currency);
    return account?.accountId ?? 0;
  };

  const accountId = getAccountId(currency);

  const actions = [
    { to: `/account/deposit?sourceAccountId=${accountId}`, icon: <FaHandHoldingUsd />, label: 'Depositar' },
    { to: `/account/transfer?sourceAccountId=${accountId}`, icon: <FaExchangeAlt />, label: 'Transferir' },
    { to: '/redeem', icon: <FaRegCreditCard />, label: 'Tarjetas' },
  ];

  return (
    <div className="bg-white p-4 rounded-lg shadow-md lg:w-[65%]">
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
        {actions.map(({ to, icon, label }) => (
          <Link
            key={label}
            to={to}
            className="bg-gradient-to-r from-cyan-400 to-cyan-600 rounded-lg flex flex-col items-center justify-center p-4 space-y-2 border border-gray-200 hover:shadow-lg transition-shadow min-h-[120px]"
          >
            <span className="text-white text-3xl md:text-4xl">{icon}</span>
            <p className="text-white font-medium">{label} {currency}</p>
          </Link>
        ))}
      </div>
    </div>
  );
};
