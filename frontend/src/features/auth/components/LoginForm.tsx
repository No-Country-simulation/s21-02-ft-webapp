import { useLogin } from '../hooks/useLogin';
import { useAuthStore } from '../store/authStore';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { Alert } from '../../../components/ui/Alert';
import { Card } from '../../../components/ui/Card';
import { Link } from 'react-router-dom';
import { useState } from 'react';

// Tipos para las credenciales predefinidas
type DemoAccount = {
  email: string;
  password: string;
  label: string;
  description: string;
  color: 'blue' | 'green' | 'purple';
};

export const LoginForm = () => {
  const {
    credentials: { email, password },
    actions: { setEmail, setPassword, handleLogin, handleRegisterRedirect },
    status: { error, isLoading },
  } = useLogin();

  const { isAuthenticated, logout } = useAuthStore();
  const [showDemoAccounts, setShowDemoAccounts] = useState(false);

  // Credenciales predefinidas para demo
  const demoAccounts: DemoAccount[] = [
    {
      email: 'sebastian.tournier11@gmail.com',
      password: 'password123',
      label: 'Cuenta Principal',
      description: 'Usuario emisor (para operaciones)',
      color: 'blue'
    },
    {
      email: 'gusti.paz11@gmail.com',
      password: 'password123',
      label: 'Cuenta Destinatario',
      description: 'Usuario receptor (para transferencias)',
      color: 'green'
    }
  ];

  // Función para cargar credenciales automáticamente
  const loadDemoCredentials = (account: DemoAccount) => {
    setEmail(account.email);
    setPassword(account.password);
    setShowDemoAccounts(false);
  };

  // Función para alternar entre las dos cuentas rápidamente
  const toggleBetweenAccounts = () => {
    const currentAccount = demoAccounts.find(acc => acc.email === email);
    const otherAccount = demoAccounts.find(acc => acc.email !== email) || demoAccounts[0];
    loadDemoCredentials(otherAccount);
  };

  if (isAuthenticated()) {
    return (
      <Card>
        <div className="text-center space-y-4">
          <p className="text-gray-600">Ya has iniciado sesión</p>
          <Button variant="danger" onClick={logout} fullWidth>
            Cerrar sesión
          </Button>
        </div>
        <Link to="/" className="block">
          <Button variant="outline" fullWidth>
            Volver al inicio
          </Button>
        </Link>
      </Card>
    );
  }

  return (
    <div className="flex items-center justify-center min-h-screen p-4">
      <Card className="w-full max-w-lg">
        <div className="text-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Iniciar sesión</h2>
          <p className="text-gray-600 mt-2">Ingresa tus credenciales para continuar</p>
          
          {/* Botón para mostrar/ocultar cuentas demo */}
          <div className="mt-3">
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => setShowDemoAccounts(!showDemoAccounts)}
              className="text-xs"
            >
              {showDemoAccounts ? 'Ocultar cuentas demo' : 'Mostrar cuentas demo'}
            </Button>
            
            {/* Botón para alternar entre cuentas si ya hay un email */}
            {email && (
              <Button
                type="button"
                variant="outline"
                size="sm"
                onClick={toggleBetweenAccounts}
                className="text-xs ml-2"
              >
                Alternar cuenta
              </Button>
            )}
          </div>
        </div>

        {/* Panel de cuentas demo */}
        {showDemoAccounts && (
          <div className="mb-6 p-4 bg-gray-50 rounded-lg border border-gray-200">
            <h3 className="text-sm font-semibold text-gray-700 mb-3">Cuentas de demostración</h3>
            <div className="space-y-3">
              {demoAccounts.map((account, index) => (
                <div 
                  key={index}
                  className={`p-3 rounded-lg border cursor-pointer transition-all hover:scale-[1.02] ${
                    email === account.email 
                    ? 'border-cyan-500 bg-cyan-50' 
                    : 'border-gray-200 bg-white hover:border-gray-300'
                  }`}
                  onClick={() => loadDemoCredentials(account)}
                >
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="flex items-center">
                        <div className={`w-3 h-3 rounded-full mr-2 ${
                          account.color === 'blue' ? 'bg-blue-500' :
                          account.color === 'green' ? 'bg-green-500' :
                          'bg-purple-500'
                        }`} />
                        <span className="font-medium text-gray-800">{account.label}</span>
                      </div>
                      <p className="text-sm text-gray-600 mt-1">{account.description}</p>
                    </div>
                    <div className="text-right">
                      <div className="text-xs font-mono bg-gray-100 px-2 py-1 rounded">
                        {account.email}
                      </div>
                      <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          loadDemoCredentials(account);
                        }}
                        className="mt-2 text-xs text-cyan-600 hover:text-cyan-700 font-medium"
                      >
                        Usar esta cuenta
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <p className="text-xs text-gray-500 mt-3 text-center">
              Estas credenciales son para fines de demostración
            </p>
          </div>
        )}

        {error && <Alert message={error} variant="error" />}

        <form onSubmit={handleLogin} className="space-y-4">
          <div className="relative">
            <Input
              label="Email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              disabled={isLoading}
              placeholder="tu@email.com"
            />
            {/* Indicador visual cuando es una cuenta demo */}
            {demoAccounts.some(acc => acc.email === email) && (
              <div className="absolute right-2 top-9">
                <span className="text-xs bg-cyan-100 text-cyan-800 px-2 py-1 rounded">
                  DEMO
                </span>
              </div>
            )}
          </div>

          <div className="relative">
            <Input
              label="Contraseña"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={isLoading}
              placeholder="••••••••"
            />
            {/* Mostrar contraseña en texto cuando es demo (opcional) */}
            {demoAccounts.some(acc => acc.email === email && acc.password === password) && (
              <div className="absolute right-2 top-9">
                <button
                  type="button"
                  onClick={() => {
                    const currentAccount = demoAccounts.find(acc => acc.email === email);
                    if (currentAccount) {
                      // Solo para debugging, podrías remover esto en producción
                      console.log('Contraseña demo:', currentAccount.password);
                    }
                  }}
                  className="text-xs text-gray-500 hover:text-gray-700"
                  title="Ver contraseña (solo demo)"
                >
                  🔒
                </button>
              </div>
            )}
          </div>

          <div className="flex items-center justify-between mb-2">
            <div className="flex items-center">
              <input
                type="checkbox"
                id="remember"
                className="h-4 w-4 text-cyan-600 rounded"
              />
              <label htmlFor="remember" className="ml-2 text-sm text-gray-600">
                Recordarme
              </label>
            </div>
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
            className="relative"
          >
            {isLoading ? 'Iniciando sesión...' : 'Iniciar sesión'}
            {demoAccounts.some(acc => acc.email === email) && (
              <span className="absolute -top-2 -right-2 bg-yellow-400 text-yellow-900 text-xs px-2 py-1 rounded-full animate-pulse">
                DEMO
              </span>
            )}
          </Button>

          {/* Quick login buttons */}
          <div className="flex space-x-2">
            {demoAccounts.map((account, index) => (
              <Button
                key={index}
                type="button"
                variant="outline"
                onClick={() => loadDemoCredentials(account)}
                className="flex-1 text-sm"
                disabled={isLoading}
              >
                {account.label}
              </Button>
            ))}
          </div>

          <div className="text-center text-sm text-gray-600 mt-4">
            ¿No tienes cuenta?{' '}
            <Link
              to="/register-user"
              className="text-cyan-600 font-medium hover:underline"
              onClick={handleRegisterRedirect}
            >
              Regístrate
            </Link>
          </div>
        </form>

        {/* Información para evaluadores */}
        <div className="mt-6 p-3 bg-blue-50 border border-blue-200 rounded-lg">
          <div className="flex items-start">
            <div className="flex-shrink-0">
              <span className="text-blue-500">💡</span>
            </div>
            <div className="ml-3">
              <h4 className="text-sm font-medium text-blue-800">Para evaluadores</h4>
              <p className="text-xs text-blue-700 mt-1">
                Usa las cuentas de demostración para probar funcionalidades como:
                transferencias entre usuarios, historial de transacciones, y más.
                Todas las operaciones son en un entorno simulado.
              </p>
            </div>
          </div>
        </div>
      </Card>
    </div>
  );
};