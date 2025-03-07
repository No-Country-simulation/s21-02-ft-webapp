import { useState } from 'react'

function ActivityItem({ date, username, reason, amount, time, icon }) {
  return (
    <div className="border-b bg-white border-BlackBlue rounded-3xl py-4 px-3">
      <div className="text-sm text-gray-700 mb-2">{date}</div>
      <div className="flex items-start justify-between">
        <div className="flex items-start gap-3">
          <div className="w-8 h-8  rounded-full flex items-center justify-center">
            
            <span className="text-xl">{icon}</span>
          </div>
          <div>
            <p className="font-medium">{username}</p>
            <p className="text-sm text-Gray">{reason}</p>
          </div>
        </div>
        <div className="text-right">
          <p className="font-medium">${amount}</p>
          <p className="text-sm text-Gray">{time}</p>
        </div>
      </div>
    </div>
  )
}
 {/*Gaston: la funcion ActivityCard funciona como una pseudo base de datos para pruebas, cuando llegue el momento no tengan problemas en eliminarla */}
function ActivityCard() {
  const [activities] = useState([
    {
      date: '15 de Febrero, 2025',
      username: 'Carlos Rodriguez',
      reason: 'Transferencia de dinero',
      amount: '-150.00',
      time: '14:30',
      icon: "🔃",
    },
    {
      date: '14 de Febrero, 2025',
      username: 'María González',
      reason: 'Pago de servicios',
      amount: '-75.50',
      time: '11:20',
      icon: "🍔",
    },
    {
      date: '14 de Febrero, 2025',
      username: 'Juan Pérez',
      reason: 'Transferencia recibida',
      amount: '+200.00',
      time: '09:45',
      icon: "🔃",
    },
    {
      date: '13 de Febrero, 2025',
      username: 'Ana Silva',
      reason: 'Pago de alquiler',
      amount: '-300.00',
      time: '16:15',
      icon: "💵",
    },
    {
      date: '13 de Febrero, 2025',
      username: 'Luis Torres',
      reason: 'Depósito',
      amount: '+450.00',
      time: '10:00',
      icon: "💰",
    },
    {
      date: '13 de Febrero, 2025',
      username: 'Luis Torres',
      reason: 'Depósito',
      amount: '+450.00',
      time: '10:00',
      icon: "💰",
    },
    {
      date: '13 de Febrero, 2025',
      username: 'Luis Torres',
      reason: 'Depósito',
      amount: '+450.00',
      time: '10:00',
      icon: "💰",
    },
    {
      date: '13 de Febrero, 2025',
      username: 'Luis Torres',
      reason: 'Depósito',
      amount: '+450.00',
      time: '10:00',
      icon: "💰",
    },
    {
      date: '13 de Febrero, 2025',
      username: 'Luis Torres',
      reason: 'Depósito',
      amount: '+450.00',
      time: '10:00',
      icon: "💰",
    },
    {
      date: '13 de Febrero, 2025',
      username: 'Luis Torres',
      reason: 'Depósito',
      amount: '+450.00',
      time: '10:00',
      icon: "💰",
    },
  ])

  return (
    <div className='flex justify-center w-full'>
      <div className="w-full max-w-md bg-BlackBlue rounded-3xl shadow-lg p-6 space-y-6 text-black">
        <h2 className="text-xl font-semibold text-white">Tu última actividad</h2>
        
        <div className="space-y-2">
          {activities.slice(-5).map((activity, index) => (
            <ActivityItem key={index} {...activity} />
          ))}
        </div>

        <button className="w-full text-xl py-3 text-gray-300 text-left px-2 rounded-lg cursor-pointer hover:text-white transition-colors font-medium">
          Ver toda la actividad
        </button>
      </div>
    </div>
  )
}

export default ActivityCard
