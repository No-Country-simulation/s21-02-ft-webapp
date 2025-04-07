// src/features/account/hooks/useRegisterAccount.ts
import { useState } from 'react';
import { createAccount } from '../services/accountService';
import { useNavigate } from 'react-router-dom';

export const useRegisterAccount = () => {
  const [currency, setCurrency] = useState<string>('ARS');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      await createAccount(currency);
      navigate('/dashboard', { state: { accountCreated: true } });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error desconocido al crear la cuenta');
    } finally {
      setIsLoading(false);
    }
  };

  return {
    currency,
    setCurrency,
    isLoading,
    error,
    handleSubmit
  };
};