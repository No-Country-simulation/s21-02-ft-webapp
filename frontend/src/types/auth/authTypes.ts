export interface AuthState {
    user: string | null;
    token: string | null;
    login: (user: string, token: string) => void;
    logout: () => void;
    isAuthenticated: () => boolean;
  }
  