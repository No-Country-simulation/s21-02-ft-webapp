export interface RegisterResponse {
    token: string;
    user: {
      fullName: string,
      email: string
    }
  }
  
  export interface LoginResponse {
      token: string;
      user: {
        fullName: string,
        email: string
      }
  }

  export type LoggedUserResponse = {
    id: number;
    fullName: string;
    dni: string;
    email: string;
    phoneNumber: string;
    createdAt: string;
    updatedAt: string;
    active: boolean;
  };

  export interface LoginHookResponse {
    credentials: {
      email: string;
      password: string;
    };
    status: {
      error: string;
      isLoading: boolean;
    };
    actions: {
      setEmail: (value: string) => void;
      setPassword: (value: string) => void;
      handleLogin: (e: React.FormEvent) => Promise<void>;
      handleRegisterRedirect: () => void;
    };
  }