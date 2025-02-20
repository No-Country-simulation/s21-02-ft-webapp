import { useState } from 'react';

const Navbar = ()=>{
    const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
    
    
    return (
        <nav className="bg-gray-800 w-full h-16 flex items-center justify-between px-4 fixed top-0">
          <div className="flex items-center space-x-4">
            <div className="relative">
              <button 
                className="text-white hover:text-gray-300 flex items-center space-x-2 px-3 py-2 rounded-md hover:bg-gray-700"
                onClick={() => setIsProfileMenuOpen(!isProfileMenuOpen)}
              >
                
                <span>Perfil</span>
              </button>
    
              {isProfileMenuOpen && (
                <div className="absolute left-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
                  <div className="py-1" role="menu" aria-orientation="vertical">
                    <button className='w-full text-left px-4 py-2 hover:text-blue-600'>Inicio</button>
                    <button className='w-full text-left px-4 py-2 hover:text-blue-600'>Tú Dinero</button>
                    <button className='w-full text-left px-4 py-2 hover:text-blue-600'>Actividad</button>
                    <button className='w-full text-left px-4 py-2 hover:text-blue-600'>Transferir Dinero</button>
                    <button className='w-full text-left px-4 py-2 hover:text-blue-600'>Reservas</button>
                  </div>
                </div>
              )}
            </div>
            <button className="text-white hover:text-gray-300 flex items-center space-x-2 px-3 py-2 rounded-md hover:bg-gray-700">
              
              <span>Inicio</span>
            </button>
          </div>
          
          <div className="flex items-center space-x-4">
            <button className="text-white hover:text-gray-300 flex items-center space-x-2 px-3 py-2 rounded-md hover:bg-gray-700">
              
              <span>Notificaciones</span>
            </button>
            <button className="text-white hover:text-gray-300 flex items-center space-x-2 px-3 py-2 rounded-md hover:bg-gray-700">
              
              <span>Ayuda</span>
            </button>
          </div>
        </nav>
      );
    }
    
    export default Navbar;
    