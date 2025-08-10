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

    // Configurar los headers con el token de autenticació
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

    try {
        const response = await api.get('/accounts', config);
        return response.data;
    } catch (error: any) {
        // Si la respuesta es 404, retornamos arreglo vacío en vez de lanzar error
        if (error.response && error.response.status === 404) {
            return [];
        }
        // Para otros errores, relanzamos la excepción para que se maneje arriba
        throw error;
    }
};

export const getCurrencyTypeAccounts = async (): Promise<string[]> => {
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
        const response = await api.get('/accounts/currencies', config);
        return response.data;
    } catch (error) {
        console.error('Error fetching currency types:', error);
        throw new Error('No se pudieron cargar los tipos de moneda disponibles');
    }
};