// 📄 types/auth/authTypes.ts
export interface AuthState {
  user: User | null;
  token: string | null;
  login: (userData: User, token: string) => void;
  logout: () => void;
  isAuthenticated: () => boolean;
}

export interface Account {
  id: number;
  currency: string;
}

export interface User {
  id?: number;
  fullName: string;
  email: string;
  accounts?: Account[];
}