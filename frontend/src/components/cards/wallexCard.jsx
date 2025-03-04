import { useState } from 'react'

const WallexCard = () => {
  const [balance] = useState(0)

  return (
    <div className=" w-full max-w-md bg-BlackBlue rounded-xl shadow-lg p-6 space-y-6 text-white">
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
      <div className="w-3/4 border-LightGolden px-4 py-1 border-6 rounded-xl ">
        <div className='flex flex-row gap-3 '>
          <svg className='fill-none w-18 -h-12'  viewBox="0 -9 58 58" >
            <rect x="0.5" y="0.5" rx="3.5"  />
            <path d="M34.3102 28.9765H23.9591V10.5122H34.3102V28.9765Z" fill="#FF5F00"/>
            <path d="M24.6223 19.7429C24.6223 15.9973 26.3891 12.6608 29.1406 10.5107C27.1285 8.93843 24.5892 7.99998 21.8294 7.99998C15.2961 7.99998 10 13.2574 10 19.7429C10 26.2283 15.2961 31.4857 21.8294 31.4857C24.5892 31.4857 27.1285 30.5473 29.1406 28.975C26.3891 26.8249 24.6223 23.4884 24.6223 19.7429" fill="#EB001B"/>
            <path d="M48.2706 19.7429C48.2706 26.2283 42.9745 31.4857 36.4412 31.4857C33.6814 31.4857 31.1421 30.5473 29.1293 28.975C31.8815 26.8249 33.6483 23.4884 33.6483 19.7429C33.6483 15.9973 31.8815 12.6608 29.1293 10.5107C31.1421 8.93843 33.6814 7.99998 36.4412 7.99998C42.9745 7.99998 48.2706 13.2574 48.2706 19.7429" fill="#F79E1B"/>
          </svg>
        

        </div>

        <div>
          <span className="text-sm text-blue-200 italic">Tarjeta Wallex</span>
        </div>
      </div>
    </div>
  )
}

export default WallexCard
