// src/features/transfer/services/transferService.ts
import { api } from '../../../services/api';
import { useAuthStore } from '../../../features/auth/store/authStore';
import {  TransferResponse } from '../../../types/account/response';
import {  TransferRequest } from '../../../types/account/request';

export const makeTransfer = async (
    sourceAccountId: number,
    data: TransferRequest
  ): Promise<TransferResponse> => {
    const token = useAuthStore.getState().token;
    
    if (!token) {
      throw new Error('No authentication token available');
    }
  
    try {
    const response = await api.post(
      `/accounts/transfer?sourceAccountId=${sourceAccountId}`,
      data,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );
    return response.data;
  } catch (error: any) {
    if (error.response) {
      // Extrae el mensaje del error 422 del backend
      if (error.response.status === 422) {
        throw new Error(error.response.data.message);
      }
      // Maneja otros códigos de error
      throw new Error(error.response.data.message || 'Error al procesar la transferencia');
    }
    throw new Error('Error de conexión con el servidor');
  }
};
  
  export const validateDestination = async (destination: string): Promise<{
    isValid: boolean;
    accountName?: string;
    type?: 'CBU' | 'Alias';
  }> => {
    const response = await api.get(`/accounts/validate?destination=${destination}`);
    return response.data;
  };

  // src/features/transfer/services/transferService.ts
export const checkAccountBalance = async (
  accountId: number,
  amount: number
): Promise<{
  hasEnoughBalance: boolean;
  currentBalance: number;
}> => {
  const token = useAuthStore.getState().token;
  
  if (!token) {
    throw new Error('No authentication token available');
  }

  const response = await api.get(
    `/accounts/${accountId}/check-balance?amount=${amount}`,
    {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }
  );

  return response.data;
};