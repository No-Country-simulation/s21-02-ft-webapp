import { useState, useEffect } from 'react';
import { useAuthStore } from '../../auth/store/authStore';
import { useDeposit } from '../hooks/useDeposit';
import { useAccountStore } from '../stores/useAccountStore';
import { useCardStore } from '../../card/store/useCardStore';
import { Input } from '../../../components/ui/Input';
import { Select } from '../../../components/ui/Select';
import { Button } from '../../../components/ui/Button';
import { Card } from '../../../components/ui/Card';
import { TransactionFormProps } from '../../../types/account/request';

import { TransactionConfirmationModal } from './shared/TransactionConfirmationModal';
import { TransactionError } from './shared/TransactionError';
import { TransactionSuccess } from './shared/TransactionSuccess';

export const DepositForm = ({ sourceAccountId }: TransactionFormProps) => {
  const { user } = useAuthStore();

  // Stores para cuentas
  const accounts = useAccountStore(state => state.accounts);
  const loadingAccounts = useAccountStore(state => state.loading);
  const errorAccounts = useAccountStore(state => state.error);
  const fetchAccounts = useAccountStore(state => state.fetchAccounts);
  const currentAccount = useAccountStore(state => state.getAccountById(sourceAccountId));

  // Stores para tarjetas
  const cards = useCardStore(state => state.cards);
  const fetchCards = useCardStore(state => state.fetchCards);

  // Hook de lógica de depósito
  const {
    cardNumber,
    amount,
    isLoading,
    error,
    success,
    transactionDetails,
    handleChange,
    submitDeposit,
  } = useDeposit(sourceAccountId);

  // Estados locales para UI específica
  const [showConfirm, setShowConfirm] = useState(false);
  const [accountError] = useState<string | null>(null);
  const [validationError, setValidationError] = useState<string | null>(null);

  // Filtrar sólo tarjetas débito
  const debitCards = cards.filter(card => card.type === 'DEBIT');
  const isOwnCard = debitCards.some(card => card.encryptedNumber === cardNumber);

  // Carga inicial de cuentas y tarjetas
  useEffect(() => {
    if (accounts.length === 0) fetchAccounts();
  }, [accounts.length, fetchAccounts]);

  useEffect(() => {
    if (cards.length === 0) fetchCards();
  }, [cards.length, fetchCards]);

  // Seleccionar tarjeta débito por defecto si no hay seleccionada
  useEffect(() => {
    if (debitCards.length > 0 && !cardNumber) {
      handleChange('cardNumber', debitCards[0].encryptedNumber);
    }
  }, [debitCards, cardNumber, handleChange]);

  // Validaciones básicas antes de mostrar modal
  const validateInputs = (): boolean => {
    if (!cardNumber.trim()) {
      setValidationError('Seleccione una tarjeta válida.');
      return false;
    }

    const amountValue = parseFloat(amount);
    if (isNaN(amountValue) || amountValue <= 0) {
      setValidationError('Ingrese un monto válido mayor a cero.');
      return false;
    }

    // Validar saldo disponible en la cuenta origen si está definido
    if (currentAccount?.balance !== undefined && amountValue > currentAccount.balance) {
      setValidationError('Saldo insuficiente en la cuenta origen para realizar el depósito.');
      return false;
    }

    setValidationError(null);
    return true;
  };

  // Handlers
  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (validateInputs()) {
      setShowConfirm(true);
    }
  };

  const confirmDeposit = async () => {
    setShowConfirm(false);
    await submitDeposit();
  };

  const handleReturnToDashboard = () => {
    window.location.href = '/dashboard';
  };

  // Renderizados condicionales

  if (loadingAccounts) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <p>Cargando información de cuentas...</p>
      </div>
    );
  }

  if (errorAccounts) {
    return (
      <TransactionError
        message={errorAccounts}
        onAction={() => window.location.reload()}
        actionLabel="Reintentar"
      />
    );
  }

  if (accountError) {
    return (
      <TransactionError
        message={accountError}
        onAction={handleReturnToDashboard}
        actionLabel="Volver al dashboard"
      />
    );
  }

  if (!currentAccount) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <p>Cuenta origen no encontrada o no disponible.</p>
      </div>
    );
  }

  if (success && transactionDetails) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
      <TransactionSuccess
        userName={`Tarjeta ****${cardNumber.slice(-4)}`}
        currency={currentAccount.currency}
        amount={amount}
        reason="Depósito"
        transactionDetails={transactionDetails}
        destinationAccountName={user?.fullName || 'Cuenta'}
        onReturn={handleReturnToDashboard}
        type="DEPOSIT"
        // Si en TransactionSuccess tienes definida esta prop y la usas:
        // isOwnCard={isOwnCard} 
        // Si no la usas en TransactionSuccess la puedes quitar.
      />
      </div>
    );
  }

  return (
    <>
      <div className="flex flex-col items-center min-h-screen mt-10">
        <Card className="w-full max-w-md">
          <div className="p-6">
            <div className="text-center mb-6">
              <h2 className="text-2xl font-bold text-gray-800">Realizar Depósito</h2>
            </div>
            <form onSubmit={handleFormSubmit} className="space-y-4">
              <Select
                label="Tarjeta de débito"
                value={cardNumber}
                onChange={(e) => handleChange('cardNumber', e.target.value)}
                options={debitCards.map((card) => ({
                  value: card.encryptedNumber,
                  label: `${card.issuingBank} - ****${card.encryptedNumber.slice(-4)} - $${card.balance.toFixed(
                    2
                  )}`,
                }))}
                required
              />

              <Input
                label="Monto a depositar"
                type="number"
                min="0.01"
                step="0.01"
                value={amount}
                onChange={(e) => handleChange('amount', e.target.value)}
                placeholder="Ej: 500.50"
                required
              />

              {validationError && <p className="text-red-600">{validationError}</p>}
              {error && <p className="text-red-600">{error}</p>}

              <Button type="submit" isLoading={isLoading} fullWidth>
                Realizar Depósito
              </Button>
            </form>
          </div>
        </Card>
      </div>

      <TransactionConfirmationModal
        isOpen={showConfirm}
        onClose={() => setShowConfirm(false)}
        onConfirm={confirmDeposit}
        currency={currentAccount.currency}
        amount={amount}
        destinationIdentifier={cardNumber} // número de tarjeta destino
        destinationName={`Tarjeta ****${cardNumber.slice(-4)}`} // opcional, evita fetch
        isOwnCard={isOwnCard}
      />
    </>
  );
};
