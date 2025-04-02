// src/components/layout/MainContent.tsx
import { FaSearch, FaHandHoldingUsd, FaExchangeAlt, FaQrcode } from 'react-icons/fa';

export const MainContent = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className="lg:ml-64 lg:pl-4 flex flex-col mt-5 mx-2">
      {/* Buscador */}
      <div className="bg-white rounded-full border-none p-3 mb-4 shadow-md">
        <div className="flex items-center">
          <FaSearch className="px-3 ml-1 text-gray-400" />
          <input 
            type="text" 
            placeholder="Buscar..." 
            className="ml-3 focus:outline-none w-full bg-transparent"
          />
        </div>
      </div>

      {/* Contenido principal */}
      {children}
    </div>
  );
};