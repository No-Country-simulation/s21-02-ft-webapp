// 📄 authStorage.ts
export const getUserFromStorage = (): string | null => {
    return localStorage.getItem("user");
  };
  
  export const getTokenFromStorage = (): string | null => {
    return localStorage.getItem("token");
  };
  
  export const saveAuthToStorage = (user: string, token: string) => {
    localStorage.setItem("user", user);
    localStorage.setItem("token", token);
  };
  
  export const clearAuthStorage = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
  };
  