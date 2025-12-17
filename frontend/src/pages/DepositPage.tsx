import { DepositForm } from '../features/account/components/DepositForm';
import { PageContainer } from '../components/ui/PageContainer';
import { useEffect } from 'react';
import { useAccountStore } from '../features/account/stores/useAccountStore';
import { useCardStore } from '../features/card/store/useCardStore';
import { useSearchParams } from 'react-router-dom';

export const DepositPage = () => {
  const { fetchAccounts, accounts } = useAccountStore();
  const { fetchCards } = useCardStore();
  const [searchParams] = useSearchParams();

  // Obtener parámetro y convertir a número, o null si inválido
  const sourceAccountIdParam = searchParams.get('sourceAccountId');
  const sourceAccountId = sourceAccountIdParam && !isNaN(+sourceAccountIdParam)
    ? Number(sourceAccountIdParam)
    : null;

  useEffect(() => {
    fetchAccounts();
    fetchCards();
  }, [fetchAccounts, fetchCards]);

  // Buscar cuenta si tenemos un sourceAccountId válido
  const foundAccount = sourceAccountId !== null
    ? accounts.find(acc => acc.accountId === sourceAccountId)
    : null;

  // Opcional: Mostrar error si ID inválido o cuenta no encontrada
  if (sourceAccountId === null) {
    return <PageContainer><p>ID de cuenta no válido.</p></PageContainer>;
  }

  if (!foundAccount) {
    return <PageContainer><p>Cuenta no encontrada.</p></PageContainer>;
  }

  // Si todo ok, renderizar el formulario con sourceAccountId válido
  return (
    <PageContainer>
      <DepositForm sourceAccountId={sourceAccountId} />
    </PageContainer>
  );
};
