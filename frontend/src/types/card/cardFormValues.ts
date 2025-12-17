export interface CardFormValues {
    number: string;
    type: "DEBIT";
    issuingBank: string;
    expirationDate: string;
    cvv: string;
    balance?: number;
  }