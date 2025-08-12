export interface ReserveResponseDTO {
    reservationId: number;
    accountId: number;
    reservedAmount: number;
    creationDate: string;
    status: string;
    type: string
}
