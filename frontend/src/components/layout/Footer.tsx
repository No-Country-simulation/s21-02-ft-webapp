import { Link } from 'react-router-dom';
import { FaQuestionCircle, FaShieldAlt, FaLock } from 'react-icons/fa';

export const Footer = () => {
  return (
    <footer className="bg-white border-t border-gray-200 py-2 fixed bottom-0 left-0 right-0 z-10">
      <div className="text-center text-xs text-gray-500">
        © {new Date().getFullYear()} POC WCS. Todos los derechos reservados.
      </div>

      <div className="flex justify-center space-x-4 mt-2">
        <Link to="/faq" className="text-gray-600 hover:text-cyan-500 text-sm flex items-center">
          <FaQuestionCircle className="mr-1 text-gray-400 text-xs" /> Preguntas Frecuentes
        </Link>
        <Link to="/terms" className="text-gray-600 hover:text-cyan-500 text-sm flex items-center">
          <FaShieldAlt className="mr-1 text-gray-400 text-xs" /> Términos y Condiciones
        </Link>
        <Link to="/privacy" className="text-gray-600 hover:text-cyan-500 text-sm flex items-center">
          <FaLock className="mr-1 text-gray-400 text-xs" /> Privacidad
        </Link>
      </div>
    </footer>
  );
};
