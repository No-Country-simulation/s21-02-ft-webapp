export interface TransactionResponseDTO {
  transactionId: number;
  transactionDate: string;          // ISO date string
  reason: string;
  amount: number;
  destinationAccount: string;
  transactionType: string
}