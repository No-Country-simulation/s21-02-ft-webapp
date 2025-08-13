// src/features/reservation/components/ReservationTypeForm.tsx

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createReservationType } from "../../service/reservationTypeService";
import { useReservationTypes } from '../../hook/useReservationTypes';
import { ReservationTypeResponseDTO } from '../../../../types/reserve/response'; // Make sure to import this

// The rest of the imports and state management remain the same...

export const ReservationTypeForm = () => {
  const [name, setName] = useState('');
  const [customIcon, setCustomIcon] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const { types: suggestedTypes, loading: typesLoading, error: typesError } = useReservationTypes();

  const handleSuggestionClick = (typeName: string) => {
    setName(typeName);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name || !customIcon) {
      setError('Por favor, completa todos los campos.');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await createReservationType(name, customIcon);
      navigate('/');
    } catch (err: any) {
      setError(err.message || 'Error al crear el tipo de reserva.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-lg mx-auto p-6 bg-white rounded-lg shadow-md mt-8">
      <h2 className="text-2xl font-semibold text-gray-800 mb-6">¿Cuál es el nombre de esta reserva?</h2>
      {error && <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">{error}</div>}

      <div className="mb-6">
        <input
          type="text"
          id="name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="shadow-sm appearance-none border-b-2 border-gray-300 w-full py-3 px-1 text-gray-700 leading-tight focus:outline-none focus:border-blue-500 text-2xl font-bold"
          placeholder="Ingresá un nombre"
        />
      </div>

      <div className="mt-8">
        <h3 className="text-lg font-semibold text-gray-700 mb-4">Sugerencias</h3>
        {typesLoading && <div className="text-gray-500">Cargando sugerencias...</div>}
        {typesError && <div className="text-red-500">Error al cargar sugerencias.</div>}
        
        <div className="flex flex-wrap justify-between items-center space-x-2">
          {/* Corrected map function */}
          {suggestedTypes.map((type: ReservationTypeResponseDTO) => (
            <SuggestionItem 
              key={type.id} 
              iconUrl={type.iconUrl} 
              name={type.name} 
              onSelect={() => handleSuggestionClick(type.name)} 
            />
          ))}
        </div>
      </div>
      
      <div className="mt-6">
        <label htmlFor="custom-icon" className="block text-gray-700 text-sm font-bold mb-2">
          Subir ícono (opcional)
        </label>
        <input
          type="file"
          id="custom-icon"
          accept="image/*"
          onChange={(e) => setCustomIcon(e.target.files ? e.target.files[0] : null)}
          className="shadow-sm appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
        />
      </div>

      <button
        type="submit"
        onClick={handleSubmit}
        disabled={loading || !name}
        className={`mt-10 w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg focus:outline-none focus:shadow-outline transition-colors ${
          loading || !name ? 'opacity-50 cursor-not-allowed' : ''
        }`}
      >
        {loading ? 'Creando...' : 'Continuar'}
      </button>
    </div>
  );
};

// Componente auxiliar para las sugerencias
const SuggestionItem = ({ iconUrl, name, onSelect }: { iconUrl: string; name: string; onSelect: () => void }) => (
  <div onClick={onSelect} className="flex flex-col items-center cursor-pointer rounded-lg p-3 hover:bg-gray-100 transition-colors">
    <img src={iconUrl} alt={name} className="w-12 h-12 mb-2" />
    <span className="text-sm font-medium text-gray-700">{name}</span>
  </div>
);