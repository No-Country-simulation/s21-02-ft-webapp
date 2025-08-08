import { api } from '../../../services/api';
import { DepositRequest } from '../../../types/account/request';
import { DepositResponse } from '../../../types/account/response';
import { useAuthStore } from '../../../features/auth/store/authStore';

export const makeDeposit = async (
  sourceAccountId: number,
  depositData: DepositRequest
): Promise<DepositResponse> => {
  const token = useAuthStore.getState().token;
  try {
    const response = await api.post(
      `/accounts/deposit?sourceAccountId=${sourceAccountId}`,
      depositData,
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
      throw new Error(error.response.data.message || 'Error al procesar el depósito');
    }
    throw new Error('Error de conexión con el servidor');
  }
};