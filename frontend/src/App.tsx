import { Sidebar } from './components/layout/Sidebar';
import { MainContent } from './components/layout/MainContent';
import { DashboardCards } from './features/dashboard/components/DashboardCards';
import { RegisterAccountPage } from './pages/RegisterAccountPage';
import { RegisterUserPage } from './pages/RegisterUserPage';
import { IndexPage } from './pages/IndexPage';
import { Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from './features/auth/store/authStore';
import { LoginPage } from './pages/LoginPage';
// Layout para rutas protegidas que incluye Dashboard
const ProtectedLayout = () => {
  return (
    <MainContent>
      <DashboardCards />
      <Outlet /> {/* Esto renderizará el contenido adicional según la ruta */}
    </MainContent>
  );
};

function App() {
  const { isAuthenticated } = useAuthStore();

  return (
    <div className="bg-gray-200 min-h-">
      {/* Mostrar sidebar solo cuando está autenticado */}
      {isAuthenticated() && <Sidebar />}
      
      <Routes>
        {/* Rutas públicas (accesibles sin autenticación) */}
        <Route path="/login" element={!isAuthenticated() ? <LoginPage /> : <Navigate to="/" replace />} />
        <Route path="/register-user" element={!isAuthenticated() ? <RegisterUserPage /> : <Navigate to="/" replace />} />
        
        {/* Rutas protegidas (requieren autenticación) */}
        <Route element={isAuthenticated() ? <ProtectedLayout /> : <Navigate to="/login" replace />}>
          <Route path="/" element={<IndexPage />} />
          <Route path="/account/create" element={<RegisterAccountPage />} />
          {/* Puedes añadir más rutas aquí que compartan el mismo layout */}
        </Route>
        
        {/* Redirección para rutas no encontradas */}
        <Route path="*" element={isAuthenticated() ? <Navigate to="/" replace /> : <Navigate to="/login" replace />} />
      </Routes>
    </div>
  );
}

export default App;