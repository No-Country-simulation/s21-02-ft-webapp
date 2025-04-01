// src/App.tsx
import { Routes, Route } from 'react-router-dom'; // Importa Routes y Route que sirven para manejar las rutas
import { Navbar } from './components/layouts/Navbar'; // Importa el componente Navbar
import { LoginPage as Login } from './pages/LoginPage';
import { Dashboard } from './pages/DashboardPage';
import { RegisterPage } from './pages/RegisterPage';
import { Home } from './pages/Home';
import  UserOnline  from './components/forms/UserOnline';

// Define la función App que devuelve el componente principal de la aplicación
function App() {
  return (
    <div className="min-h-screen bg-gray-100">
      <Navbar />
      <main className="container mx-auto p-4">
        <Routes>
          <Route path="/" element={<Home />} /> 
          <Route path="/login" element={<Login />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/registerUser" element={<RegisterPage />} />
          <Route path="/userOnline" element={<UserOnline />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;