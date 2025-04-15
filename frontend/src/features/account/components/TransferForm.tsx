import { useState, useEffect } from 'react';
import { useAuthStore } from '../../auth/store/authStore';
import { useAccountStore } from '../../account/stores/useAccountStore';
import { useTransfer } from '../hooks/useTransfer';
import { TransferFormProps, TransferField } from '../../../types/account/request';
import { TransferSuccess } from './transfer/TransferSuccess';
import { TransferError } from './transfer/TransferError';
import { TransferFormView } from './transfer/TransferFormView';
import { AccountResponse } from '../../../types/account/response';
import { Spinner } from '../../../components/ui/Spinner';
import { validateDestination } from '../services/transferServices';

export const TransferForm = ({ sourceAccountId }: TransferFormProps) => {
  const { user } = useAuthStore();
  const { accounts, loading, error, fetchAccounts } = useAccountStore();
  const [accountError, setAccountError] = useState<string | null>(null);
  const [currentAccount, setCurrentAccount] = useState<AccountResponse | null>(null);
  const [showConfirm, setShowConfirm] = useState(false);
  const [validationError, setValidationError] = useState<string | null>(null);
  const [destinationValid, setDestinationValid] = useState<boolean>(false);
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

  useEffect(() => {
    fetchAccounts();
  }, [fetchAccounts]);

  useEffect(() => {
    if (accounts.length > 0) {
      const foundAccount = accounts.find((account) => account.accountId === sourceAccountId);
      if (!foundAccount) {
        setAccountError('No puedes transferir desde una cuenta que no te pertenece');
        setCurrentAccount(null);
      } else {
        setAccountError(null);
        setCurrentAccount(foundAccount);
      }
    }
  }, [accounts, sourceAccountId]);

  const validateDestinationAccount = async (): Promise<boolean> => {
    try {
      const result = await validateDestination(destinationIdentifier);
      if (!result.isValid) {
        setValidationError('El CBU/Alias no existe o no es válido');
        return false;
      }
      setDestinationName(result.accountName || 'Cuenta válida');
      setDestinationValid(true);
      return true;
    } catch (error) {
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

    if (currentAccount?.balance !== null && amountValue > currentAccount!!.balance) {
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
    if (validationError) {
      setValidationError(null);
    }
    if (field === 'destinationIdentifier') {
      setDestinationValid(false);
      setDestinationName('');
    }
  };

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
        <TransferError 
          message={error} 
          onAction={() => window.location.reload()} 
          actionLabel="Reintentar" 
        />
      </div>
    );
  }

  if (accountError) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <TransferError 
          message={accountError} 
          onAction={handleReturnToDashboard} 
          actionLabel="Volver al dashboard" 
        />
      </div>
    );
  }

  if (success && transactionDetails && currentAccount) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <TransferSuccess
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
