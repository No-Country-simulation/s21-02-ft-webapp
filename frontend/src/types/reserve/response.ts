export interface ReserveResponseDTO {
    reservationId: number;
    accountId: number;
    reservedAmount: number;
    creationDate: string;
    status: string;
    type: string
}
// src/types/reserve/response.ts

// This is an example of what your DTO type should look like
export type ReservationTypeResponseDTO = {
    id: number;
    name: string;
    iconUrl: string; // The URL of the icon from the API
};