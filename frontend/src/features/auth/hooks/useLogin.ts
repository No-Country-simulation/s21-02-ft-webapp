// src/hooks/useLogin.ts
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../auth/store/authStore';
import { login as loginService } from '../services/authService';
import { LoginHookResponse } from '../../../types/auth/response';

export const useLogin = (): LoginHookResponse  => {
  const navigate = useNavigate();
  const { login } = useAuthStore();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');
    
    try {
      const { user, token } = await loginService({email, password});
      login(user, token);
      navigate('/dashboard');
    } catch (err) {
      setError('Credenciales incorrectas');
      console.error('Login error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRegisterRedirect = () => {
    navigate('/register');
  };

  return {
    credentials: { email, password },
    status: { error, isLoading },
    actions: {
      setEmail,
      setPassword,
      handleLogin,
      handleRegisterRedirect
    }
  };
};

