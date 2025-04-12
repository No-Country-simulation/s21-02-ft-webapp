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

export interface TransferResponse {
  transactionId: number;
  transactionDate: string;
  sourceAccount: string;
  destinationAccount: string;
  amount: number;
  reason: string;
  transactionType: string;
}
export interface TransferDetails {
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
  transactionDetails: TransferDetails | null;
  destinationAccountName: string;
}
