import { Alert } from '../../../../components/ui/Alert';
import { Button } from '../../../../components/ui/Button';
import { Card } from '../../../../components/ui/Card';
import { Input } from '../../../../components/ui/Input';
import { Textarea } from '../../../../components/ui/Textarea';
import { AccountResponse } from '../../../../types/account/response';
import { TransferConfirmationModal } from '../../components/transfer/TransferConfirmationModal';

interface TransferFormViewProps {
  currentAccount: AccountResponse;
  userName: string;
  transferError: string | null;
  destinationIdentifier: string;
  amount: string;
  reason: string;
  isLoading: boolean;
  showConfirm: boolean;
  onFormSubmit: (e: React.FormEvent) => void;
  onInputChange: (field: string, value: string) => void;
  onConfirmTransfer: () => void;
  onCloseModal: () => void;
}

export const TransferFormView = ({
  currentAccount,
  userName,
  transferError,
  destinationIdentifier,
  amount,
  reason,
  isLoading,
  showConfirm,
  onFormSubmit,
  onInputChange,
  onConfirmTransfer,
  onCloseModal,
}: TransferFormViewProps) => (
  <div className="flex flex-col items-center min-h-screen mt-10">
    <Card className="w-full max-w-md">
      <div className="p-6">
        <div className="text-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Transferir {currentAccount.currency}</h2>
          <p className="text-gray-600 mt-2">Desde: {userName}</p>
          {currentAccount.balance !== null && (
            <p className="text-sm text-gray-500 mt-1">
              Saldo disponible: {currentAccount.currency} {currentAccount.balance.toLocaleString('es-AR', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
              })}
            </p>
          )}
        </div>

        {transferError && <Alert message={transferError} variant="error" className="mb-4" />}

        <form onSubmit={onFormSubmit} className="space-y-4">
          <Input
            label="CBU o Alias del destinatario"
            value={destinationIdentifier}
            onChange={(e) => onInputChange('destinationIdentifier', e.target.value)}
            placeholder="Ej: mi.alias o CBU0000000000000000000"
            required
            disabled={isLoading}
          />

          <Input
            label="Monto a transferir"
            type="number"
            value={amount}
            onChange={(e) => onInputChange('amount', e.target.value)}
            placeholder={`Ej: 1000.00`}
            min="0.01"
            step="0.01"
            required
            disabled={isLoading}
            leftAddon={currentAccount.currency === 'ARS' ? '$' : 'US$'}
          />

          <Textarea
            label="Motivo de la transferencia"
            value={reason}
            onChange={(e) => onInputChange('reason', e.target.value)}
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

    <TransferConfirmationModal
      isOpen={showConfirm}
      onClose={onCloseModal}
      onConfirm={onConfirmTransfer}
      currency={currentAccount.currency}
      amount={amount}
      destinationIdentifier={destinationIdentifier}
    />
  </div>
);