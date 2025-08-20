export interface RegisterRequest {
    fullName: string;
    dni: string;
    email: string;
    phoneNumber: string;
    password: string;
  }
  
  export interface LoginRequest {
    email: string;
    password: string;
  }