// src/features/Dashboard/Services/accountService.ts
import { useAuthStore } from '../../../features/auth/store/authStore';
import { api } from '../../../services/api';



export interface Account {
  accountId: number;
  cbu: string;
  alias: string;
  currency: string;
  balance: number;
  reservedBalance: number;
}

export const fetchAccounts = async (): Promise<Account[]> => {
  const token = useAuthStore.getState().token;
  
  if (!token) {
    throw new Error('No authentication token available');
  }

  const response = await fetch(`${api}/accounts`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    throw new Error(`Error fetching accounts: ${response.statusText}`);
  }

  return await response.json();
};