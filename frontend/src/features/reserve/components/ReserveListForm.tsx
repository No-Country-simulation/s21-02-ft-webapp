// src/features/account/components/ReserveList.tsx
import { useReserves } from '../hook/useReserve';
import { useSuggestedReserve } from "../hook/useSuggestedReserve";
import { useNavigate } from "react-router-dom";

type Props = {
  sourceAccountId: number | null;
  selectable?: boolean; // 🔹 Nueva prop
};

export const ReserveList = ({ sourceAccountId, selectable = false }: Props) => {
  const { reserves, loading, error } = useReserves(sourceAccountId);
  const { types: suggestedReserves } = useSuggestedReserve();
  const navigate = useNavigate();

  if (loading) return <div className="p-4 text-center text-gray-700">Cargando reservas...</div>;
  if (error) return <div className="p-4 text-center text-red-600">Error: {error}</div>;
  if (!reserves || reserves.length === 0) return <div className="p-4 text-center text-gray-700">No hay reservas</div>;

  return (
    <div className="flex flex-col items-center mt-8">
      <h3 className="text-lg font-semibold text-slate-800 text-left w-full mb-4">
        {selectable ? "Selecciona una reserva" : "Tus reservas"}
      </h3>

      {reserves.map((reserve) => {
        const suggested = suggestedReserves.find(
          (s) => s.name.toLowerCase() == (reserve.reason ?? "").toLowerCase()
        );

        const iconUrl =
          suggested?.iconUrl && suggested.iconUrl.trim() !== ""
            ? suggested.iconUrl
            : "https://cdn-icons-png.flaticon.com/512/565/565547.png";

        return (
          <div
            key={reserve.transactionId}
            className={`w-full flex items-center justify-between p-4 rounded-lg mb-4 cursor-pointer transition 
              ${selectable 
                ? "bg-blue-50 hover:bg-blue-100 shadow" 
                : "bg-white shadow-sm"}`}
            onClick={() => {
              if (selectable) {
                navigate(`/reservations/create/amount/`, {
                  state: {reserve.reason}
                });
              }
              
            }}



            
          >
            <div className="flex items-center space-x-4">
              <img
                src={iconUrl}
                alt={reserve.reason || "Icono genérico"}
                className="w-8 h-8 object-contain"
                onError={(e) => {
                  (e.currentTarget as HTMLImageElement).src =
                    "https://cdn-icons-png.flaticon.com/512/565/565547.png";
                }}
              />

              <span className="text-gray-800 font-medium">
                {reserve.reason || "Reserva sin nombre"}
              </span>
            </div>

            <div className="text-right">
              <span className="text-lg font-semibold text-gray-800">
                {new Intl.NumberFormat("es-AR", {
                  style: "currency",
                  currency: "ARS",
                  minimumFractionDigits: 2,
                }).format(reserve.reservedAmount)}
              </span>
            </div>
          </div>
        );
      })}
    </div>
  );
};
