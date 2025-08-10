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
   // Nuevo selector para obtener cuenta por id
  getAccountById: (accountId: number) => AccountResponse | undefined;
}

export const useAccountStore = create<AccountState>((set, get) => ({
  accounts: [],
  loading: false,
  error: null,

  // Cargar cuentas desde la API
  fetchAccounts: async () => {
    set({ loading: true, error: null });
    try {
      const token = useAuthStore.getState().token;
      const response = await api.get<AccountResponse[]>('/accounts', {
        headers: {'Authorization': `Bearer ${token}`,},
      });
      set({ accounts: response.data, loading: false });
    } catch (error: any) {
      set({
        error: error instanceof Error ? error.message : 'Error al cargar cuentas',
        loading: false,
      });
    }
  },

  // Actualiza el balance de la cuenta especificada con el nuevo saldo
  updateAccountBalance: (accountId: number, newBalance: number) => {
    set((state) => ({
      accounts: state.accounts.map((account) =>
        account.accountId === accountId
          ? { ...account, balance: newBalance }  // 🟢 Actualiza solo esa cuenta
          : account
      ),
    }));
  },

   // Selector para obtener cuenta por id
  getAccountById: (accountId: number) =>
    get().accounts.find((account: AccountResponse) => account.accountId === accountId),
}));

