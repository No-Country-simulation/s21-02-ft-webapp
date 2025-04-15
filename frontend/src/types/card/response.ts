export interface CardResponse {
    encryptedNumber: string;
    type: 'DEBIT';
    issuingBank: string;
    expirationDate: string;
    balance: number;
    createdAt: string;
    encryptedCvv: string;
    lastUpdated: string;
  }