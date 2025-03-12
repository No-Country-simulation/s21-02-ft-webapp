import React, { use, useState } from 'react'
import './Transferir'
import Navbar from '../../components/navbar/Navbar'
import AccountsService from '../../Services/AccountsService'
import { ArrowBigLeft, CircleAlert } from 'lucide-react'
import TransactionsService from '../../Services/TransactionsService'
const Transferir = () => {

  
      const [transferUser, settransferUser] = useState(""); 
      const [amount, setAmount] = useState(); 
      const {FetchAccountsServiceByCVU} = AccountsService();
      const {CreateTransactionsService} = TransactionsService();
      const [Accounts, SetAccounts] = useState();
      const [Confirmation, SetConfirmation] = useState(false)
      const [Error , Seterror] = useState('')

      const [Reason, setReason] = useState('');
      const handleSubmit = async() => {
        Seterror('')
      try{
        const response = await FetchAccountsServiceByCVU(transferUser)
        console.log(response)

        if (response !== true && amount !== undefined) {
          SetAccounts(response);
          SetConfirmation(!Confirmation);
      } else {
          Seterror('Datos invalidos');
      }
      }catch{

      }
       
      };

      const HandlerTransaction = async() =>{

          await CreateTransactionsService(Accounts.cbu ,amount,Reason)
        
      }
  
  return (
    <div className='Contenido'>
      <nav><Navbar /></nav>
      <main>
        <div className="flex flex-col justify-center items-center mt-10">
          
          {!Confirmation && 
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
                onChange={(e) => settransferUser(e.target.value)}
              />
              <label htmlFor="amount-input">Monto</label>
              <input className="px-8 py-2 bg-LightGolden rounded-md text-BlackBlue appearance-none [&::-webkit-inner-spin-button]:appearance-none [&::-webkit-outer-spin-button]:appearance-none placeholder:text-Gray"
                type="number"
                name="amount-input"
                id="amount-input"
                placeholder="Monto a transferir"
                onChange={(e) => setAmount(e.target.value)}
              />
              <input className="px-4 py-2 bg-LightGolden text-BlackBlue rounded-3xl cursor-pointer hover:bg-DarkGolden  text-black transition duration-500"
                type="submit"
                value="Continuar"
                onClick={handleSubmit}
              />
              {Error !== '' && <p className='text-red-500'>{Error}</p>}
            </div>
          </div>
        }
           {Confirmation &&  
          <div className="flex justify-center">
            <div className="flex flex-col w-full max-w-md space-y-6 space rounded-xl bg-BlackBlue p-8 text-WhiteBlack">
                
            <ArrowBigLeft onClick={()=>(SetConfirmation(!Confirmation))} className='text-LightGolden hover:text-DarkGolden '/>
                <h1 className="text-3xl font-bold">Verifica si todo esta bien</h1>
                <span className="flex flex-row gap-4">
                    <b>Monto a transferir:</b>
                    <p className="font-bold text-2xl">${amount}</p>
                </span>
                
                <span className="flex flex-row gap-4">
                    <b>Destinatario:</b>  
                    <p>{Accounts.owner}</p>
                </span>
                <span className="flex flex-row gap-4">
                    <b>CVU:</b>
                    <p>{Accounts.cbu}</p>
                </span>
                <span className="flex flex-row gap-4">
                    <b>Alias:</b>
                    <p>{Accounts.alias}</p>
                </span>
                <select className="flex flex-row gap-4"  onChange={(e)=>{setReason(e.target.value)}}>
                <option value="" disabled selected >Motivos</option> 
                <option className='text-black'>Varios</option>
                <option className='text-black'>Alquileres</option>
                <option className='text-black'>Aportes Capital</option>
                <option className='text-black'>Bienes registrables habtialistas</option>
                <option className='text-black'>Bienes registrables  no habtialistas</option>
                <option className='text-black'>Coutas</option>
                <option className='text-black'>Expensas</option>
                <option className='text-black'>Haberes</option>
                <option className='text-black'>Honorarios</option>
                <option className='text-black'>Operaciones inmobiliarias</option>
                <option className='text-black' >Operaciones inmobiliarias habitualistas</option>
                <option className='text-black'>Seguros</option>
                <option className='text-black'>Suscripciones</option>

                </select>
                <button className="w-3/5 self-center bg-LightGolden text-BlackBlue py-1 px-2 rounded-xl cursor-pointer hover:bg-DarkGolden transition duration-500" onClick={HandlerTransaction} >Transferir</button>

                <div className="flex flex-row gap-4 text-LightGolden">
                
                <svg className="fill-transparent w-8 h-8" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <CircleAlert/>
                </svg>
                
                    <span>Se transfiere al instante. <br />
                        No es cancelable.
                    </span>
                </div>
            
            </div>  
        </div>
        } 
        </div>
      </main>
    </div>
  )
}

export default Transferir

