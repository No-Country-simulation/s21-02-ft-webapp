// src/App.tsx
import { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import { Navbar } from './components/shared/Navbar';
import { LoginPage } from './pages/LoginPage';
/* import { DashboardPage } from './pages/DashboardPage';
import { HomePage } from './pages/HomePage'; */

function App() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar isMenuOpen={isMenuOpen} setIsMenuOpen={setIsMenuOpen} />
      <main className={`transition-all duration-300 ${isMenuOpen ? 'md:ml-64' : 'ml-0'} pt-16`}>
        <div className="p-4">
          <Routes>
            {/* <Route path="/" element={<HomePage />} /> */}
            <Route path="/login" element={<LoginPage />} />
          {/*   <Route path="/dashboard" element={<DashboardPage />} /> */}
          </Routes>
        </div>
      </main>
    </div>
  );
}

export default App;