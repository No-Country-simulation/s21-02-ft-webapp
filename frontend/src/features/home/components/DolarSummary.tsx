import { useDolarQuotes } from "../../dolar/hook/useDolarQuotes";

const DESTACADOS = ["oficial", "blue", "bolsa", "tarjeta"]; 

export const DolarSummary = () => {
  const { quotes, loading, error } = useDolarQuotes();

  if (loading) return <div className="text-center py-6">Cargando cotizaciones...</div>;
  if (error) return <div className="text-center text-red-500">Error: {error}</div>;

  const destacados = quotes.filter(q =>
    DESTACADOS.includes(q.casa.toLowerCase())
  );

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
      {destacados.map(quote => (
        <div
          key={quote.casa}
          className="bg-white rounded-2xl shadow-md p-5 hover:shadow-xl transition-all"
        >
          <h3 className="text-lg font-semibold text-gray-700 mb-3">{quote.nombre}</h3>
          <div className="flex justify-between items-center mb-2">
            <span className="text-sm text-gray-500">Compra</span>
            <span className="text-green-600 font-bold">
              ${quote.compra.toFixed(2)}
            </span>
          </div>
          <div className="flex justify-between items-center">
            <span className="text-sm text-gray-500">Venta</span>
            <span className="text-red-600 font-bold">
              ${quote.venta.toFixed(2)}
            </span>
          </div>
        </div>
      ))}
    </div>
  );
};
