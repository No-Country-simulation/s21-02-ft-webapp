import { api } from '../../../services/api'; 
import { TransactionResponseDTO } from '../../../types/transaction/response';

export const getTransactionsByAccount = async (accountId: number): Promise<TransactionResponseDTO[]> => {
  const response = await api.get(`/accounts/${accountId}/transactions`);
  return response.data;
};
