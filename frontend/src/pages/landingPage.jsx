import Img3 from "../assets/images/landing-page-3.png";


const landingPage = ()=>{

    return (
        <div>
            <section className="flex flex-row justify-center px-8 py-4">
                <p className="text-left text-xl">Gestiona tus finanzas de <br /> forma fácil, rápida y <br /> segura con Wallex. <br/>
                    Accede a tus pagos y <br /> transacciones cuando <br /> los necesites. <br/>
                    ¡Tú Dinero, tus reglas!
                </p>
                <picture>
                    
                </picture>
            </section>
                <div className="flex w-full justify-center">
                    <hr className=" w-3/4  self-center border-2 m-8 rounded" />
                </div>
            <section>
                <h1 className="text-center text-3xl m-4">Tu experiencia financiera simplificada</h1>
                <div className="flex gap-4">
                    <div className="flex flex-col gap-4 px-8">
                        <p className="p-4 bg-amber-200">Seguridad de vanguardia: <br />
                        Protege tus fondos con encriptación avanzada y autenticación de dos factores.
                        </p>

                        <p className="p-4 bg-amber-200">
                            Transferencias gratis e instantáneas: <br />
                            Envia dinero al instante, sin comisiones.
                        </p>
                    </div>

                    <div className="flex flex-col gap-4">
                        <p className="p-4 bg-amber-200">Paga con tarjeta : <br />
                        Realiza compras y pagos de manera fácil y rápida con tu tarjeta virtual.
                        </p>

                        <p className="p-4 bg-amber-200">Control total de tú dinero: <br />
                        Administra tus fondos de manera simple y eficiente, en cualquier momento.
                        </p>

                    </div>
                </div>
            </section>
                <div className="flex w-full justify-center">
                    <hr className=" w-3/4  self-center border-2 m-8 rounded" />
                </div>

            <section className="flex flex-row justify-center">
                <div className="flex flex-col px-8">
                    <h1 className="text-3xl ">Cuenta digital a partir de los 13 años</h1>
                    <p className="text-lg p-3">Con Wallex, puedes tener tu cuenta <br /> gratis, enviar y recibir dinero. <br />
                    Ahorrar y gestionar tus fondos de <br /> mánera fácil y segura, todo desde tu celular.
                    </p>
                </div>
                <picture className="flex justify-center">
                    <img className="w-full h-full" src={Img3} alt="" />
                </picture>
                
            </section>
        </div>
    );
}

export default landingPage;