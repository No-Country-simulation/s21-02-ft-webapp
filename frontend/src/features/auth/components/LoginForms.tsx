// src/features/auth/components/LoginForm.tsx
import { useLogin } from '../../auth/hooks/useLogin';
import { useAuthStore } from '../store/authStore';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { Alert } from '../../../components/ui/Alert';

export const LoginForm = () => {
  const {
    credentials: { email, password },
    actions: { setEmail, setPassword, handleLogin, handleRegisterRedirect },
    status: { error, isLoading },
  } = useLogin();

  const { isAuthenticated, logout } = useAuthStore();

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">
        {isAuthenticated() ? 'Mi Cuenta' : 'Iniciar Sesión'}
      </h2>
      
      {isAuthenticated() ? (
        <div className="text-center space-y-4">
          <p className="text-gray-600">Ya has iniciado sesión</p>
          <Button 
            variant="danger" 
            onClick={logout}
            fullWidth
          >
            Cerrar sesión
          </Button>
        </div>
      ) : (
        <>
          {error && <Alert message={error} className="mb-4" />}
          
          <form onSubmit={handleLogin} className="space-y-4">
            <Input
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              disabled={isLoading}
            />

            <Input
              label="Contraseña"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={isLoading}
            />

            <Button 
              type="submit" 
              isLoading={isLoading}
              fullWidth
            >
              Iniciar sesión
            </Button>

            <Button
              type="button"
              variant="secondary"
              onClick={handleRegisterRedirect}
              disabled={isLoading}
              fullWidth
            >
              Ir al registro
            </Button>
          </form>
        </>
      )}
    </div>
  );
};