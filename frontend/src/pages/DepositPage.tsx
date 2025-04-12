// src/pages/TransferPage.tsx
import { DepositForm } from '../features/account/components/DepositForm';
import { PageContainer } from '../components/ui/PageContainer';

export const DepositPage = () => {
  return (
    <PageContainer>
      <DepositForm />
    </PageContainer>
  );
};