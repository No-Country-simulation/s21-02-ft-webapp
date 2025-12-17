import { useEffect } from 'react';
import { useAccountStore } from '../../account/stores/useAccountStore';
import TransactionListForm from './TransactionListForm';

export const AccountTransactionsContainer = () => {
    const {
        accounts,
        loading,
        error,
        activeAccountId,
        setActiveAccountId,
        fetchAccounts,
    } = useAccountStore();

    useEffect(() => {
        if (!accounts || accounts.length === 0) {
            fetchAccounts();
        }
    }, [accounts, fetchAccounts]);

    useEffect(() => {
        if (!accounts || accounts.length === 0) return;

        const existsActive = typeof activeAccountId === 'number' && accounts.some(a => a.accountId === activeAccountId);
        if (existsActive) return;

        const arsAccount = accounts.find(a =>
            typeof a.currency === 'string' && a.currency.trim().toUpperCase() === 'ARS'
        ) || accounts[0];

        setActiveAccountId(arsAccount.accountId);
    }, [accounts, activeAccountId, setActiveAccountId]);

    if (loading) return <div className="p-4 text-center text-gray-700">Cargando cuentas...</div>;
    if (error) return <div className="p-4 text-center text-red-600">{error}</div>;
    if (!accounts || accounts.length === 0) return <div className="p-4 text-center text-gray-500">No tienes cuentas</div>;

    return (
        <div className="w-full px-4 sm:px-6 lg:px-8">
            {/* Contenedor principal centrado que ahora envuelve ambos elementos para una alineación consistente */}
            <div className="max-w-4xl mx-auto">
                {/* Selector de cuenta */}
                {/* Se eliminó max-w-md del div contenedor. Se cambió items-center a items-start. */}
                <div className="mb-6 flex flex-col items-start w-full">
                    <label htmlFor="accountSelect" className="block text-sm font-bold text-slate-700 mb-2 mt-4 text-left w-full">
                        Seleccione cuenta:
                    </label>
                    <select
                        id="accountSelect"
                        value={activeAccountId ?? ''}
                        onChange={e => setActiveAccountId(Number(e.target.value))}
                        // Se eliminó max-w-md y se ajustó el padding a pl-4 py-2 para alinear con el contenido de la tabla
                        className="w-full pl-4 py-2 border border-slate-200 rounded bg-white focus:outline-none focus:ring-1 focus:ring-slate-200"
                    >
                        {accounts.map(acc => (
                            <option key={acc.accountId} value={acc.accountId}>
                                {acc.currency} — Saldo: {typeof acc.balance === 'number' ? acc.balance.toFixed(2) : '0.00'}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Lista de transacciones */}
                {activeAccountId !== null && (
                    // El TransactionListForm ahora estará dentro del mismo max-w-4xl mx-auto
                    <TransactionListForm sourceAccountId={activeAccountId} />
                )}
            </div>
        </div>
    );
};
