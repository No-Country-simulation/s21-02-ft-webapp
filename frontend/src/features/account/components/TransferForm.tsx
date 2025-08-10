import { useState, useEffect } from 'react';
import { useAuthStore } from '../../auth/store/authStore';
import { useAccountStore } from '../../account/stores/useAccountStore';
import { useTransfer } from '../hooks/useTransfer';
import { TransactionFormProps, TransferField } from '../../../types/account/request';
import { TransactionSuccess } from './shared/TransactionSuccess';
import { TransactionError } from './shared/TransactionError';
import { TransferFormView } from './TransferFormView';
import { Spinner } from '../../../components/ui/Spinner';
import { validateDestination } from '../services/transferServices';
export const TransferForm = ({ sourceAccountId }: TransactionFormProps) => {
  const { user } = useAuthStore();

  // Consumo del estado global de cuentas
  const accounts = useAccountStore(state => state.accounts);
  const loading = useAccountStore(state => state.loading);
  const error = useAccountStore(state => state.error);
  const fetchAccounts = useAccountStore(state => state.fetchAccounts);
  const currentAccount = useAccountStore(state => state.getAccountById(sourceAccountId));

  // --- Estados locales para control puntual y UI ---
  const [accountError, setAccountError] = useState<string | null>(null);
  const [showConfirm, setShowConfirm] = useState(false);
  const [validationError, setValidationError] = useState<string | null>(null);
  const [destinationName, setDestinationName] = useState<string>('');

  const {
    destinationIdentifier,
    amount,
    reason,
    isLoading,
    error: transferError,
    success,
    transactionDetails,
    handleChange,
    submitTransfer,
  } = useTransfer(sourceAccountId);

  // --- Efectos ---
  useEffect(() => {
    if (accounts.length === 0) {
      fetchAccounts();
    }
  }, [accounts.length, fetchAccounts]);

  useEffect(() => {
    if (accounts.length > 0 && !currentAccount) {
      setAccountError('No puedes transferir desde una cuenta que no te pertenece');
    } else {
      setAccountError(null);
    }
  }, [accounts.length, currentAccount]);

  // --- Funciones para validaciones ---

  const validateDestinationAccount = async (): Promise<boolean> => {
    try {
      const result = await validateDestination(destinationIdentifier);
      if (!result.isValid) {
        setValidationError('El CBU/Alias no existe o no es válido');
        return false;
      }
      setDestinationName(result.accountName || 'Cuenta válida');
      // setDestinationValid(true);  <-- esta línea no aporta y puede eliminarse
      return true;
    } catch {
      setValidationError('Error al validar el destino');
      return false;
    }
  };

  const validateInputs = async (): Promise<boolean> => {
    if (!destinationIdentifier.trim()) {
      setValidationError('Ingrese un CBU o Alias válido');
      return false;
    }

    if (destinationIdentifier.length < 5) {
      setValidationError('El CBU o Alias debe tener al menos 5 caracteres');
      return false;
    }

    const amountValue = parseFloat(amount);
    if (isNaN(amountValue)) {
      setValidationError('Ingrese un monto válido');
      return false;
    }

    if (amountValue <= 0) {
      setValidationError('El monto debe ser mayor a cero');
      return false;
    }

    // Usar operador seguro para balance, chequea que exista y compara monto
    if (currentAccount?.balance !== undefined && amountValue > currentAccount.balance) {
      setValidationError('Saldo insuficiente para realizar la transferencia');
      return false;
    }

    if (!reason.trim()) {
      setValidationError('Ingrese un motivo para la transferencia');
      return false;
    }

    const isDestinationValid = await validateDestinationAccount();
    if (!isDestinationValid) {
      return false;
    }

    setValidationError(null);
    return true;
  };

  // --- Handlers ---

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setValidationError(null);
    const isValid = await validateInputs();
    if (isValid) {
      setShowConfirm(true);
    }
  };

  const confirmTransfer = () => {
    setShowConfirm(false);
    submitTransfer();
  };

  const handleReturnToDashboard = () => {
    window.location.href = '/dashboard';
  };

  const handleInputChange = (field: TransferField, value: string) => {
    handleChange(field, value);

    if (validationError) setValidationError(null);

    // Al cambiar destino, reseteamos nombre destino
    if (field === 'destinationIdentifier') {
      setDestinationName('');
      // setDestinationValid(false);  <-- eliminar porque no se usa
    }
  };

  // --- Renderizado condicional ---

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <Spinner size="lg" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <TransactionError message={error} onAction={() => window.location.reload()} actionLabel="Reintentar" />
      </div>
    );
  }

  if (accountError) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <TransactionError message={accountError} onAction={handleReturnToDashboard} actionLabel="Volver al dashboard" />
      </div>
    );
  }

  if (success && transactionDetails && currentAccount) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <TransactionSuccess
          userName={user?.fullName || 'Cuenta'}
          currency={currentAccount.currency}
          amount={amount}
          reason={reason}
          transactionDetails={transactionDetails}
          destinationAccountName={destinationName}
          onReturn={handleReturnToDashboard}
        />
      </div>
    );
  }

  if (!currentAccount) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <Spinner size="lg" />
      </div>
    );
  }

  // Renderizado principal con componente visual pasándole todos los props necesarios
  return (
    <TransferFormView
      currentAccount={currentAccount}
      userName={user?.fullName || 'Cuenta'}
      transferError={transferError}
      validationError={validationError}
      destinationIdentifier={destinationIdentifier}
      amount={amount}
      reason={reason}
      isLoading={isLoading}
      showConfirm={showConfirm}
      onFormSubmit={handleFormSubmit}
      onInputChange={handleInputChange}
      onConfirmTransfer={confirmTransfer}
      onCloseModal={() => setShowConfirm(false)}
      destinationName={destinationName}
    />
  );
};

