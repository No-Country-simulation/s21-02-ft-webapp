// Implementación básica de encriptación (deberías usar una librería como crypto-js en producción)
export const encryptData = (data: string): string => {
    // En un entorno real, usa una implementación segura como AES
    return btoa(data); // Solo para demostración, NO usar en producción
  };