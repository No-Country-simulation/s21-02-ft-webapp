import { useState } from "react";

const TransferCard = () => {
    {/* Gaston: Falta Agregar los iconos y unificar con el Backend */}

    const [transferValue, setTransferValue] = useState(""); {/* Gaston: Estado para guardar el CBU o alias de la transferencia */}
    const [amount, setAmount] = useState(""); {/*Gaston: Estado para guardar el valor del Monto */}

    const handleSubmit = () => {
        console.log("CBU o Alias:", transferValue);
        console.log("Monto:", amount);
        {/* Gaston: Esta funcion sirve para conectar con el Backend */}
    };

    
    return (
        <div className="flex justify-center items-center">
            <div className="flex flex-col w-full max-w-md space-y-6 space rounded-xl bg-BlackBlue p-8 text-WhiteBlack">
                <div>
                    <h1 className="text-2xl">Ingresa nuevo destinatario</h1>
                </div>

                <div className="flex flex-col justify-center gap-3 text-white">
                    <label htmlFor="transfer-input">Ingrese el CBU o Alias</label>
                    <input className="px-8 py-2 bg-LightGolden rounded-md text-BlackBlue placeholder:text-Gray" 
                        type="text" 
                        name="transfer-input" id="transfer-input" 
                        placeholder="Ingrese CBU o Alias"
                        onChange={(e) => setTransferValue(e.target.value)}
                    />
                    <label htmlFor="amount-input">Monto</label>
                    <input className="px-8 py-2 bg-LightGolden rounded-md text-BlackBlue appearance-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none placeholder:text-Gray" 
                        type="number" 
                        name="amount-input" 
                        id="amount-input" 
                        placeholder="Monto a transferir"
                        onChange={(e) => setAmount(e.target.value)}
                    />
                    <input className="px-4 py-2 bg-LightGolden text-BlackBlue rounded-3xl cursor-pointer hover:bg-Gray hover:text-LightGolden transition duration-500" 
                        type="submit" 
                        value="Continuar" 
                        onClick={handleSubmit}
                    />
                </div>
            </div>
        </div>
    ) 

    
}

export default TransferCard
