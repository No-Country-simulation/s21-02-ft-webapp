import { useState, useMemo } from 'react';
import { useTransactions } from '../hook/useTransactions';
import { Card } from '../../../components/ui/Card';

type Props = {
    sourceAccountId: number;
};

const TransactionListForm = ({ sourceAccountId }: Props) => {
    const { transactions, loading, error } = useTransactions(sourceAccountId);

    const [searchTerm, setSearchTerm] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const pageSize = 10;

    const filteredTransactions = useMemo(() => {
        if (!searchTerm.trim()) return transactions;
        const lowered = searchTerm.toLowerCase();
        return transactions.filter(tx =>
            tx.transactionId.toString().includes(searchTerm) ||
            (tx.transactionType && tx.transactionType.toLowerCase().includes(lowered)) 
        );
    }, [transactions, searchTerm]);

    const pagedTransactions = useMemo(() => {
        const start = (currentPage - 1) * pageSize;
        return filteredTransactions.slice(start, start + pageSize);
    }, [filteredTransactions, currentPage]);

    const totalPages = Math.max(1, Math.ceil(filteredTransactions.length / pageSize));

    const goToPage = (page: number) => {
        if (page < 1 || page > totalPages) return;
        setCurrentPage(page);
    };

    if (loading) return <div className="p-4 text-center text-gray-700">Cargando transacciones...</div>;
    if (error) return <div className="p-4 text-center text-red-600">Error: {error}</div>;
    if (transactions.length === 0) return <div className="p-4 text-center text-gray-500">No hay transacciones para esta cuenta.</div>;

    return (
        <div className="flex flex-col items-center">
            <Card className="w-full max-w-full">
                {/* Header */}
                <div className="w-full flex justify-between items-center mb-3 mt-1 px-3">
                    <div>
                        <h3 className="text-lg font-semibold text-slate-800">Transacciones</h3>
                        <p className="text-slate-500">Resumen de las transacciones de la cuenta seleccionada.</p>
                    </div>

                    <div className="ml-3">
                        <div className="w-full max-w-sm min-w-[200px] relative">
                            <input
                                type="text"
                                value={searchTerm}
                                onChange={e => { setSearchTerm(e.target.value); setCurrentPage(1); }}
                                className="bg-white w-full pr-11 h-10 pl-3 py-2 placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded transition duration-200 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm"
                                placeholder="Buscar transacción..."
                            />
                            <button
                                className="absolute h-8 w-8 right-1 top-1 my-auto px-2 flex items-center bg-white rounded"
                                type="button"
                                onClick={() => setSearchTerm('')}
                                aria-label="Limpiar búsqueda"
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={2} stroke="currentColor" className="w-5 h-5 text-slate-400">
                                    <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>

                {/* Tabla con scroll horizontal */}
                <div className="overflow-x-auto w-full">
                    <table className="min-w-[1000px] w-full text-left table-auto border-collapse">
                        <thead>
                            <tr className="bg-slate-50">
                                <th className="p-4 border-b border-slate-200 text-sm text-slate-500">Fecha</th>
                                <th className="p-4 border-b border-slate-200 text-sm text-slate-500">Tipo</th>
                                <th className="p-4 border-b border-slate-200 text-sm text-slate-500">Monto</th>
                                <th className="p-4 border-b border-slate-200 text-sm text-slate-500">Destinatario</th>
                                <th className="p-4 border-b border-slate-200 text-sm text-slate-500">Descripción</th>
                            </tr>
                        </thead>

                        <tbody>
                            {pagedTransactions.map(tx => {
                                const isPositive = tx.amount > 0;
                                const amountText = isPositive ? `+${tx.amount.toFixed(2)}` : tx.amount.toFixed(2);

                                // flechitas neutras (gris) que indican entrada/salida (no coloreadas)
                                const arrow = tx.amount === 0 ? null : (isPositive ? (
                                    <svg className="w-4 h-4 text-slate-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" aria-hidden>
                                        <path d="M12 18V6" />
                                        <path d="M5 11l7-7 7 7" />
                                    </svg>
                                ) : (
                                    <svg className="w-4 h-4 text-slate-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" aria-hidden>
                                        <path d="M12 6v12" />
                                        <path d="M19 13l-7 7-7-7" />
                                    </svg>
                                ));

                                const typeLabel = tx.transactionType ? String(tx.transactionType) : '-';

                                return (
                                    <tr key={tx.transactionId} className="hover:bg-slate-50 border-b border-slate-200">
                                        <td className="p-4 py-5 text-sm text-slate-500">
                                            {new Date(tx.transactionDate).toLocaleString(undefined, {
                                                day: '2-digit',
                                                month: '2-digit',
                                                year: '2-digit',
                                                hour: '2-digit',
                                                minute: '2-digit',
                                                hour12: false
                                            })}
                                        </td>

                                        <td className="p-4 py-5 text-sm text-slate-500">{typeLabel}</td>
                                        <td className="p-4 py-5 text-sm text-slate-700">
                                            <div className="flex items-center space-x-2">
                                                <span className="text-sm text-slate-700 font-semibold">{amountText}</span>
                                                {arrow}
                                            </div>
                                        </td>
                                        <td className="p-4 py-5 text-sm text-slate-500">{tx.destinationAccount ?? '-'}</td>
                                        <td className="p-4 py-5 text-sm text-slate-500 whitespace-normal">{tx.reason || '-'}</td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>

                {/* Paginación */}
                <div className="flex justify-between items-center px-4 py-3 mt-3">
                    <div className="text-sm text-slate-500">
                        Mostrando <b>{(currentPage - 1) * pageSize + 1}</b>-
                        <b>{Math.min(currentPage * pageSize, filteredTransactions.length)}</b> de <b>{filteredTransactions.length}</b>
                    </div>

                    <div className="flex items-center space-x-2">
                        <button
                            onClick={() => goToPage(currentPage - 1)}
                            disabled={currentPage === 1}
                            className="px-3 py-1 text-sm text-slate-500 bg-white border border-slate-200 rounded disabled:opacity-50"
                        >
                            Prev
                        </button>

                        {[...Array(totalPages)].map((_, idx) => {
                            const pageNum = idx + 1;
                            return (
                                <button
                                    key={pageNum}
                                    onClick={() => goToPage(pageNum)}
                                    className={`px-3 py-1 text-sm rounded ${currentPage === pageNum ? 'bg-slate-800 text-white' : 'bg-white text-slate-500 border border-slate-200'}`}
                                >
                                    {pageNum}
                                </button>
                            );
                        })}

                        <button
                            onClick={() => goToPage(currentPage + 1)}
                            disabled={currentPage === totalPages}
                            className="px-3 py-1 text-sm text-slate-500 bg-white border border-slate-200 rounded disabled:opacity-50"
                        >
                            Next
                        </button>
                    </div>
                </div>
            </Card>
        </div>
    );
};

export default TransactionListForm;
