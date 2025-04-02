import { Sidebar } from './components/layout/Sidebar';
import { MainContent } from './components/layout/MainContent';
import { DashboardCards } from './features/dashboard/components/DashboardCards';
import { RegisterPage } from './pages/RegisterPage';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuthStore } from './features/auth/store/authStore';
import { LoginPage } from './pages/LoginPage';
import  UserOnline  from './features/auth/components/UserOnlineForm';

function App() {
  const { isAuthenticated } = useAuthStore();

  return (
    <div className="bg-gray-200 min-h-screen">
      {/* Mostrar sidebar solo cuando está autenticado */}
      {isAuthenticated() && <Sidebar />}
      
      <Routes>
        {/* Rutas públicas (accesibles sin autenticación) */}
        <Route path="/login" element={!isAuthenticated() ? <LoginPage /> : <Navigate to="/" replace />} />
        <Route path="/register" element={!isAuthenticated() ? <RegisterPage /> : <Navigate to="/" replace />} />
        
        {/* Rutas protegidas (requieren autenticación) */}
        <Route path="/" element={isAuthenticated() ? (
            <MainContent>
              <DashboardCards />
              <UserOnline />
            </MainContent>
          ) : (
            <Navigate to="/login" replace />)} />
        
        {/* Ruta de registro alternativa (también protegida) */}
        <Route path="/register" element={isAuthenticated() ? (
            <MainContent>
              <RegisterPage />
            </MainContent>
          ) : (
            <Navigate to="/login" replace />)} />
        
        {/* Redirección para rutas no encontradas */}
        <Route path="*" element={isAuthenticated() ? <Navigate to="/" replace /> : <Navigate to="/login" replace />} />
      </Routes>
    </div>
  );
}

export default App;



