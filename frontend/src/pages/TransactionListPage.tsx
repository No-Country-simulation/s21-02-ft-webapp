import { PageContainer } from '../components/ui/PageContainer';
import { AccountTransactionsContainer } from '../features/account/components/AccountTransactionsContainer';

export const TransactionListPage = () => {
  return (
    <PageContainer>
      <AccountTransactionsContainer />
    </PageContainer>
  );
};
