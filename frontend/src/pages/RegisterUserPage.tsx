// src/pages/LoginPage.tsx
import { RegisterForm } from '../features/auth/components/RegisterForm';
import { PageContainer } from '../components/ui/PageContainer';

export const RegisterUserPage = () => {
  return (
    <PageContainer>
      <RegisterForm />
    </PageContainer>
  );
};