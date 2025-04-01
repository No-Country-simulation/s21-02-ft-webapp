import { useLogin } from '../hooks/useLogin';
import { useAuthStore } from '../../auth/store/authStore';
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

  if (isAuthenticated()) {
    return (
      <div className="text-center space-y-4">
        <p className="text-gray-600">Ya has iniciado sesión</p>
        <Button variant="danger" onClick={logout}>
          Cerrar sesión
        </Button>
      </div>
    );
  }

  return (
    <form onSubmit={handleLogin} className="space-y-4">
      {error && <Alert message={error} variant="error" />}
      
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

      <Button type="submit" isLoading={isLoading} fullWidth>
        Iniciar sesión
      </Button>

      <Button
        type="button"
        variant="secondary"
        onClick={handleRegisterRedirect}
        disabled={isLoading}
        fullWidth
      >
        Registrarse
      </Button>
    </form>
  );
};