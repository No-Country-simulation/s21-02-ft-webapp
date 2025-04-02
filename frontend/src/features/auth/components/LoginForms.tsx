import { useLogin } from '../hooks/useLogin';
import { useAuthStore } from '../../auth/store/authStore';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { Alert } from '../../../components/ui/Alert';
import { Card } from '../../../components/ui/Card';
import { Link } from 'react-router-dom';

export const LoginForm = () => {
  const {
    credentials: { email, password },
    actions: { setEmail, setPassword, handleLogin, handleRegisterRedirect },
    status: { error, isLoading },
  } = useLogin();

  const { isAuthenticated, logout } = useAuthStore();

  if (isAuthenticated()) {
    return (
      <Card>
        <div className="text-center space-y-4">
          <p className="text-gray-600">Ya has iniciado sesión</p>
          <Button variant="danger" onClick={logout} fullWidth>
            Cerrar sesión
          </Button>
        </div>
      </Card>
    );
  }

  return (
    <Card className="w-full max-w-md">
      <div className="text-center mb-6">
        <h2 className="text-2xl font-bold text-gray-800">Iniciar sesión</h2>
        <p className="text-gray-600 mt-2">Ingresa tus credenciales para continuar</p>
      </div>

      {error && <Alert message={error} variant="error" />}
      
      <form onSubmit={handleLogin} className="space-y-4">
        <Input
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          disabled={isLoading}
          placeholder="tu@email.com"
        />

        <Input
          label="Contraseña"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          disabled={isLoading}
          placeholder="••••••••"
        />

        <div className="flex items-center justify-end mb-2">
          <Link 
            to="/forgot-password" 
            className="text-sm text-cyan-600 hover:underline"
          >
            ¿Olvidaste tu contraseña?
          </Link>
        </div>

        <Button 
          type="submit" 
          variant="gradient" 
          isLoading={isLoading} 
          fullWidth
        >
          Iniciar sesión
        </Button>

        <div className="text-center text-sm text-gray-600 mt-4">
          ¿No tienes cuenta?{' '}
          <Link 
            to="/register" 
            className="text-cyan-600 font-medium hover:underline"
            onClick={handleRegisterRedirect}
          >
            Regístrate
          </Link>
        </div>
      </form>
    </Card>
  );
};