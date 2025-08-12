import {api} from "../../../services/api";
import {MovementResponseDTO} from "../../../types/movement/response";

export const getMovementsByAccount = async (accountId: number): Promise<MovementResponseDTO[]> => {
    const response = await api.get(`/accounts/${accountId}/movements`);
    return response.data;
};