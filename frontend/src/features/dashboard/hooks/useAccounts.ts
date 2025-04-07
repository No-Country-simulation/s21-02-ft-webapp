// src/features/Dashboard/hooks/useAccounts.ts
import { useEffect, useState } from 'react';
import { AccountResponse } from '../../../types/account/response';
import { fetchAccounts } from '../services/accountService';

export const useAccounts = () => {
  const [accounts, setAccounts] = useState<AccountResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadAccounts = async () => {
      try {
        const data = await fetchAccounts();
        setAccounts(data);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Unknown error');
      } finally {
        setLoading(false);
      }
    };

    loadAccounts();
  }, []);

  return { accounts, loading, error };
};