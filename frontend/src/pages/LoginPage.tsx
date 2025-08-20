// src/pages/LoginPage.tsx
import { LoginForm } from '../features/auth/components/LoginForm';

export const LoginPage = () => {
  return (
    <div className="min-h-screen bg-gray-100">
      <LoginForm />
    </div>
  );
};