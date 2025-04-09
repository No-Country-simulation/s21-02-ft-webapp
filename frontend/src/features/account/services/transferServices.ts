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
  };
  
  export const validateDestination = async (destination: string): Promise<{
    isValid: boolean;
    accountName?: string;
    type?: 'CBU' | 'Alias';
  }> => {
    const response = await api.get(`/accounts/validate?destination=${destination}`);
    return response.data;
  };