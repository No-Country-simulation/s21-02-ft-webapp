// src/features/reservation/hook/useReservationTypes.ts
import { useState, useEffect } from 'react';
import { getAllSuggestedReserves } from "../service/suggestedReserveService";
import { SuggestedReserveResponseDTO } from '../../../types/reserve/response';

export const useSuggestedReserve = () => {
  const [types, setTypes] = useState<SuggestedReserveResponseDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTypes = async () => {
      try {
        const data = await getAllSuggestedReserves();
        setTypes(data);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchTypes();
  }, []);

  return { types, loading, error };
};