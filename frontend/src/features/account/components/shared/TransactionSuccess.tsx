import { format } from 'date-fns';
import { Card } from '../../../../components/ui/Card'
import { Button } from '../../../../components/ui/Button'
import { TransactionDetails } from '../../../../types/account/response';

interface TransactionSuccessProps {
  userName: string;
  currency: string;
  amount: string;
  reason: string;
  transactionDetails: TransactionDetails;
  destinationAccountName: string;
  onReturn: () => void;
  isOwnCard?: boolean;
}

export const TransactionSuccess = ({
  userName,
  currency,
  amount,
  reason,
  transactionDetails,
  destinationAccountName,
  onReturn,
  isOwnCard,
}: TransactionSuccessProps) => (
  <Card className="w-full max-w-md p-6">
    <div className="text-center space-y-4">
      <div className="text-green-500 text-5xl mb-4">✓</div>
      <h2 className="text-2xl font-bold text-gray-800">{isOwnCard? 'Deposito realizado' : 'Transferencia realizada'}</h2>
      <div className="text-left space-y-2 bg-gray-50 p-4 rounded-lg">
        <p><span className="font-medium">N° Transacción:</span> {transactionDetails.transactionId}</p>
        <p><span className="font-medium">Fecha:</span> {format(new Date(transactionDetails.transactionDate), 'PPpp')}</p>
        <p><span className="font-medium">Desde:</span> {userName}</p>
        <p><span className="font-medium">Hacia:</span> {transactionDetails.destinationAccount || destinationAccountName}</p>
        <p><span className="font-medium">Monto:</span> {currency} {parseFloat(amount).toLocaleString('es-AR', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        })}</p>
        <p><span className="font-medium">Motivo:</span> {reason}</p>
      </div>

      <Button onClick={onReturn} fullWidth className="mt-4">
        Volver al dashboard
      </Button>
    </div>
  </Card>
);