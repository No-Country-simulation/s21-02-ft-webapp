import React, { useEffect, useState } from 'react'
import './home.css'
import Navbar from '../../components/navbar/Navbar'
import { ChevronRightIcon } from 'lucide-react'
import logo from '../../assets/Icons/Logo.png'
import mastercard from '../../assets/Icons/Mastercard.png'
import UseAccounts from '../../Hooks/Accounts/UseAccounts'
import ActivityItem from '../../components/Activity/ActivityItem'
import UseMovements from '../../Hooks/Movements/UseMovements'
const Home = () => {
  const { FetchAccountsbyid } = UseAccounts();
  const { FetchMovementsbyuser } = UseMovements();
  const [Accounts, SetAccounts] = useState([]);
  const [Movements, SetMovements] = useState([])
 

  const fetchData = async () => {
    try {
      const dataAccounts = await FetchAccountsbyid();
      const dataMovements = await FetchMovementsbyuser();
      SetMovements(dataMovements)
      SetAccounts(dataAccounts || [])
      
    } catch (error) {
      SetCurrencies([]);
    }
  };
  useEffect(() => {

    fetchData();
  }, [])

  return (
    <div className='Contenido'>
      <nav><Navbar /></nav>
      <main>
        <div className='flex flex-col justify-center items-center mt-10'>

          <div className=" w-full max-w-md bg-BlackBlue rounded-xl shadow-lg p-6 space-y-6 text-white">
            {/* Gaston: Cabecera de la tarjeta*/}
            <div className="flex justify-between items-center">
              <h2 className="text-xl font-semibold">Dinero Disponible</h2>
              <a className="px-4 py-1 bg-LightGolden text-gray-900 rounded-lg cursor-pointer hover:bg-DarkGolden transition-colors" href='/CVU'>
                Tu CVU
              </a>
            </div>

            {/*Gaston: Dinero disponible*/}
            <div className="text-3xl font-bold text-left">
              ${Accounts.balance}
            </div>

            {/* Gaston: Botones de accion*/}
            <div className="grid grid-cols-2 gap-4">
              <button className="px-4 py-1 bg-LightGolden  cursor-pointer text-blue-900 rounded-lg hover:bg-DarkGolden transition-colors font-medium">
                Ingresar Dinero
              </button>
              <a href='transferir' className=" text-center px-4 py-1 bg-LightGolden cursor-pointer text-blue-900 rounded-lg hover:bg-DarkGolden transition-colors font-medium">
                Transferir Dinero
              </a>
            </div>
            <div className='flex justify-between'>
              {/*Gaston: Marca de la tarjeta*/}
              {/*Gaston: Falta agregar los iconos */}
              <a className="w-3/4 border-LightGolden px-4 py-1 border-6 rounded-xl " href='/tutarjeta'>
                <div className='flex flex-row gap-3 '>
                  <img src={mastercard} className='w-10 h-10'></img>
                  <img src={logo} className='w-10 h-10'></img>
                </div>

                <div className='flex justify-between'>
                  <span className="text-1xl text-blue-200 italic ">Tarjeta Wallex</span>
                  <ChevronRightIcon size={20} color="#F5E7BE" strokeWidth={2} />
                </div>
              </a>

              <img src={logo} className='w-15 h-15'></img>
            </div>
          </div>

          <div className="w-full max-w-md bg-BlackBlue rounded-3xl shadow-lg p-6 space-y-6 text-black mt-10">
            <h2 className="text-xl font-semibold text-white">Tus movimientos</h2>

            <div className="space-y-2 ">
              {Movements.map((movement, index) => (
                <ActivityItem key={index} data={movement} />
              ))}

            </div>
          </div>
        </div>
      </main>

    </div>
  )
}

export default Home