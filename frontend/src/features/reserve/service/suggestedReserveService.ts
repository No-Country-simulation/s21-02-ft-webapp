// src/features/reservation/service/reservationTypeService.ts
import { api } from '../../../services/api';
import { useAuthStore } from '../../auth/store/authStore';
import { SuggestedReserveResponseDTO } from '../../../types/reserve/response';

export const createSuggestedReserve = async (
  name: string,
  icon: File
): Promise<SuggestedReserveResponseDTO> => {
  const token = useAuthStore.getState().token;
  if (!token) {
    throw new Error('No authentication token available');
  }

  const config = {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'multipart/form-data'
    }
  };

  const formData = new FormData();
  formData.append('name', name);
  formData.append('icon', icon);

  try {
    const response = await api.post('/suggestedReserve', formData, config);
    return response.data;
  } catch (error) {
    console.error('Error creating reservation type:', error);
    throw new Error('Failed to create reservation type.');
  }
};

export const getAllSuggestedReserves = async (): Promise<SuggestedReserveResponseDTO[]> => {
  const token = useAuthStore.getState().token;
  if (!token) {
    throw new Error('No authentication token available');
  }

  const config = {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  };

  try {
    const response = await api.get('/suggestedReserve', config);
    return response.data;
  } catch (error) {
    console.error('Error fetching reservation types:', error);
    throw new Error('Failed to fetch reservation types.');
  }
};