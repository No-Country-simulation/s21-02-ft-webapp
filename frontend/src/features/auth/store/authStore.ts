// 📄 authStore.ts
import { create } from "zustand";
import { AuthState, User } from "../../../types/auth/authTypes";
import {
  getUserFromStorage,
  getTokenFromStorage,
  saveAuthToStorage,
  clearAuthStorage,
} from "./authStorage";
import { getLoggedUser } from "../services/authService";

export const useAuthStore = create<AuthState>((set, get) => ({
  user: getUserFromStorage(),
  token: getTokenFromStorage(),

  login: (user: User, token: string) => {
    saveAuthToStorage(user, token);
    set({ user, token });
  },

  logout: () => {
    clearAuthStorage();
    set({ user: null, token: null });
  },

  isAuthenticated: () => !!get().token,

  fetchUser: async () => {
    try {
      const user = await getLoggedUser();
      set({ user });
    } catch (error) {
      console.error("Error obteniendo usuario:", error);
      set({ user: null, token: null });
    }
  }
}));