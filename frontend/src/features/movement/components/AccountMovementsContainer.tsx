import {useEffect} from "react";
import {useAccountStore} from "../../account/stores/useAccountStore";
import MovementListForm from "./MovementListForm";

export const AccountMovementsContainer = () => {
    const{
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

    if (loading) return <div className="p-4 text-center text-gray-700">Cargando movimientos...</div>;
    if (error) return <div className="p-4 text-center text-red-600">Error: {error}</div>;
    if (!accounts || accounts.length === 0) return <div className="p-4 text-center text-gray-700">No se encontraron movimientos</div>;

    return (
        <div className="w-full px-4 sm:px-6 lg:px-8">
            <div className="max-w-md mx-auto mb-6 flex flex-col items-center">
                <label htmlFor="accountSelect" className="block text-sm font-bold text-slate-700 mb-2 mt-4 text-left w-full">
                    Seleccione cuenta:
                </label>
                <select
                    id="accountSelect"
                    value={activeAccountId ?? ''}
                    onChange={e => setActiveAccountId(Number(e.target.value))}
                    className="w-full max-w-md p-2 border border-slate-200 rounded bg-white focus:outline-none focus:ring-1 focus:ring-slate-200"
                >

                    {accounts.map(acc => (
                        <option key={acc.accountId} value={acc.accountId}>
                            {acc.currency} — Saldo: {typeof acc.balance === 'number' ? acc.balance.toFixed(2) : '0.00'}
                        </option>
                    ))}
                </select>
            </div>

            {activeAccountId !== null && (
                <div className="max-w-7xl mx-auto">
                    <MovementListForm sourceAccountId={activeAccountId} />
                </div>
            )}
        </div>
    );
};  