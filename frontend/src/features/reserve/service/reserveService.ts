import {api} from "../../../services/api";
import { ReserveResponseDTO } from "../../../types/reserve/response";

export const getReservesByAccount = async (accountId: number): Promise<ReserveResponseDTO[]> => {
    const response = await api.get(`/accounts/${accountId}/reservations`);
    return response.data;
};