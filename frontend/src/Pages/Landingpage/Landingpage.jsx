import React from 'react'
import './Landingpage.css'
import '../../Styles/Global.css'
import image1 from '../../assets/General/Image1_landingpage.png'
import image2 from '../../assets/General/Image2_landingpage.png'
import Navbar from '../../components/navbar/Navbar'
const Landingpage = () => {


  return (
    <div className='Contenido'>

      <nav><Navbar /></nav>

      <main >
        <div className='container mx-auto'>
          <section className='flex flex-wrap items-center mt-4'>
            <p className='text-Gray text-3xl w-full sm:w-1/2 '>
              Gestiona tus finanzas de forma fácil, rápida y segura con Wallet. Accede a tus pagos y transacciones cuando los necesites.
              ¡Tu dinero, tus reglas!
            </p>
            <img src={image1} className='w-full sm:w-1/2 max-w-full' alt="Una persona sosteniendo un celular con la Wallex" />
          </section>
          <div className=' border-2 rounded-4xl w-3/4 h mx-auto'  ></div>

          <section className=' my-10'>
            <h2 className=' text-4xl text-center mb-6 font-bold'>Tu experiencia financiera simplificada </h2>
            <div className='grid grid-cols-1 sm:grid-cols-2 gap-3'>

              <div className=' bg-LightGolden  rounded-3xl p-3  '>
                <p>Seguridad de vanguardia 🔒:</p>
                <p> Protege tus fondos con encriptación avanzada y autenticación de dos factores. </p>
              </div>
              <div className=' bg-LightGolden rounded-3xl p-3  '>
                <p>Paga con tarjeta 💳: </p>
                <p> Realiza compras y pagos de manera fácil y rápida con tu tarjeta virtual.</p>
              </div>
              <div className=' bg-LightGolden rounded-3xl p-3 '>
                <p>Transferencias gratis e instantáneas ⚡: </p>
                <p>Envía dinero al instante, sin comisiones.</p>
              </div>
              <div className=' bg-LightGolden rounded-3xl p-3 '>
                <p>Control total de tu dinero 💼:</p>
                <p>Administra tus fondos de manera simple y eficiente, en cualquier momento.</p>
              </div>

            </div>
          </section>
          <div className=' border-2 rounded-4xl w-3/4 h mx-auto'  ></div>

          <section className='flex flex-col sm:flex-row mt-4'>
  <div className='sm:w-1/2 p-4 mt-2'>
    <h3 className='text-4xl font-bold mb-10'>Cuenta digital a partir de los 13 Años</h3>
    <p className='text-Gray text-2xl'>
      Con Wallex, puedes tener tu cuenta gratis, enviar y recibir dinero, ahorrar y gestionar tus fondos de manera fácil y segura, todo desde tu celular.
    </p>
  </div>
  <div className='sm:w-1/2 p-4 flex justify-center'>
    <img src={image2} className='w-80 max-w-full rounded-full' alt="Una persona sonriendo al celular" />
  </div>
</section>


        </div>
      </main>


      <footer>
  <div className='bg-BlackBlue flex flex-wrap justify-between text-white py-3 px-4'>
    <a className='text-2xl mb-2 sm:mb-0' href='/Acercade'>
      Acerca de
    </a>
    <a className='text-2xl mb-2 sm:mb-0' href='/Accesibilidad'>
      Accesibilidad
    </a>
    <a className='text-2xl mb-2 sm:mb-0' href='/Condicionesdeuso'>
      Condiciones de uso
    </a>
    <a className='text-2xl mb-2 sm:mb-0' href='/Terminosdelservicio'>
      Términos del Servicio
    </a>
    <a className='text-2xl mb-2 sm:mb-0'>
      Políticas de Privacidad
    </a>
  </div>
</footer>

    </div>
  )
}

export default Landingpage