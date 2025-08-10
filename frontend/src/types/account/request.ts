export interface CreateAccountPayload {
    currency: string;
}

export interface TransferRequest {
    destinationIdentifier: string;
    amount: number;
    reason: string;
  }

  export interface TransactionFormProps {
    sourceAccountId: number;
  }

  export interface DepositRequest {
    amount: number;
    cardNumber: string;
  }

  export type TransferField = 'destinationIdentifier' | 'amount' | 'reason';
  export type DepositField =  'amount' | 'cardNumber';