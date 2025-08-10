import { useState } from 'react';
import { makeDeposit } from '../services/depositService';
import { DepositRequest } from '../../../types/account/request';
import { TransactionResponse } from '../../../types/account/response';
import { useAccountStore } from '../../account/stores/useAccountStore';

type DepositState = {
  cardNumber: string;
  amount: string;
  isLoading: boolean;
  error: string | null;
  success: boolean;
  transactionDetails: TransactionResponse | null;
};

export const useDeposit = (sourceAccountId: number) => {
  const { updateAccountBalance, accounts } = useAccountStore();
  const [state, setState] = useState<DepositState>({
    cardNumber: '',
    amount: '',
    isLoading: false,
    error: null,
    success: false,
    transactionDetails: null,
  });

  // Actualiza campos de formulario
  const handleChange = (field: keyof DepositState, value: string) => {
    setState(prev => ({ ...prev, [field]: value }));
  };

  // Validación de inputs
  const validateInputs = (): string | null => {
    if (!state.cardNumber.trim()) {
      return 'Seleccione un número de tarjeta válido';
    }
    const amountValue = parseFloat(state.amount);
    if (isNaN(amountValue)) {
      return 'Ingrese un monto válido';
    }
    if (amountValue <= 0) {
      return 'El monto debe ser mayor a cero';
    }
    return null;
  };

  // Procesar depósito
  const submitDeposit = async () => {
    const validationError = validateInputs();
    if (validationError) {
      setState(prev => ({ ...prev, error: validationError }));
      return;
    }

    setState(prev => ({ ...prev, isLoading: true, error: null }));

    try {
      // Construir payload
      const depositData: DepositRequest = {
        cardNumber: state.cardNumber,
        amount: parseFloat(state.amount),
      };

      // Hacer petición
      const result = await makeDeposit(sourceAccountId, depositData);

      // Obtener saldo actual de la cuenta en el store
      const account = accounts.find(acc => acc.accountId === sourceAccountId);
      if (account) {
        updateAccountBalance(
          sourceAccountId,
          account.balance + parseFloat(state.amount)
        );
      }

      setState(prev => ({
        ...prev,
        success: true,
        isLoading: false,
        transactionDetails: result,
      }));
    } catch (err: any) {
      setState(prev => ({
        ...prev,
        error: err.message || 'Error realizando el depósito',
        isLoading: false,
      }));
    }
  };

  return {
    ...state,
    handleChange,
    submitDeposit,
  };
};
