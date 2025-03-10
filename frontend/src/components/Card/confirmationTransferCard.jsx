import { useState } from "react"

const ConfirmationCard = (Username, UserDestination, amount, destinationCbu, sourceCbu) =>{

    const [userData] = useState({
        transaction_id: 458463531235454131,
        userName: "Julio Tricerri",
        UserDestination: "Francisco Ortega",
        amount: 500.00,
        sourceCbu: "CBU000000351900000048199272",
        destinationCbu : "CBU000000000000000000000002",
        Reason : "Pago de servicios",
        status: "Terminada",
    })

    {/*Gaston: Esta funcion sirve para terminar de hacer la transaccion, falta conectar con el Back y hacer los ajustes correspondientes */}
    const handleSubmit = () => {
        console.log("Datos de la transacción:", userData);
      };


    return(
        <div className="flex justify-center">
            <div className="flex flex-col w-full max-w-md space-y-6 space rounded-xl bg-BlackBlue p-8 text-WhiteBlack">
                <h1 className="text-3xl font-bold">Verifica si todo esta bien</h1>
                <span className="flex flex-row gap-4">
                    <b>Monto a transferir:</b>
                    <p className="font-bold text-2xl">${userData.amount}.00</p>
                </span>
                
                <span className="flex flex-row gap-4">
                    <b>Destinatario:</b>  
                    <p>{userData.UserDestination}</p>
                </span>
                <span className="flex flex-row gap-4">
                    <b>CBU:</b>
                    <p>{userData.destinationCbu}</p>
                </span>
                <span className="flex flex-row gap-4">
                    <b>Motivo: </b>
                    <p>{userData.Reason}</p>
                </span>
                <button className="w-3/5 self-center bg-LightGolden text-BlackBlue py-1 px-2 rounded-xl cursor-pointer hover:bg-Gray hover:text-LightGolden transition duration-500" onClick={handleSubmit}>Transferir</button>

                <div className="flex flex-row gap-4">
                    <svg className="fill-transparent w-8 h-8" viewBox="0 0 24 24"  stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" ><circle cx="12" cy="12" r="10"/><line x1="12" x2="12" y1="8" y2="12"/><line x1="12" x2="12.01" y1="16" y2="16"/></svg>
                    <span>Se transfiere al instante. <br />
                        No es cancelable.
                    </span>
                </div>
            
            </div>  
        </div>
    )
}



export default ConfirmationCard
