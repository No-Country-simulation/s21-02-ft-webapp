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
  // Contenedor principal para el centrado del modal
  // Estas clases crean un overlay fijo que ocupa toda la pantalla y centra su contenido
  <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50 p-4">
    {/* La tarjeta con los detalles de la transacción */}
    <Card className="w-full max-w-md p-6 bg-white rounded-lg shadow-xl transform transition-all scale-100 ease-out duration-300">
      <div className="text-center space-y-4">
        {/* Ícono de éxito mejorado */}
        <div className="mb-4">
          <svg
            className="w-16 h-16 text-green-500 mx-auto"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
            ></path>
          </svg>
        </div>
        <h2 className="text-2xl font-bold text-gray-800">
          {isOwnCard ? 'Depósito realizado' : 'Transferencia realizada'}
        </h2>
        <p className="text-gray-600 text-lg">¡Operación completada con éxito!</p>

        {/* Detalles de la transacción */}
        <div className="text-left space-y-2 bg-gray-50 p-4 rounded-lg border border-gray-100">
          <p><span className="font-medium text-gray-700">N° Transacción:</span> <span className="text-gray-900">{transactionDetails.transactionId}</span></p>
          <p><span className="font-medium text-gray-700">Fecha:</span> <span className="text-gray-900">{format(new Date(transactionDetails.transactionDate), 'PPpp')}</span></p>
          <p><span className="font-medium text-gray-700">Desde:</span> <span className="text-gray-900">{userName}</span></p>
          <p><span className="font-medium text-gray-700">Hacia:</span> <span className="text-gray-900">{transactionDetails.destinationAccount || destinationAccountName}</span></p>
          <p>
            <span className="font-medium text-gray-700">Monto:</span>{' '}
            <span className="font-semibold text-blue-600">
              {currency} {parseFloat(amount).toLocaleString('es-AR', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
              })}
            </span>
          </p>
          <p><span className="font-medium text-gray-700">Motivo:</span> <span className="text-gray-900">{reason}</span></p>
        </div>

        {/* Botón de retorno */}
        <Button onClick={onReturn} fullWidth className="mt-6 bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg focus:outline-none focus:shadow-outline transition-colors duration-200">
          Volver al dashboard
        </Button>
      </div>
    </Card>
  </div>
);