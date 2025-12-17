// src/pages/LoginPage.tsx
import { LoginForm } from '../features/auth/components/LoginForm';
import { PageContainer } from '../components/ui/PageContainer';
export const LoginPage = () => {
  return (
    <PageContainer>
      <LoginForm />
    </PageContainer>
  );
};