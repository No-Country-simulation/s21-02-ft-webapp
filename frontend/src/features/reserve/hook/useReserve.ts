import { useEffect, useState } from "react";
import { getReservesByAccount, releaseReservation } from "../service/reserveService";
import { ReserveResponseDTO, ReserveTransactionResponseDTO } from "../../../types/reserve/response";

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

  const releaseReserve = async (reservationId: number): Promise<ReserveTransactionResponseDTO  | null> => {
  if (!accountId) return null;
  setLoading(true);
  try {
    const response = await releaseReservation(accountId, reservationId); // llama al service
    await fetchReserves(); // actualiza la lista
    return response; // 🚀 devuelve la transacción para el modal
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
