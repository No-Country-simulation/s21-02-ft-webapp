import { useState, useEffect } from 'react';
import { getLoggedUser } from '../services/authService';
import { useAuthStore } from '../store/authStore';
import { LoggedUserResponse } from '../../../types/auth/response';

export const useUserOnline = () => {
  const [user, setUser] = useState<LoggedUserResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { token } = useAuthStore();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        setLoading(true);
        setError(null);
        
        if (!token) {
          throw new Error('No autenticado');
        }

        const loggedUser = await getLoggedUser();
        setUser(loggedUser);
      } catch (err) {
        console.error('Error al obtener usuario:', err);
        setError(err instanceof Error ? err.message : 'Error desconocido');
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [token]);

  return {
    user,
    loading,
    error,
    token
  };
};