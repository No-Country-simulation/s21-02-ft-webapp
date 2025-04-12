import { create } from 'zustand';
import { api } from '../../../services/api';
import { useAuthStore } from '../../../features/auth/store/authStore';
import { AccountResponse } from '../../../types/account/response';

interface AccountState {
  accounts: AccountResponse[];
  loading: boolean;
  error: string | null;
  fetchAccounts: () => Promise<void>;
  updateAccountBalance: (accountId: number, newBalance: number) => void;
}

export const useAccountStore = create<AccountState>((set) => ({
  accounts: [],
  loading: false,
  error: null,

  fetchAccounts: async () => {
    set({ loading: true, error: null });
    try {
      const token = useAuthStore.getState().token;
      const response = await api.get<AccountResponse[]>('/accounts', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      set({ accounts: response.data, loading: false });
    } catch (error) {
      set({
        error: error instanceof Error ? error.message : 'Error al cargar cuentas',
        loading: false,
      });
    }
  },

  updateAccountBalance: (accountId: number, newBalance: number) => {
    set((state) => ({
      accounts: state.accounts.map((account) =>
        account.accountId === accountId
          ? { ...account, balance: newBalance }
          : account
      ),
    }));
  },
}));
