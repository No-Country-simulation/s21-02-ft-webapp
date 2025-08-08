// features/account/components/deposit/DepositSuccess.tsx
import { Button } from '../../../../components/ui/Button';

export const DepositSuccess = ({ transaction, onClose }: {
  transaction: any;
  onClose: () => void;
}) => {
  return (
    <div className="text-center p-6">
      <h3 className="text-xl font-bold text-green-600 mb-4">¡Depósito Exitoso!</h3>
      <div className="space-y-2 mb-6">
        <p><strong>Monto:</strong> ${transaction.amount.toLocaleString()}</p>
        <p><strong>Tarjeta:</strong> **** **** **** {transaction.reason.slice(-4)}</p>
        <p><strong>Fecha:</strong> {new Date(transaction.transactionDate).toLocaleString()}</p>
      </div>
      <Button onClick={onClose} variant="outline">
        Cerrar
      </Button>
    </div>
  );
};