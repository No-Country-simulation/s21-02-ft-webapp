import { useEffect, useState } from 'react';
import { useCard } from '../hooks/useCards';
import { FaEye, FaEyeSlash, FaMicrochip, FaCcVisa } from 'react-icons/fa6';

export const CardList = () => {
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
          className="bg-gradient-to-r from-cyan-400 to-cyan-600 text-white rounded-2xl p-5 shadow-xl relative overflow-hidden h-56 flex flex-col justify-between"
        >
          {/* Banco */}
          <h3 className="text-xl font-bold">{card.issuingBank}</h3>

          {/* Chip */}
          <div className="mt-2">
            <FaMicrochip className="text-white text-3xl" />
          </div>

          {/* Número de tarjeta */}
          <div className="text-2xl tracking-widest font-mono mt-3">
            {card.encryptedNumber.replace(/(.{4})/g, '$1 ')}
          </div>

          {/* Parte inferior: fecha + logo */}
          <div className="flex justify-between items-center mt-3">
            <div className="text-sm">
              <p className="text-cyan-100">Vence</p>
              <p>{card.expirationDate}</p>
            </div>
            <FaCcVisa className="text-white text-4xl" />
          </div>

          {/* Botón CVV */}
          <div className="mt-2 flex gap-2">
            <button
              onClick={() => toggleCvv(card.encryptedNumber)}
              className="flex items-center text-xs bg-white text-cyan-600 px-3 py-1 rounded-full hover:bg-cyan-50 transition"
            >
              {visibleCvv[card.encryptedNumber] ? (
                <>
                  <FaEyeSlash className="mr-1" />
                  Ocultar CVV
                </>
              ) : (
                <>
                  <FaEye className="mr-1" />
                  Ver CVV
                </>
              )}
            </button>
          </div>

          {/* CVV visible */}
          {visibleCvv[card.encryptedNumber] && (
            <div className="absolute top-3 right-4 p-2 bg-white text-cyan-700 rounded text-sm font-mono">
              CVV: {card.encryptedCvv}
            </div>
          )}
        </div>
      ))}
    </div>
  );
};
