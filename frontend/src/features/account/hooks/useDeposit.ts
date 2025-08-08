import { useState } from 'react';
import { makeDeposit } from '../services/depositService';
import { useAccountStore } from '../stores/useAccountStore';
import { DepositRequest } from '../../../types/account/request';

export const useDeposit = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { updateAccountBalance } = useAccountStore();

  const deposit = async (sourceAccountId: number, data: DepositRequest) => {
    setIsLoading(true);
    setError(null);
    try {
      const result = await makeDeposit(sourceAccountId, data);
      // Actualiza el balance en el store
      updateAccountBalance(sourceAccountId, result.amount + result.newBalance);
      return result;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error desconocido');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return { deposit, isLoading, error };
};