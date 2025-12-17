// src/features/account/stores/useAccountStore.ts
import { create } from 'zustand';
import { api } from '../../../services/api';
import { useAuthStore } from '../../../features/auth/store/authStore';
import { AccountResponse } from '../../../types/account/response';

interface AccountState {
  accounts: AccountResponse[];
  loading: boolean;
  error: string | null;
  activeAccountId: number | null; 
  setActiveAccountId: (id: number) => void; 
  fetchAccounts: () => Promise<void>;
  updateAccountBalance: (accountId: number, newBalance: number) => void;
  getAccountById: (accountId: number) => AccountResponse | undefined;
}

export const useAccountStore = create<AccountState>((set, get) => ({
  accounts: [],
  loading: false,
  error: null,
  activeAccountId: null,

  setActiveAccountId: (id: number) => set({ activeAccountId: id }),

  fetchAccounts: async () => {
    set({ loading: true, error: null });
    try {
      const token = useAuthStore.getState().token;
      const response = await api.get<AccountResponse[]>('/accounts', {
        headers: {'Authorization': `Bearer ${token}`,},
      });
      
      const fetchedAccounts = response.data;
      set({ accounts: fetchedAccounts, loading: false });

      if (fetchedAccounts.length > 0) {
        const arsAccount = fetchedAccounts.find(account => account.currency.toUpperCase() === 'ARS');
        const accountToActivate = arsAccount ? arsAccount.accountId : fetchedAccounts[0].accountId;
        set({ activeAccountId: accountToActivate });
      } else {
        set({ activeAccountId: null });
      }

    } catch (error: any) {
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

  getAccountById: (accountId: number) =>
    get().accounts.find((account: AccountResponse) => account.accountId === accountId),
}));