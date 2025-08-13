// src/features/reservation/hook/useReservationTypes.ts
import { useState, useEffect } from 'react';
import { getAllReservationTypes } from "../service/reservationTypeService";
import { ReservationTypeResponseDTO } from '../../../types/reserve/response';

export const useReservationTypes = () => {
  const [types, setTypes] = useState<ReservationTypeResponseDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTypes = async () => {
      try {
        const data = await getAllReservationTypes();
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