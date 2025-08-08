import { DepositForm } from '../features/account/components/deposit/DepositForm';
import { PageContainer } from '../components/ui/PageContainer';
import { useEffect } from 'react';
import { useAccountStore } from '../features/account/stores/useAccountStore';
import { useCardStore } from '../features/card/store/useCardStore';
import { useSearchParams } from 'react-router-dom';

export const DepositPage = () => {
  const { fetchAccounts, accounts } = useAccountStore();
  const { fetchCards } = useCardStore();
  const [searchParams] = useSearchParams();
  const sourceAccountIdParam = searchParams.get('sourceAccountId');

  // Convierte el parámetro a un número. Si es nulo o no es un número, será null.
  const sourceAccountId = sourceAccountIdParam ? Number(sourceAccountIdParam) : null;

  useEffect(() => {
    fetchAccounts();
    fetchCards();
  }, [fetchAccounts, fetchCards]);

  // Si se encuentra un ID válido, buscamos la cuenta.
  const foundAccount = sourceAccountId
    ? accounts.find(acc => acc.accountId === sourceAccountId)
    : undefined;

  // Asignamos explícitamente null si no se encuentra la cuenta.
  const selectedAccount = foundAccount ?? null;

  return (
    <PageContainer>
      <DepositForm sourceAccountId={sourceAccountId} selectedAccount={selectedAccount} />
    </PageContainer>
  );
};