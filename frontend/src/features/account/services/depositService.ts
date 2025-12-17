import { api } from '../../../services/api';
import { DepositRequest } from '../../../types/account/request';
import { TransactionResponse } from '../../../types/account/response';
import { useAuthStore } from '../../../features/auth/store/authStore';

export const makeDeposit = async (
  sourceAccountId: number,
  depositData: DepositRequest
): Promise<TransactionResponse> => {
  const token = useAuthStore.getState().token;

  if (!token) {
    throw new Error('No authentication token available');
  }

  try {
    const response = await api.post(
      `/accounts/deposit?sourceAccountId=${sourceAccountId}`,
      depositData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      }
    );

    return response.data;
  } catch (error: any) {
    if (error.response) {
      if (error.response.status === 422) {
        throw new Error(error.response.data.message);
      }
      throw new Error(error.response.data.message || 'Error al procesar el depósito');
    }
    throw new Error('Error de conexión con el servidor');
  }
};
