import {api} from '../../../services/api';
import {LoginRequest, RegisterRequest} from '../../../types/auth/request';
import {RegisterResponse, LoginResponse} from '../../../types/auth/response';

export const login = async (data: LoginRequest): Promise<LoginResponse> => {
  const response = await api.post('/auth/login', data);
  return {
    token: response.data.token,
    user: response.data.fullName // Usamos fullName como user
  };
}; // Actualiza la interfaz de retorno

// Servicio para cerrar sesión
export const logout = async (): Promise<void> => {
    await api.post('/auth/logout'); // Llama al backend para invalidar el token
    localStorage.removeItem('authToken'); // Elimina el token del navegador local 
  };  
    
  // Actualiza la interfaz de retorno para el registro
// Usa la interfaz RegisterRequest en lugar de parámetros individuales
export const register = async (data: RegisterRequest): Promise<RegisterResponse> => {
  const response = await api.post('/auth/register', data);
  return {
      token: response.data.token,
      user: response.data.fullName // Devuelve un RegisterResponse
  };
};