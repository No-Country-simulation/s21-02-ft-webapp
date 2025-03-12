import React, { useEffect, useState } from "react";
import Navbar from "../../components/navbar/Navbar";
import UseFetchReserve from "../../Hooks/Reserve/UseFetchReserve";
import iconchancho from '../../assets/Icons/chancho.png';
import ReserveService from "../../Services/ReserveService";
import logo from '../../assets/Icons/logo.png'
import { ArrowBigDown, ArrowBigUp, DollarSign, Plus } from "lucide-react";
import UseAccounts from "../../Hooks/Accounts/UseAccounts";

const TuReserva = () => {
  const { FetchReserve } = UseFetchReserve();
  const [reserve, setReserve] = useState([]);
  const [CreateReserve, SetCreateReserve] = useState(false);
  const [Name, SetName] = useState('');
  const [Amount, SetAmount] = useState(0);
  const [Retirar, SetRetirar] = useState(false)
  const [Reservar, SetReservar] = useState(false)
  const [Errro, Seterror] = useState('')
  const [AcountAmount, SetAcountAmount] = useState()
  const { CreateReserveService } = ReserveService();

  const {FetchAccountsbyid} = UseAccounts();

  useEffect(() => {

    const fetchData = async () => {
      try {
        const data = await FetchReserve();
        const acaount = await FetchAccountsbyid();
        SetAcountAmount(acaount.balance)
        setReserve(data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };


    fetchData();
  }, []);

  const HandleCreateReserve = async () => {

    try {
      const data = await CreateReserveService(Amount, Name);
      Seterror(data)
      const updatedata = await FetchReserve();
      setReserve(updatedata);
      SetCreateReserve(!CreateReserve)
    } catch {

    }
  }
  const HandleRetirar = async () => {
    alert(`Retiamos: $${Amount}`);

  }
  const HandleReservar = async () => {
    alert(`Rervamos: $${Amount}`);

  }
  return (
    <div>
      <nav>
        <Navbar />
      </nav>

      <div className="flex justify-center items-center mt-10 ">

        {reserve.length === 0 ? (
          !CreateReserve && (

            <div className="bg-BlackBlue rounded-2xl items-center text-center p-5 flex flex-col w-full md:w-1/2">
              <img src={iconchancho} className="invert mx-auto mb-3" alt="Icono chancho" />
              <h2 className="text-white text-3xl">Crear una nueva reserva</h2>
              <p className="text-white">Organizá tu dinero y alcanzá tus objetivos.</p>
              <button className="bg-LightGolden hover:bg-DarkGolden px-4 py-2 rounded-2xl mt-3" onClick={() => SetCreateReserve(!CreateReserve)}>
                Crear Reserva
              </button>
            </div>
          )
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

            {!CreateReserve && !(Retirar || Reservar) &&
              reserve.map((reserve) => (
                <div key={reserve.reservationId} className="bg-BlackBlue rounded-2xl p-10 flex flex-col w-full">
                  <div className="flex items-center mb-5">
                    <h3 className="text-white text-3xl mr-3">{reserve.description}</h3>
                    <img src={logo} className="w-20" alt="Logo" />
                  </div>

                  <h3 className="text-white text-6xl flex">
                    <DollarSign size={60} /> {reserve.reservedAmount}
                  </h3>

                  <div className="flex my-5">
                    <button className="flex bg-LightGolden hover:bg-DarkGolden rounded-2xl p-2 mr-3" onClick={() => (SetReservar(!Retirar))} >
                      Reservar <ArrowBigUp size={20} strokeWidth={0.75}/>
                    </button>
                    <button className="flex bg-LightGolden hover:bg-DarkGolden rounded-2xl p-2" onClick={() => (SetRetirar(!Retirar))}>
                      Retirar <ArrowBigDown size={20} strokeWidth={0.75} />
                    </button>
                  </div>

                </div>
              ))
            }

            {!CreateReserve && !(Retirar || Reservar)  &&
              <div className="col-span-2 flex justify-center mt-3">
                <button className="bg-BlackBlue text-white px-5 py-2 rounded-2xl flex" onClick={() => SetCreateReserve(!CreateReserve)}>
                  Crear Reserva <Plus size={20} strokeWidth={0.75} />
                </button>
              </div>
            }

          </div>

        )}


        {(Retirar || Reservar) && (
          <div className="bg-BlackBlue rounded-2xl items-center text-center p-5 flex flex-col w-full md:w-1/2">
            <h3 className="text-white text-3xl">
              {Retirar ? "¿Cuánto dinero quieres retirar?" : "¿Cuánto dinero quieres reservar?"}
            </h3>
            <input
              type="number"
              placeholder="0"
              onChange={(e) => SetAmount(e.target.value)}
              className="placeholder-white text-white text-5xl bg-transparent border-none focus:outline-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none ml-2 w-full text-center"
            />
            <p className="text-white">$10.10 disponibles</p>
            <button
              className="bg-LightGolden hover:bg-DarkGolden px-4 py-2 rounded-2xl mt-3"
              onClick={Retirar ? HandleRetirar : HandleReservar}
            >
              {Retirar ? "Retirar" : "Reservar"}
            </button>
          </div>
        )}



        {CreateReserve && (
          <div className="bg-BlackBlue rounded-2xl items-center text-center p-5 flex flex-col w-full md:w-1/2">
            <div>
              <h3 className="text-white text-2xl sm:text-2xl md:text-2xl lg:text-3xl">Nombre de la reserva</h3>
              <input
                type="text"
                className="bg-WhiteBlack rounded-lg px-2 py-1"
                onChange={(e) => SetName(e.target.value)}
                placeholder="Ingrese nombre"
              />
            </div>
            <div>
              <h3 className="text-white text-2xl sm:text-3xl md:text-3xl lg:text-4xl">
                ¿Cuánto dinero quieres reservar?
              </h3>
              <input
                type="number"
                placeholder="0"
                onChange={(e) => { SetAmount(e.target.value); }}
                className="placeholder-white text-white text-5xl bg-transparent border-none focus:outline-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none ml-2 w-full text-center"
              />
              <p className="text-white"> Dinero disponible: ${AcountAmount}</p>

              <button className="bg-LightGolden hover:bg-DarkGolden px-4 py-2 rounded-2xl mt-3" onClick={HandleCreateReserve}>
                Reservar
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default TuReserva;
