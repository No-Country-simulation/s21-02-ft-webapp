import { useNavigate } from "react-router-dom"
import Toastify from "toastify-js"; 
import { AuthUserService } from "../../Services/AuthUserService";
import { useContext, useState } from "react";
import { AuthContext } from "../../Context/AuthContext";


const Uselogin = () =>{
    const navigate = useNavigate();
    const {LoginUserService} = AuthUserService();
    const {login} = useContext(AuthContext)
    const UseloginUser = async(email, password) =>{

        try{
            const response = await LoginUserService(email, password)
            
             if (response.status == 200){
                const data  = await response.data;
                
                login(data);
                
                navigate('/account')
            
                return;
             }
             

        }catch(error){
            if(error.status == 401){
                return 'Usuario o contraseña incorrectas';
            }

            if(error.status == 500){
                return 'Error en el sisetma';
            }
        }

    }



    return {UseloginUser}
}

export default Uselogin;
