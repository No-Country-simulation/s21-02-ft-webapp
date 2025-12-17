export interface ReserveResponseDTO {
    reservationId: number;
    accountId: number;
    reservedAmount: number;
    creationDate: string;
    status: string;
    reason: string;
}

export interface ReserveTransactionResponseDTO {
    transactionId: number;
    transactionDate: string;
    sourceAccount: string;
    destinationAccount: string;
    amount: number;
    reason: string;
    transactionType: string;
}

export type SuggestedReserveResponseDTO = {
    id: number;
    name: string;
    iconUrl: string; // The URL of the icon from the API
};
