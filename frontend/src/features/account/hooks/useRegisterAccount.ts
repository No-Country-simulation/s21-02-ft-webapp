// src/features/account/hooks/useRegisterAccount.ts
import { useState, useEffect } from 'react';
import { createAccount, getCurrencyTypeAccounts, getAccounts } from '../services/accountService';
import { useNavigate } from 'react-router-dom';

interface CurrencyOption {
  value: string;
  label: string;
}

export const useRegisterAccount = () => {
  const [currency, setCurrency] = useState<string | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [currencyOptions, setCurrencyOptions] = useState<CurrencyOption[]>([]);
  const [isLoadingCurrencies, setIsLoadingCurrencies] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const loadData = async () => {
      try {
        const allCurrencies = await getCurrencyTypeAccounts();
        const userAccounts = await getAccounts();
        const userCurrencies = userAccounts.map(account => account.currency);

        const missingCurrencies = allCurrencies.filter(
          currency => !userCurrencies.includes(currency)
        );

        if (missingCurrencies.length === 0) {
          setError('Ya has creado todos los tipos de cuenta disponibles');
          setCurrencyOptions([]);
          return;
        }

        const options = missingCurrencies.map((code: string) => ({
          value: code,
          label: formatCurrencyLabel(code)
        }));

        setCurrencyOptions(options);

        // Seleccionar por defecto la primera
        setCurrency(missingCurrencies[0]);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Error al cargar los datos');
        setCurrencyOptions([
          { value: 'ARS', label: 'Pesos Argentinos ($)' },
          { value: 'USD', label: 'Dólares Estadounidenses (US$)' }
        ]);
        setCurrency('ARS'); // Opcional
      } finally {
        setIsLoadingCurrencies(false);
      }
    };

    loadData();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!currency) {
      setError('Por favor selecciona un tipo de cuenta');
      return;
    }

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
    currencyOptions,
    isLoading: isLoading || isLoadingCurrencies,
    error,
    handleSubmit
  };
};

function formatCurrencyLabel(code: string): string {
  const currencyLabels: Record<string, string> = {
    'ARS': 'Pesos Argentinos ($)',
    'USD': 'Dólares Estadounidenses (US$)'
  };
  return currencyLabels[code] || `${code} (${code})`;
}
