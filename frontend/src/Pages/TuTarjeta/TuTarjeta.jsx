import React, { useEffect, useState } from 'react';
import './TuTarjeta.css';
import UseFetchCard from '../../Hooks/Card/UseFetchCard';
import Mastercard from '../../assets/Icons/Mastercard.png';
import logo from '../../assets/Icons/Logo.png';
import Navbar from '../../components/navbar/Navbar';

const TuTarjeta = () => {
  const { FetchCarduser } = UseFetchCard();
  const [Card, SetCard] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      const data = await FetchCarduser();
      SetCard(data);
    };

    fetchData();
  }, []);


  const isCardValid = Card && Card.type !== '' && Card.encryptedNumber && Card.expirationDate;

  const cardNumber = isCardValid ? Card.encryptedNumber : '---- ---- ---- ----';
  const expirationDate = isCardValid ? Card.expirationDate : '--/--';
  const cvv = isCardValid ? Card.cvv : '---'; 

  return (
    <div className='Contenido'>
      <nav><Navbar /></nav>
      <main>
        <div className='flex justify-center items-center mt-10'>
          <div className='bg-BlackBlue p-8 rounded-lg max-w-xl w-full'>
            <div className='flex justify-between mb-5'>
              <div className='flex'>
                <img src={Mastercard} className='mr-2 w-15 h-10' />
                <h3 className='text-white text-4xl'>Tarjeta virtual</h3>
              </div>
              <img src={logo} className='w-20' />
            </div>
            <div className='flex justify-between items-center mb-5'>

              
              <p className='text-white text-3xl'>{cardNumber}</p>
              <button className='bg-LightGolden hover:bg-DarkGolden rounded-2xl px-3'>
                Copiar
              </button>
            </div>

            <div className="flex justify-between items-center space-x-5">
              <div className='flex'>
              <div className="flex flex-col items-center sm:items-start mr-7">
                <h5 className="text-white text-2xl">Vencimiento</h5>
                <p className="text-white text-3xl">{expirationDate}</p>
              </div>

              <div className="flex flex-col items-center sm:items-start">
                <h5 className="text-white text-2xl">Cvv</h5>
                <p className="text-white text-3xl">{cvv}</p>
              </div>
              </div>
              {Card === true &&
                <button className="bg-LightGolden px-4 py-1 rounded-2xl text-xl">
                  Crear
                </button>
              }
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default TuTarjeta;
