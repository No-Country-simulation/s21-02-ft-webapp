import { useEffect, useState } from 'react';
import { useCard } from '../hooks/useCards';
import { FaEye, FaEyeSlash, FaMicrochip, FaCcVisa } from 'react-icons/fa6';
import { useAuthStore } from '../../../features/auth/store/authStore';

export const CardList = () => {
  const { user } = useAuthStore();
  const { cards, isLoading, error, fetchCards } = useCard();
  const [visibleCvv, setVisibleCvv] = useState<{ [key: string]: boolean }>({});

  useEffect(() => {
    fetchCards();
  }, [fetchCards]);

  const toggleCvv = (encryptedNumber: string) => {
    setVisibleCvv(prev => ({
      ...prev,
      [encryptedNumber]: !prev[encryptedNumber]
    }));
  };

  if (isLoading) return <div className="text-center text-gray-500">Cargando tarjetas...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {cards.map((card) => (
        <div
        key={card.encryptedNumber}
        className="relative rounded-2xl overflow-hidden p-5 shadow-xl bg-gradient-to-r from-cyan-500 to-cyan-700 text-white flex flex-col justify-between h-60 sm:h-64 md:h-72"
      >
        {/* Banco */}
        <div className="flex justify-between items-center">
          <h3 className="text-lg sm:text-xl font-bold">{card.issuingBank}</h3>
          <FaCcVisa className="text-white text-3xl sm:text-4xl" />
        </div>
      
        {/* Chip + Internacional */}
        <div className="flex items-center mt-2 gap-3">
          <FaMicrochip className="text-white text-2xl sm:text-3xl" />
          <span className="text-sm sm:text-base font-medium">Internacional</span>
        </div>
      
        {/* Número de tarjeta */}
        <div className="text-lg sm:text-2xl tracking-widest font-mono mt-3">
          {card.encryptedNumber.replace(/(.{4})/g, '$1 ')}
        </div>
      
       
        <div className="text-xs sm:text-sm">
            <p className="text-cyan-100">VENCE</p>
            <p>{card.expirationDate}</p>
          </div>
        {/* Fecha + CVV */}
        <div className="flex justify-between items-center mt-3">
         {/* Nombre */}
         <div className="text-sm sm:text-base font-semibold mt-1 tracking-wide">
          {user?.fullName.toUpperCase()}
        </div>
      
          {/* Botón ver/ocultar CVV */}
          <button
            onClick={() => toggleCvv(card.encryptedNumber)}
            className="flex items-center text-xs bg-white text-cyan-600 px-2 py-1 rounded-full hover:bg-cyan-50 transition"
          >
            {visibleCvv[card.encryptedNumber] ? (
              <>
                <FaEyeSlash className="mr-1" />
                Ocultar
              </>
            ) : (
              <>
                <FaEye className="m-1" />
                Ver CVV
              </>
            )}
          </button>
        </div>
      
        {/* CVV visible */}
        {visibleCvv[card.encryptedNumber] && (
          <div className="absolute bottom-5 right-25 p-2 bg-white text-cyan-700 rounded text-xs font-mono shadow">
            CVV: {card.encryptedCvv}
          </div>
        )}
      </div>
      
      ))}
    </div>
  );
};
