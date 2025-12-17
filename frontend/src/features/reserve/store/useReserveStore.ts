// src/features/reserve/stores/useReserveStore.ts
import { create } from "zustand";
import { getReservesByAccount, releaseReservation } from "../service/reserveService";
import { ReserveResponseDTO, ReserveTransactionResponseDTO } from "../../../types/reserve/response";
import { useAccountStore } from "../../account/stores/useAccountStore";

interface ReserveState {
  reserves: ReserveResponseDTO[];
  loading: boolean;
  error: string | null;
  fetchReserves: (accountId: number) => Promise<void>;
  releaseReserve: (accountId: number, reservationId: number) => Promise<ReserveTransactionResponseDTO | null>;
}

export const useReserveStore = create<ReserveState>((set) => ({
  reserves: [],
  loading: false,
  error: null,

  fetchReserves: async (accountId: number) => {
    set({ loading: true, error: null });
    try {
      const data = await getReservesByAccount(accountId);
      set({ reserves: data });
    } catch (err: any) {
      set({ error: err.message || "Error cargando reservas" });
    } finally {
      set({ loading: false });
    }
  },

  releaseReserve: async (accountId: number, reservationId: number) => {
    set({ loading: true, error: null });
    try {
      const transaction = await releaseReservation(accountId, reservationId);

      // actualizar balance en store de cuentas
      const accountStore = useAccountStore.getState();
      const currentAccount = accountStore.getAccountById(accountId);
      if (currentAccount) {
        const newBalance = (currentAccount.balance || 0) + (transaction?.amount || 0);
        accountStore.updateAccountBalance(accountId, newBalance);
      }

      // 🔹 actualizar reservas globalmente
      await getReservesByAccount(accountId).then((data) => set({ reserves: data }));

      return transaction;
    } catch (err: any) {
      set({ error: err.message || "Error al liberar reserva" });
      return null;
    } finally {
      set({ loading: false });
    }
  },
}));
