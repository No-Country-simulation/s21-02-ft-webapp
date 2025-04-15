export interface CardRequest {
    encryptedNumber: string;
    type: string;
    issuingBank: string;
    expirationDate: string; // Formato MM/YY
    encryptedCvv: string;
    balance: number;
  }