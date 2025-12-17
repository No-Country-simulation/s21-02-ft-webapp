// 📄 authStorage.ts
import { User } from "../../../types/auth/authTypes";
export const getUserFromStorage = (): User | null => {
  const user = localStorage.getItem("user");
  return user ? JSON.parse(user) : null;
};
  
  export const getTokenFromStorage = (): string | null => {
    return localStorage.getItem("token");
  };
  
  export const saveAuthToStorage = (user: User, token: string) => {
    localStorage.setItem("user", JSON.stringify(user));
    localStorage.setItem("token", token);
  };
  export const clearAuthStorage = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
  };
  