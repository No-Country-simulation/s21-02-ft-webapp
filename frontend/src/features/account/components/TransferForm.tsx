import { Button } from '../../../components/ui/Button';
import { Alert } from '../../../components/ui/Alert';
import { Card } from '../../../components/ui/Card';
import { Input } from '../../../components/ui/Input';
import { Textarea } from '../../../components/ui/Textarea';
import { useTransfer } from '../hooks/useTransfer';
import { format } from 'date-fns';
import { useAccounts } from '../../../features/dashboard/hooks/useAccounts';
import { AccountResponse } from '../../../types/account/response';
import { useState, useEffect } from 'react';
import { useAuthStore } from '../../../features/auth/store/authStore';
import { Spinner } from '../../../components/ui/Spinner';

interface Props {
  sourceAccountId: number;
}

export const TransferForm = ({ sourceAccountId }: Props) => {
  const { user } = useAuthStore();
  const { accounts, loading: accountsLoading, error: accountsError } = useAccounts();
  const [accountError, setAccountError] = useState<string | null>(null);
  const [currentAccount, setCurrentAccount] = useState<AccountResponse | null>(null);

  // Validar y obtener la cuenta en un solo paso
  useEffect(() => {
    if (accounts.length > 0) {
      const foundAccount = accounts.find(account => account.accountId === sourceAccountId);
      
      if (!foundAccount) {
        setAccountError('No puedes transferir desde una cuenta que no te pertenece');
        setCurrentAccount(null);
      } else {
        setAccountError(null);
        setCurrentAccount(foundAccount);
      }
    }
  }, [accounts, sourceAccountId]);

  // Extraer propiedades del usuario
  const userName = user?.fullName || 'Cuenta';

  const {
    destinationIdentifier,
    amount,
    reason,
    isLoading,
    error: transferError,
    success,
    transactionDetails,
    destinationAccountName,
    handleChange,
    submitTransfer
  } = useTransfer(sourceAccountId);

  // Mostrar spinner mientras se cargan las cuentas
  if (accountsLoading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <Spinner size="lg" />
      </div>
    );
  }

  // Mostrar error de cuentas
  if (accountsError) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <Card className="w-full max-w-md p-6">
          <div className="text-center space-y-4">
            <Alert message="Error al cargar tus cuentas" variant="error" />
            <Button 
              onClick={() => window.location.reload()} 
              fullWidth
              className="mt-4"
            >
              Reintentar
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  // Mostrar error si la cuenta no pertenece al usuario
  if (accountError) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <Card className="w-full max-w-md p-6">
          <div className="text-center space-y-4">
            <Alert message={accountError} variant="error" />
            <Button 
              onClick={() => window.location.href = '/dashboard'} 
              fullWidth
              className="mt-4"
            >
              Volver al dashboard
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  // Mostrar confirmación de transferencia exitosa
  if (success && transactionDetails) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <Card className="w-full max-w-md p-6">
          <div className="text-center space-y-4">
            <div className="text-green-500 text-5xl mb-4">✓</div>
            <h2 className="text-2xl font-bold text-gray-800">Transferencia exitosa</h2>
            
            <div className="text-left space-y-2 bg-gray-50 p-4 rounded-lg">
              <p><span className="font-medium">N° Transacción:</span> {transactionDetails.transactionId}</p>
              <p><span className="font-medium">Fecha:</span> {format(new Date(transactionDetails.transactionDate), 'PPpp')}</p>
              <p><span className="font-medium">Desde:</span> {userName} - {transactionDetails.sourceAccount}</p>
              <p><span className="font-medium">Hacia:</span> {transactionDetails.destinationAccount || destinationAccountName}</p>
              <p><span className="font-medium">Monto:</span> {currentAccount?.currency} {parseFloat(amount).toLocaleString('es-AR', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
              })}</p>
              <p><span className="font-medium">Motivo:</span> {reason}</p>
            </div>

            <Button 
              onClick={() => window.location.href = '/dashboard'} 
              fullWidth
              className="mt-4"
            >
              Volver al dashboard
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  // Renderizar formulario de transferencia
  return (
    <div className="flex flex-col items-center min-h-screen mt-10">
      <Card className="w-full max-w-md">
        <div className="p-6">
          <div className="text-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Transferir {currentAccount?.currency}</h2>
            <p className="text-gray-600 mt-2">Desde: {userName}</p>
          </div>

          {transferError && <Alert message={transferError} variant="error" className="mb-4" />}

          <form onSubmit={(e) => {
            e.preventDefault();
            submitTransfer();
          }} className="space-y-4">
            <Input
              label="CBU o Alias del destinatario"
              value={destinationIdentifier}
              onChange={(e) => handleChange('destinationIdentifier', e.target.value)}
              placeholder="Ej: mi.alias o CBU0000000000000000000"
              required
              disabled={isLoading}
            />

            <Input
              label="Monto a transferir"
              type="number"
              value={amount}
              onChange={(e) => handleChange('amount', e.target.value)}
              placeholder={`Ej: 1000.00`}
              min="0.01"
              step="0.01"
              required
              disabled={isLoading}
              leftAddon={currentAccount?.currency === 'ARS' ? '$' : 'US$'}
            />

            <Textarea
              label="Motivo de la transferencia"
              value={reason}
              onChange={(e) => handleChange('reason', e.target.value)}
              placeholder="Ej: Pago de servicios"
              required
              disabled={isLoading}
              rows={3}
            />

            <div className="pt-2">
              <Button type="submit" fullWidth isLoading={isLoading}>
                Confirmar transferencia
              </Button>
            </div>
          </form>
        </div>
      </Card>
    </div>
  );
};