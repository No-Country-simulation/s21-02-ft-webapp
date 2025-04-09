export interface AccountResponse {
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