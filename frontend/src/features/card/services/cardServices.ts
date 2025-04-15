import { api } from '../../../services/api';
import { CardRequest } from '../../../types/card/request';
import { CardResponse } from '../../../types/card/response';
import { useAuthStore } from '../../../features/auth/store/authStore';

export const createCard = async (cardData: CardRequest): Promise<CardResponse> => {
  const token = useAuthStore.getState().token;
  if (!token) throw new Error('No authentication token available');
try{
  const response = await api.post('/cards/register', cardData, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  return response.data;
  } catch (error: any) {
    if (error.response) {
      // Extrae el mensaje del error 422 del backend
      if (error.response.status === 422) {
        throw new Error(error.response.data.message);
      }
      // Maneja otros códigos de error
      throw new Error(error.response.data.message || 'Error al procesar la transferencia');
    }
    throw new Error('Error de conexión con el servidor');
  }
};

export const getCards = async (): Promise<CardResponse[]> => {
  const token = useAuthStore.getState().token;
  if (!token) throw new Error('No authentication token available');

  try {
  const response = await api.get('/cards', {
    headers: {
      'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
    }
  });
  return response.data;
  } catch (error: any) {
    if (error.response) {
      // Extrae el mensaje del error 422 del backend
      if (error.response.status === 422) {
        throw new Error(error.response.data.message);
      }
      // Maneja otros códigos de error
      throw new Error(error.response.data.message || 'Error al procesar la transferencia');
    }
    throw new Error('Error de conexión con el servidor');
  }
  
};
