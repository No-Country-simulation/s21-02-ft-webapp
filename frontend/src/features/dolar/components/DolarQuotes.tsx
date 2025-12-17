import { useDolarQuotes } from "../hook/useDolarQuotes";

export const DolarQuotes = () => {
    const { quotes, loading, error } = useDolarQuotes();

    if (loading) return <div className="text-center py-12 text-gray-600">Cargando cotizaciones...</div>;
    if (error) return <div className="text-center py-12 text-red-500">Error: {error}</div>;

    return (
        <div className="max-w-7xl mx-auto mt-10 px-4 sm:px-6 lg:px-8">
            <h2 className="text-4xl font-bold text-gray-600 mb-10 text-center">Cotización de dólares</h2>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
                {quotes.map((quote) => (
                    <div
                        key={quote.casa}
                        className="bg-white rounded-3xl shadow-lg p-6 hover:shadow-2xl transition-all duration-300"
                    >
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-semibold text-gray-800">{quote.nombre}</h3>
                            <div className="flex flex-col text-right">
                                <span className="text-xs text-gray-400">
                                    {new Date(quote.fechaActualizacion).toLocaleDateString("es-AR", {
                                        day: "2-digit",
                                        month: "2-digit",
                                        year: "numeric",
                                    })}
                                </span>
                                <span className="text-xs text-gray-400">
                                    {new Date(quote.fechaActualizacion).toLocaleTimeString("es-AR", {
                                        hour: "2-digit",
                                        minute: "2-digit",
                                    })}
                                </span>
                            </div>
                        </div>

                        <div className="flex flex-col space-y-3 mt-2">
                            <div className="flex justify-between bg-green-50 p-3 rounded-lg items-center">
                                <span className="text-gray-600 font-medium">Compra</span>
                                <span className="text-green-700 font-bold text-lg">
                                    ${quote.compra.toLocaleString("es-AR", { minimumFractionDigits: 2 })}
                                </span>
                            </div>
                            <div className="flex justify-between bg-red-50 p-3 rounded-lg items-center">
                                <span className="text-gray-600 font-medium">Venta</span>
                                <span className="text-red-700 font-bold text-lg">
                                    ${quote.venta.toLocaleString("es-AR", { minimumFractionDigits: 2 })}
                                </span>
                            </div>
                        </div>

                        <div className="mt-4 text-sm text-gray-500 text-right">
                            Última actualización
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};
