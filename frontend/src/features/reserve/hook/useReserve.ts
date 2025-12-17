import { useEffect, useState } from "react";
import { getReservesByAccount, releaseReservation } from "../service/reserveService";
import { ReserveResponseDTO, ReserveTransactionResponseDTO } from "../../../types/reserve/response";
import { useAccountStore } from "../../account/stores/useAccountStore";

export const useReserves = (accountId?: number) => {
  const [reserves, setReserves] = useState<ReserveResponseDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchReserves = async () => {
    if (accountId === undefined) {
      setReserves([]);
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const data = await getReservesByAccount(accountId);
      setReserves(data);
    } catch (err: any) {
      setError(err.message || "Error cargando reservas");
    } finally {
      setLoading(false);
    }
  };

const releaseReserve = async (reservationId: number): Promise<ReserveTransactionResponseDTO | null> => {
    if (!accountId) return null;
    setLoading(true);
    try {
      const transaction = await releaseReservation(accountId, reservationId);

      // 🔹 Actualizar saldo en store
      const accountStore = useAccountStore.getState();
      const currentAccount = accountStore.getAccountById(accountId);
      if (currentAccount) {
        const newBalance = (currentAccount.balance || 0) + (transaction?.amount || 0);
        accountStore.updateAccountBalance(accountId, newBalance);
      }

      // 🔹 Refrescar reservas
      await fetchReserves();

      return transaction;
    } catch (err: any) {
      setError(err.message || "Error al liberar reserva");
      return null;
    } finally {
      setLoading(false);
    }
  };




  useEffect(() => {
    fetchReserves();
  }, [accountId]);

  return { reserves, loading, error, releaseReserve, fetchReserves };
};
