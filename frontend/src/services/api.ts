import axios from 'axios';
const API_URL = 'http://localhost:9091/api'; // URL del backend

export const api = axios.create({
  baseURL: API_URL,
});

// Intercepta cada solicitud y agrega el token en las cabeceras
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // Cambiado de 'authToken' a 'token'
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

