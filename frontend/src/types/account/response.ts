export interface AccountResponse {
  userId: number;
  accountId: number;
  cbu: string;
  alias: string;
  currency: string;
  balance: number;
  reservedBalance: number;
}

export interface CurrencyOptionResponse {
  value: string;
  label: string;
}

export interface TransactionResponse {
  transactionId: number;
  transactionDate: string;
  sourceAccount: string;
  destinationAccount: string;
  amount: number;
  reason: string;
  transactionType: string;
}
export interface TransactionDetails {
  transactionId: number;
  transactionDate: string;
  sourceAccount: string;
  destinationAccount: string;
}

export interface TransferState {
  destinationIdentifier: string;
  amount: string;
  reason: string;
  isLoading: boolean;
  error: string | null;
  success: boolean;
  transactionDetails: TransactionDetails | null;
  destinationAccountName: string;
}

export interface TransactionResponseDTO {
  transactionId: number;
  transactionDate: string;          // ISO date string
  reason: string;
  amount: number;
  destinationAccount: string;
  transactionType: string
}

export interface MovementResponseDTO {
  movementId: number;
  movementDate: string;          // ISO date string
  description: string;
  amount: number;
  destinationAccount: string;
  transactionType: string
}