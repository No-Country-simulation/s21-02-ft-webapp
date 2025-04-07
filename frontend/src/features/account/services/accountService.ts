// src/features/account/services/accountService.ts
import { api } from '../../../services/api';
import { AccountResponse } from '../../../types/account/response';
import { useAuthStore } from '../../auth/store/authStore';

export const createAccount = async (currency: string): Promise<AccountResponse> => {
    // Obtener el token de autenticación
    const token = useAuthStore.getState().token;
    
    if (!token) {
        throw new Error('No authentication token available');
    }

    // Configurar los headers con el token de autenticación
    const config = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };

    // Realizar la petición POST con el cuerpo requerido
    const response = await api.post('/accounts', { currency }, config);
    
    return response.data;
};

// Opcional: Función para obtener las cuentas del usuario
export const getAccounts = async (): Promise<AccountResponse[]> => {
    const token = useAuthStore.getState().token;
    
    if (!token) {
        throw new Error('No authentication token available');
    }

    const config = {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    };

    const response = await api.get('/accounts', config);
    return response.data;
};