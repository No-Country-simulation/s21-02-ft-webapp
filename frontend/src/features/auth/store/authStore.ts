// 📄 authStore.ts
import { create } from "zustand";
import { AuthState } from "../../../types/auth/authTypes";
import {
  getUserFromStorage,
  getTokenFromStorage,
  saveAuthToStorage,
  clearAuthStorage,
} from "./authStorage";

export const useAuthStore = create<AuthState>((set, get) => ({
  user: getUserFromStorage(),
  token: getTokenFromStorage(),

  login: (user, token) => {
    saveAuthToStorage(user, token);
    set({ user, token });
  },

  logout: () => {
    clearAuthStorage();
    set({ user: null, token: null });
  },

  isAuthenticated: () => !!get().token, // Retorna true si hay token, false si no
}));
