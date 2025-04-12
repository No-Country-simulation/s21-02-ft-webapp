import { useState, useEffect } from 'react';
import { useAuthStore } from '../../auth/store/authStore';
import { useAccounts } from '../../dashboard/hooks/useAccounts';
import { useTransfer } from '../hooks/useTransfer';
import { TransferFormProps } from '../../../types/account/request';
import { TransferSuccess } from './transfer/TransferSuccess';
import { TransferError } from './transfer/TransferError';
import { TransferFormView } from './transfer/TransferFormView';
import { AccountResponse } from '../../../types/account/response';
import { Spinner } from '../../../components/ui/Spinner';


export const TransferForm = ({ sourceAccountId }: TransferFormProps) => {
  const { user } = useAuthStore();
  const { accounts, loading: accountsLoading, error: accountsError } = useAccounts();
  const [accountError, setAccountError] = useState<string | null>(null);
  const [currentAccount, setCurrentAccount] = useState<AccountResponse | null>(null);
  const [showConfirm, setShowConfirm] = useState(false);

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
    submitTransfer,
  } = useTransfer(sourceAccountId);

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

  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setShowConfirm(true);
  };

  const confirmTransfer = () => {
    setShowConfirm(false);
    submitTransfer();
  };

  const handleReturnToDashboard = () => {
    window.location.href = '/dashboard';
  };

  if (accountsLoading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <Spinner size="lg" />
      </div>
    );
  }

  if (accountsError) {
    return (
      <div className="flex flex-col items-center min-h-screen mt-10">
        <TransferError 
          message="Error al cargar tus cuentas" 
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
          destinationAccountName={destinationAccountName}
          onReturn={handleReturnToDashboard}
        />
      </div>
    );
  }

  if (!currentAccount) return null;

  return (
    <TransferFormView
      currentAccount={currentAccount}
      userName={user?.fullName || 'Cuenta'}
      transferError={transferError}
      destinationIdentifier={destinationIdentifier}
      amount={amount}
      reason={reason}
      isLoading={isLoading}
      showConfirm={showConfirm}
      onFormSubmit={handleFormSubmit}
      onInputChange={handleChange as unknown as (field: string, value: string) => void}
      onConfirmTransfer={confirmTransfer}
      onCloseModal={() => setShowConfirm(false)}
    />
  );
};