export interface ReserveResponseDTO {
    transactionId: number;
    accountId: number;
    reservedAmount: number;
    creationDate: string;
    status: string;
    reason: string;
}
export type SuggestedReserveResponseDTO = {
    id: number;
    name: string;
    iconUrl: string; // The URL of the icon from the API
};
