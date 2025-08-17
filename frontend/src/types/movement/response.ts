export interface MovementResponseDTO {
  movementId: number;
  movementDate: string;          // ISO date string
  description: string;
  amount: number;
  userName: string;
  transactionType: string
}