import { useReserves } from '../hook/useReserve';

type Props = {
    sourceAccountId: number;
};

export const ReserveList = ({ sourceAccountId }: Props) => {
    const { reserves, loading, error } = useReserves(sourceAccountId);

    if (loading) return <div className="p-4 text-center text-gray-700">Cargando reservas...</div>;
    if (error) return <div className="p-4 text-center text-red-600">Error: {error}</div>;
    if (!reserves || reserves.length === 0) return <div className="p-4 text-center text-gray-700">No hay reservas</div>;

    return (
        <div className="flex flex-col items-center mt-8">
            <h3 className="text-lg font-semibold text-slate-800 text-left w-full mb-4">Tus reservas</h3>
            
            {/* Iterar sobre el array de reservas para mostrar cada una */}
            {reserves.map(reserve => (
                <div key={reserve.reservationId} className="w-full flex items-center justify-between p-4 bg-white rounded-lg shadow-sm mb-4">
                    <div className="flex items-center space-x-4">
                        {/* Aquí puedes usar un icono dinámico basado en `reserve.type` si lo tienes */}
                        {reserve.type === 'Vacaciones' && (
                            <img src="https://example.com/path-to-your-vacation-icon.png" alt="Icono de vacaciones" className="w-12 h-12" />
                        )}
                        {/* Si no es de vacaciones, puedes usar un ícono predeterminado */}
                        {reserve.type !== 'Vacaciones' && (
                            <div className="w-12 h-12 bg-gray-200 rounded-full flex items-center justify-center">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </div>
                        )}
                        <span className="text-gray-800 font-medium">{reserve.type || 'Reserva sin nombre'}</span>
                    </div>
                    <div className="text-right">
                        <span className="text-lg font-semibold text-gray-800">${reserve.reservedAmount?.toFixed(2) ?? '0.00'}</span>
                    </div>
                </div>
            ))}
        </div>
    );
};