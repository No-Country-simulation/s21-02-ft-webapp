import { useEffect, useState } from "react";
import { getReservesByAccount } from "../service/reserveService";
import { ReserveResponseDTO } from "../../../types/reserve/response";

export const useReserves = (activeAccountId: number) => {
  // Cambiado 'reserve' a 'reserves' para mayor claridad y consistencia.
  const [reserves, setReserves] = useState<ReserveResponseDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!activeAccountId) return;

    const fetchReserves = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await getReservesByAccount(activeAccountId);
        // Cambiado 'setReserve' a 'setReserves'
        setReserves(data);
      } catch (err: any) {
        setError(err.message || 'Error cargando reservas');
      } finally {
        setLoading(false);
      }
    };

    fetchReserves();
  }, [activeAccountId]);

  // Se devuelve 'reserves' en lugar de 'reserve'
  return { reserves, loading, error };
};