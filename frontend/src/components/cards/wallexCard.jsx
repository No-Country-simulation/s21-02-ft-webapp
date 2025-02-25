import { useState } from 'react'

const WallexCard = () => {
  const [balance] = useState(0)

  return (
    <div className=" w-full max-w-md bg-gray-800 rounded-xl shadow-lg p-6 space-y-6 text-white">
      {/* Gaston: Cabecera de la tarjeta*/}
      <div className="flex justify-between items-center">
        <h2 className="text-xl font-semibold">Dinero Disponible</h2>
        <button className="px-4 py-1 bg-amber-100 text-gray-900 rounded-lg cursor-pointer hover:bg-amber-700 transition-colors">
          Tu CVU
        </button>
      </div>

      {/*Gaston: Dinero disponible*/}
      <div className="text-3xl font-bold text-left">
        ${balance.toFixed(2)}
      </div>

      {/* Gaston: Botones de accion*/}
      <div className="grid grid-cols-2 gap-4">
        <button className="px-4 py-1 bg-amber-100 cursor-pointer text-blue-900 rounded-lg hover:bg-blue-200 transition-colors font-medium">
          Ingresar Dinero
        </button>
        <button className="px-4 py-1 bg-amber-100 cursor-pointer text-blue-900 rounded-lg hover:bg-blue-200 transition-colors font-medium">
          Transferir Dinero
        </button>
      </div>

      {/*Gaston: Marca de la tarjeta*/}
      {/*Gaston: Falta agregar los iconos */}
      <div className="w-3/4 border-amber-200 px-4 py-1 border-6 rounded-xl ">
        <div>
          <span className="text-sm text-blue-200 italic">Tarjeta Wallex</span>
        </div>
      </div>
    </div>
  )
}

export default WallexCard
