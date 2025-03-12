import compras from '../../assets/Icons/bolsa.png';
import transferencia from '../../assets/Icons/Enviodedinero.png';
import deposito from '../../assets/Icons/Deposito.png';
import iconoGenerico from '../../assets/Icons/Dinero.png'; 
import chancho from '../../assets/Icons/chancho.png'
import { useEffect, useState } from 'react';

function ActivityItem({ data }) {

  const name = localStorage.getItem('Name');
  const formattedDate = data.createdAt ? data.createdAt.split("T")[0] : data.date.split("T")[0]; 
  const [year, month, day] = formattedDate.split("-");
  const monthNames = [
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
  ];
  const monthName = monthNames[parseInt(month, 10) - 1]; 
  const formattedDateWithMonthName = `${day} de ${monthName} de ${year}`;

  const formattedTime = data.createdAt ? data.createdAt.split("T")[1].split(".")[0] : data.date.split("T")[1].split(".")[0]; // Extrae la hora completa
  const [hour, minutes] = formattedTime.split(":"); 
  const formattedTimeWithoutSeconds = `${hour}:${minutes}`;

  let icon;
  if (data.type === "TRANSFER") {
    icon = transferencia;
  } else if (data.type === "DEPOSIT") {
    icon = deposito;
  }else if (data.type === "com.wallex.financial_platform.entities.Reservation") {
    icon = chancho; 
  } else {
    icon = iconoGenerico; 
  }


  const owner = data.destinationAccount ? data.destinationAccount.owner : data.accountOwner; 


  const typeText = data.type === 'com.wallex.financial_platform.entities.Reservation' ? 'Reserva' : data.type;

  return (
    <div className="border-b bg-white border-blue-800 rounded-3xl py-4 px-3">
      <div className="text-sm text-gray-700 mb-2">{formattedDateWithMonthName}</div>
      <div className="flex items-start justify-between">
        <div className="flex items-start gap-3">
          <div className="w-8 h-8  rounded-full flex items-center justify-center">
            <img src={icon} alt={data.type} className="w-10 h-10" /> 
          </div>
          <div>
            <p className="font-medium">{owner}</p>
            <p className="text-sm text-Gray">{typeText}</p>
          </div>
        </div>
        <div className="text-right">
          <p className="font-medium">${data.amount}</p>
          <p className="text-sm text-Gray">{formattedTimeWithoutSeconds}</p>
        </div>
      </div> 
    </div>
  );
}

export default ActivityItem;



//   {
//     "movementId": 1,
//     "transactionId": 1,
//     "sourceAccount": {
//         "cbu": "CBU000000035110000000000011",
//         "alias": "otter.wood.chair",
//         "currency": "ARS",
//         "owner": "María Gómez",
//         "dni": "87654321"
//     },
//     "destinationAccount": {
//         "cbu": "CBU000000035160000000000024",
//         "alias": "gnat.rubber.bed",
//         "currency": "ARS",
//         "owner": "Carlos López",
//         "dni": "56789123"
//     },
//     "createdAt": "2025-03-10T12:31:45.830149",
//     "description": "Movimiento generado para la transacción 1",
//     "type": "TRANSFER",
//     "status": "COMPLETED",
//     "currency": "ARS",
//     "amount": 1000
// }





// {
//   "transactionId": 1,
//   "accountOwner": "Tesoreria Wallex",
//   "date": "2025-03-10T13:15:03.959242",
//   "type": "TRANSFER",
//   "status": "COMPLETED",
//   "amount": 500000,
//   "reason": "Ingreso de dinero desde tarjeta",
//   "detailUrl": "/api/transactions/1"
// }