import { useState } from 'react';

const Navbar = ( name )=>{
    const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
    
    const SpanName = name;
    
    return (
        <nav className="bg-BlackBlue w-full h-16 flex items-center justify-between px-4 ">
          <div className="flex items-center space-x-4">
            <div className="relative">
              <button 
                className="text-white font-bold cursor-pointer hover:text-gray-300 flex items-center space-x-2 px-3 py-2 rounded-md transition duration-500"
                onClick={() => setIsProfileMenuOpen(!isProfileMenuOpen)}
              >
                
                <span>Perfil</span>
              </button>
    
              {isProfileMenuOpen && (
                <div className="absolute left-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
                  <div className="py-1" role="menu" aria-orientation="vertical">
                    <button className='flex flex-row justify-baseline items-center fill-white w-full  stroke-black gap-2 text-left px-4 py-2 cursor-pointer hover:text-BlackBlue hover:fill-BlackBlue hover:stroke-BlackBlue transition-colors duration-500'>
                      <svg className='w-8 h-8' viewBox="0 0 24 24" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" ><path d="M15 21v-8a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v8"/><path d="M3 10a2 2 0 0 1 .709-1.528l7-5.999a2 2 0 0 1 2.582 0l7 5.999A2 2 0 0 1 21 10v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/></svg>
                      <span >Inicio</span>
                    </button>
                    <hr className='flex self-center mx-5  w-6/8 ' />
                    <button className='flex flex-row justify-baseline items-center fill-white w-full  stroke-black gap-2 text-left px-4 py-2 cursor-pointer hover:text-BlackBlue hover:fill-BlackBlue hover:stroke-BlackBlue transition-colors duration-500'>
                      <svg className='w-8 h-8' stroke-width="2" stroke-linecap="round" stroke-linejoin="round" ><path d="M19 7V4a1 1 0 0 0-1-1H5a2 2 0 0 0 0 4h15a1 1 0 0 1 1 1v4h-3a2 2 0 0 0 0 4h3a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1"/><path d="M3 5v14a2 2 0 0 0 2 2h15a1 1 0 0 0 1-1v-4"/></svg>
                      <span>Tú Dinero</span>
                    </button>
                    <button className='flex flex-row justify-baseline items-center fill-white w-full  stroke-black gap-4 text-left px-4 py-2 cursor-pointer hover:text-BlackBlue hover:fill-LightGolden hover:stroke-BlackBlue transition-colors duration-500'>
                      <svg  className='w-7 h-8' viewBox="0 0 24 24"  stroke-width="2" stroke-linecap="round" stroke-linejoin="round" ><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M17 12h-2l-2 5-2-10-2 5H7"/></svg>
                      <span>Actividad</span>
                    </button>
                    <button className='flex flex-row justify-baseline items-center fill-white w-full  stroke-black gap-2 text-left px-4 py-2 cursor-pointer hover:text-BlackBlue hover:fill-none hover:stroke-BlackBlue transition-colors duration-500'>
                      <svg className='w-7 h-8' viewBox="0 0 24 24"  stroke-width="2" stroke-linecap="round" stroke-linejoin="round" ><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M3 9a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2"/><path d="M3 11h3c.8 0 1.6.3 2.1.9l1.1.9c1.6 1.6 4.1 1.6 5.7 0l1.1-.9c.5-.5 1.3-.9 2.1-.9H21"/></svg>
                      <span>Transferir Dinero</span>
                    </button>
                    <button className='flex flex-row justify-baseline items-center fill-white w-full  stroke-black gap-2 text-left px-4 py-2 cursor-pointer hover:text-BlackBlue hover:fill-none hover:stroke-BlackBlue transition-colors duration-500'>
                      <svg className='w-8 h-8'  viewBox="0 0 24 24"  stroke-width="2" stroke-linecap="round" stroke-linejoin="round" ><path d="M19 5c-1.5 0-2.8 1.4-3 2-3.5-1.5-11-.3-11 5 0 1.8 0 3 2 4.5V20h4v-2h3v2h4v-4c1-.5 1.7-1 2-2h2v-4h-2c0-1-.5-1.5-1-2V5z"/><path d="M2 9v1c0 1.1.9 2 2 2h1"/><path d="M16 11h.01"/></svg>
                      <span>Reservas</span>
                    </button>
                    <button className='flex flex-row justify-baseline items-center fill-white w-full  stroke-black gap-2 text-left px-4 py-2 cursor-pointer hover:text-BlackBlue hover:fill-none hover:stroke-BlackBlue transition-colors duration-500'>
                    <svg className='w-8 h-8' viewBox="0 0 24 24"  stroke-width="2" stroke-linecap="round" stroke-linejoin="round" ><rect width="20" height="14" x="2" y="5" rx="2"/><line x1="2" x2="22" y1="10" y2="10"/></svg>
                      <span>Tu Tarjeta</span>
                    </button>
                  </div>
                </div>
              )}
            </div>
            <button className="text-white h-full border-l-2 border-LightGolden flex items-center space-x-2 px-5 py-5 ">
              
              <span >Inicio</span>
            </button>
          </div>
          
          <div className="flex items-center space-x-6">
            <button className="flex h-full cursor-pointer px-4 py-2  fill-white justify-center hover:fill-LightGolden rounded-md transition duration-500">
                <svg className='w-10 h-8'>
                  <path d="M10.268 21a2 2 0 0 0 3.464 0"/><path d="M3.262 15.326A1 1 0 0 0 4 17h16a1 1 0 0 0 .74-1.673C19.41 13.956 18 12.499 18 8A6 6 0 0 0 6 8c0 4.499-1.411 5.956-2.738 7.326"/>
                </svg>
              
            </button>
            <button className="text-black bg-LightGolden border-Gray rounded-3xl cursor-pointer hover:text-gray-300 flex items-center space-x-2 px-3 py-2 hover:bg-gray-700 transition duration-500">
              <span>Ayuda</span>
            </button>
          </div>
        </nav>
      );
    }
    
    export default Navbar;
    