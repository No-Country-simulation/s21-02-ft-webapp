// src/features/Dashboard/Services/accountService.ts
import { api } from '../../../services/api';
import { AccountResponse} from '../../../types/account/response';

export const fetchAccounts = async (): Promise<AccountResponse[]> => {
    const response = await api.get('/accounts'); 
    return response.data || [];
};