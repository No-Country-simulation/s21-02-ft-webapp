import { Sidebar } from './components/layout/Sidebar';
import { MainContent } from './components/layout/MainContent';
import { DashboardCards } from './features/dashboard/components/DashboardCards';
import { RegisterAccountPage } from './pages/RegisterAccountPage';
import { TransferPage } from './pages/TransferPage';
import { DepositPage } from './pages/DepositPage';
import { CardUserPage } from './pages/CardPage';
import { RegisterUserPage } from './pages/RegisterUserPage';
import { IndexPage } from './pages/IndexPage';
import { Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { useAuthStore } from './features/auth/store/authStore';
import { LoginPage } from './pages/LoginPage';
import {MovementListPage} from './pages/MovementListPage';
import {TransactionListPage} from './pages/TransactionListPage';
import {ReserveListPage} from './pages/ReserveListPage';
import {ReserveCreateNamePage} from './pages/ReserveCreateNamePage';
import {ReserveCreateAmountPage} from './pages/ReserveCreateAmountPage';
import {DolarQuotesPage} from './pages/DolarQuotesPage';
import { DolarQuotes } from "./features/dolar/components/DolarQuotes";
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
          <Route path="/account/transfer" element={<TransferPage />} />
          <Route path="/account/deposit" element={<DepositPage />} />
          <Route path="/cards" element={<CardUserPage />} />
          <Route path="/account/:accountId/transactions" element={<TransactionListPage />} />
          <Route path="/account/:accountId/movements" element={<MovementListPage />} />
          <Route path="/account/:accountId/reservations" element={<ReserveListPage />} />
          <Route path="/reservations/create/name" element={<ReserveCreateNamePage />} />
          <Route path="/reservations/create/amount" element={<ReserveCreateAmountPage />} />
          <Route path="/dolar" element={<DolarQuotesPage />} />
        </Route>
        
        {/* Redirección para rutas no encontradas */}
        <Route path="*" element={isAuthenticated() ? <Navigate to="/" replace /> : <Navigate to="/login" replace />} />
      </Routes>
    </div>
  );
}

export default App;