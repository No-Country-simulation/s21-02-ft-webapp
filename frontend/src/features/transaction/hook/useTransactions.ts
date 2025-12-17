import { useEffect, useState } from 'react';
import { getTransactionsByAccount } from '../service/transactionService';
import { TransactionResponseDTO } from '../../../types/transaction/response';

export const useTransactions = (accountId: number) => {
  const [transactions, setTransactions] = useState<TransactionResponseDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!accountId) return;

    const fetchTransactions = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await getTransactionsByAccount(accountId);
        setTransactions(data);
      } catch (err: any) {
        setError(err.message || 'Error cargando transacciones');
      } finally {
        setLoading(false);
      }
    };

    fetchTransactions();
  }, [accountId]);

  return { transactions, loading, error };
};
