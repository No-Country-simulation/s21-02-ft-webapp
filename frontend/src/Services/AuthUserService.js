import api from "./api";

export const AuthUserService = () => {


    const  LoginUserService = async(email, password) =>{
        try {
            const formData = {
                email: email,
                password:  password
            };
    
         
             const response = await api.post("api/auth/login", formData);
             return response

        } catch (error) {
          
            throw error;
        }

    }


    const RegisterUserService = async(FullName, Dni, Email, Telephone, Password)=>{
        const formData = 
        {
            fullName: FullName,
            dni: Dni,
            email: Email,
            phoneNumber: Telephone,
            password: Password 
          }

        try {

             const response = await api.post("api/auth/register", formData);

             
             return response

        } catch (error) {
          
            throw error;
        }


    }
    return {LoginUserService, RegisterUserService }
};
