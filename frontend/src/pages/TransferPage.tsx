// src/pages/TransferPage.tsx
import { useSearchParams } from 'react-router-dom';
import { TransferForm } from '../features/account/components/TransferForm';
import { PageContainer } from '../components/ui/PageContainer';

export const TransferPage = () => {
  const [searchParams] = useSearchParams(); // Obtiene los parámetros de la URL
  const sourceAccountIdParam = searchParams.get('sourceAccountId'); //

  // Validación básica
  const sourceAccountId = sourceAccountIdParam ? parseInt(sourceAccountIdParam) : null; // Convierte a entero

  // Si el parámetro es inválido, podrías mostrar un error o redireccionar
  if (!sourceAccountId || isNaN(sourceAccountId)) {
    return <p>Error: ID de cuenta no válido.</p>;
  }

  return (
    <PageContainer>
      <TransferForm sourceAccountId={sourceAccountId} />
    </PageContainer>
  );
};
