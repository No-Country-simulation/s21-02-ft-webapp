// src/features/transfer/hooks/useTransfer.ts
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { makeTransfer, validateDestination } from '../services/transferServices';
import { TransferRequest } from '../../../types/account/request';
import { TransferResponse } from '../../../types/account/response';
import { useAuthStore } from '../../../features/auth/store/authStore';

// ✅ Definimos el tipo explícito del estado
type TransferState = {
  destinationIdentifier: string;
  amount: string;
  reason: string;
  currency: string;
  isLoading: boolean;
  error: string | null;
  success: boolean;
  transactionDetails: TransferResponse | null;
  destinationAccountName: string;
};

export const useTransfer = (sourceAccountId: number) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { user } = useAuthStore();
  const queryParams = new URLSearchParams(location.search);

  const [state, setState] = useState<TransferState>({
    destinationIdentifier: '',
    amount: '',
    reason: '',
    currency: queryParams.get('currency') || 'ARS',
    isLoading: false,
    error: null,
    success: false,
    transactionDetails: null,
    destinationAccountName: ''
  });

  const handleChange = (field: keyof TransferState, value: string) => {
    setState(prev => ({ ...prev, [field]: value }));
  };

  const validateInputs = (): string | null => {
    if (!state.destinationIdentifier.trim()) {
      return 'Ingrese un CBU o Alias válido';
    }

    if (state.destinationIdentifier.length < 5) {
      return 'El identificador debe tener al menos 5 caracteres';
    }

    const amountValue = parseFloat(state.amount);
    if (isNaN(amountValue)) {
      return 'Ingrese un monto válido';
    }

    if (amountValue <= 0) {
      return 'El monto debe ser mayor a cero';
    }

    if (!state.reason.trim()) {
      return 'Ingrese un motivo para la transferencia';
    }

    return null;
  };

  const submitTransfer = async () => {
    const validationError = validateInputs();
    if (validationError) {
      setState(prev => ({ ...prev, error: validationError }));
      return;
    }

    setState(prev => ({ ...prev, isLoading: true, error: null }));

    try {
      const validation = await validateDestination(state.destinationIdentifier);
      if (!validation.isValid) {
        throw new Error('El CBU o Alias no existe');
      }

      // ✅ Aseguramos que siempre sea string
      setState(prev => ({ 
        ...prev, 
        destinationAccountName: validation.accountName ?? '' 
      }));

      const transferData: TransferRequest = {
        destinationIdentifier: state.destinationIdentifier,
        amount: parseFloat(state.amount),
        reason: state.reason
      };

      const result = await makeTransfer(sourceAccountId, transferData);

      setState(prev => ({
        ...prev,
        success: true,
        isLoading: false,
        transactionDetails: result
      }));

      setTimeout(() => navigate('/dashboard'), 5000);
    } catch (err) {
      setState(prev => ({
        ...prev,
        error: err instanceof Error ? err.message : 'Error desconocido',
        isLoading: false
      }));
    }
  };

  return {
    ...state,
    handleChange,
    submitTransfer
  };
};
