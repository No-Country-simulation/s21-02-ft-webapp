import { useState } from "react"

const ConfirmationCard = (Username, UserDestination, amount, CBU) =>{

    const [userData] = useState({
        transaction_id: 458463531235454131,
        userName: "Julio Tricerri",
        UserDestination: "Francisco Ortega",
        amount: 500.00,
        CBU : 11154865321841325000,
        Reason : "Pago de servicios",
        status: "Terminada",
    })


    return(
        <div className="flex justify-center">
            <div className="flex flex-col w-full max-w-md space-y-6 space rounded-xl bg-BlackBlue p-8 text-WhiteBlack">
                <h1 className="text-3xl font-bold">Verifica si todo esta bien.</h1>
                <span>Monto a transferir: ${userData.amount}.00 </span>
                
                <span>Destinatario:  {userData.UserDestination}</span>
                <span>CBU: {userData.CBU}</span>
                <span>Motivo:            {userData.Reason}</span>
                <button className="w-3/4 self-center bg-LightGolden text-BlackBlue py-1 px-2 rounded-xl cursor-pointer hover:bg-Gray hover:text-LightGolden transition duration-500">Transferir</button>

                <span>!No es cancelable</span>
            
            </div>  
        </div>
    )
}



export default ConfirmationCard