import { api } from "../../../services/api";
import { CreateReservationRequest } from "../../../types/reserve/request";
import { ReserveResponseDTO } from "../../../types/reserve/response";
import { ReserveTransactionResponseDTO } from "../../../types/reserve/response";

export const getReservesByAccount = async (accountId: number): Promise<ReserveResponseDTO[]> => {
  const response = await api.get(`/accounts/${accountId}/reservations`);
  return response.data;
};

export const createReservation = async (
  accountId: number,
  data: CreateReservationRequest
): Promise<ReserveTransactionResponseDTO> => {
  try {
    const response = await api.post<ReserveTransactionResponseDTO>(`/accounts/${accountId}/reservations`, data);
    return response.data;
  } catch (error: any) {
    throw new Error(error.response?.data?.message || "Error al crear la reserva");
  }
};

export const releaseReservation = async (
  accountId: number,
  reservationId: number
): Promise<ReserveTransactionResponseDTO> => {
  try {
    const response = await api.post<ReserveTransactionResponseDTO>(`/accounts/${accountId}/reservations/${reservationId}/release`);
    return response.data;
  } catch (error: any) {
    throw new Error(error.response?.data?.message || "Error al liberar la reserva");
  }
};