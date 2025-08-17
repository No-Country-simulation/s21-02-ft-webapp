import { useEffect, useState } from "react";
import { getReservesByAccount } from "../service/reserveService";
import { ReserveResponseDTO } from "../../../types/reserve/response";

// Se actualiza la firma para aceptar activeAccountId como number | null
export const useReserves = (activeAccountId: number | null) => {
  const [reserves, setReserves] = useState<ReserveResponseDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // Solo se llama a la API si activeAccountId no es null
    if (activeAccountId === null) {
      setReserves([]); // Limpiar reservas si no hay cuenta activa
      return;
    }

    const fetchReserves = async () => {
      setLoading(true);
      setError(null);
      try {
        const data = await getReservesByAccount(activeAccountId); // activeAccountId ahora es definitivamente un número aquí
        setReserves(data);
      } catch (err: any) {
        setError(err.message || 'Error cargando reservas');
      } finally {
        setLoading(false);
      }
    };

    fetchReserves();
  }, [activeAccountId]);

  return { reserves, loading, error };
};
