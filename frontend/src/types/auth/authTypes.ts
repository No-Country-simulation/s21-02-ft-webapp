export interface AuthState {
    user: string | null;
    token: string | null;
    login: (user: string, token: string) => void;
    logout: () => void;
    isAuthenticated: () => boolean;
  }
  
  export interface Account {
    id: number;
    currency: string;
  }
  
  export interface User {
    id: number;
    name: string;
    email: string;
    accounts: Account[];
    // otros campos que manejes
  }