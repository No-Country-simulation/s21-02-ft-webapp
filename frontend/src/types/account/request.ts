export interface CreateAccountPayload {
    currency: string;
}

export interface TransferRequest {
    destinationIdentifier: string;
    amount: number;
    reason: string;
  }

  export interface TransferFormProps {
    sourceAccountId: number;
  }

  export type TransferField = 'destinationIdentifier' | 'amount' | 'reason';