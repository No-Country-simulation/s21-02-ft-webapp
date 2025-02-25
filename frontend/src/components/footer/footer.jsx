const Footer = ()=> {
    const footerLinks = [
      { text: 'Sobre Nosotros', href: '#' },
      { text: 'Accesibilidad', href: '#' },
      { text: 'Condiciones de uso', href: '#' },
      { text: 'Políticas de privacidad', href: '#' },
    ];
  
    return (
      <footer className="fixed bottom-0 left-0 w-full bg-gray-800 text-white py-4">
        <div className="container mx-auto px-4">
          <div className="flex flex-wrap justify-center gap-4 md:gap-8">
            {footerLinks.map((link, index) => (
              <button
                key={index}
                className="px-4 py-2 text-sm rounded-lg cursor-pointer hover:bg-gray-700 transition-colors duration-200"
                onClick={() => window.location.href = link.href}
              >
                {link.text}
              </button>
            ))}
          </div>
        </div>
      </footer>
    );
  }
  
  export default Footer;