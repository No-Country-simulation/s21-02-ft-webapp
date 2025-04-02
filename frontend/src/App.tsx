// src/App.tsx
import { Sidebar } from './components/layout/Sidebar';
import { MainContent } from './components/layout/MainContent';
import { DashboardCards } from './features/dashboard/components/DashboardCards';
import { TransactionsTable } from './features/transaction/TransactionsTable';
import { useAuthStore } from './features/auth/store/authStore';

function App() {
  const { isAuthenticated } = useAuthStore();
  
  return (
    <div className="bg-gray-200 min-h-screen">
      <Sidebar />
      {isAuthenticated() && (
        <MainContent>
          <DashboardCards />
          <TransactionsTable/>
        </MainContent>
      )}
    </div>
  );
}

export default App;