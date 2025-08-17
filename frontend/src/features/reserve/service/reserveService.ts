import { api } from "../../../services/api";
import { CreateReservationRequest } from "../../../types/reserve/request";
import { ReserveResponseDTO } from "../../../types/reserve/response";

export const getReservesByAccount = async (accountId: number): Promise<ReserveResponseDTO[]> => {
  const response = await api.get(`/accounts/${accountId}/reservations`);
  return response.data;
};

export const createReservation = async (accountId: number, data: CreateReservationRequest): Promise<ReserveResponseDTO> => {
  try {
    const response = await api.post<ReserveResponseDTO>(
      `/accounts/${accountId}/reservations`,
      data
    );
    return response.data;
  } catch (error: any) {
    throw new Error(error.response?.data?.message || 'Error al crear la reserva');
  }
};
