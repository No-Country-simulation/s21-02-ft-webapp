import { useReserveStore } from "../store/useReserveStore";
import { ReserveResponseDTO } from "../../../types/reserve/response";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { TransactionSuccess } from "../../account/components/shared/TransactionSuccess";
import { ReserveTransactionResponseDTO } from "../../../types/reserve/response";

type Props = {
  isOpen: boolean;
  onClose: () => void;
  actionType: "add" | "release";
  sourceAccountId: number | null;
};

export const ReserveListModal = ({ isOpen, onClose, actionType, sourceAccountId }: Props) => {
  const navigate = useNavigate();
  const { reserves, releaseReserve } = useReserveStore();
  const [successData, setSuccessData] = useState<ReserveTransactionResponseDTO & { reserveName?: string } | null>(null);

  if (!isOpen || !sourceAccountId) return null;

  const handleReserveClick = async (reserve: ReserveResponseDTO) => {
    if (actionType === "add") {
      navigate("/reservations/create/amount", { state: { name: reserve.reason } });
      onClose();
    } else if (actionType === "release") {
      const confirmed = window.confirm(`¿Liberar fondos de "${reserve.reason}"?`);
      if (!confirmed) return;

      const transaction = await releaseReserve(sourceAccountId, reserve.reservationId);
      if (transaction) {
        setSuccessData({ ...transaction, transactionDate: new Date(transaction.transactionDate).toISOString(), reserveName: reserve.reason });
      }
    }
  };

  if (successData) {
    return (
      <TransactionSuccess
        userName={successData.sourceAccount || ""}
        currency="ARS"
        amount={successData.amount.toString()}
        reason={successData.reason || ""}
        transactionDetails={successData}
        destinationAccountName={successData.sourceAccount || ""}
        onReturn={() => { setSuccessData(null); onClose(); }}
        type="RELEASE"
        reserveName={successData.reserveName}
      />
    );
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 px-4">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-lg p-6 relative">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">
          {actionType === "add" ? "Añadir fondos a una reserva" : "Liberar fondos de una reserva"}
        </h2>

        <div className="max-h-96 overflow-y-auto">
          {reserves.length === 0 && (
            <p className="text-gray-500 text-center py-4">No hay reservas disponibles.</p>
          )}

          <ul className="space-y-2">
            {reserves.map((r) => (
              <li
                key={r.reservationId}
                onClick={() => handleReserveClick(r)}
                className="flex justify-between items-center p-3 bg-gray-50 rounded-lg hover:bg-blue-50 cursor-pointer transition"
              >
                <div>
                  <p className="font-medium text-gray-800">{r.reason}</p>
                  <p className="text-sm text-gray-500">
                    {r.reservedAmount.toLocaleString("es-AR", { style: "currency", currency: "ARS" })}
                  </p>
                </div>
                <span
                  className={`text-sm font-semibold ${
                    actionType === "add" ? "text-green-600" : "text-red-600"
                  }`}
                >
                  {actionType === "add" ? "➕ Añadir" : "➖ Liberar"}
                </span>
              </li>
            ))}
          </ul>
        </div>

        <button
          onClick={onClose}
          className="mt-6 w-full py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition font-medium"
        >
          Cerrar
        </button>
      </div>
    </div>
  );
};
