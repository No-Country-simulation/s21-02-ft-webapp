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
  