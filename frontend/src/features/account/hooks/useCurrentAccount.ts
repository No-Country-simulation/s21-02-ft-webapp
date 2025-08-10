import { useEffect } from 'react';
import { useAccountStore } from '../stores/useAccountStore';

export const useCurrentAccount = (accountId: number) => {
  const { fetchAccounts } = useAccountStore();
  const currentAccount = useAccountStore(state => state.getAccountById(accountId));
  const loading = useAccountStore(state => state.loading);
  const error = useAccountStore(state => state.error);
  
  useEffect(() => {
    fetchAccounts();
  }, [fetchAccounts]);
  
  return { currentAccount, loading, error };
};