import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { createReservation } from "../../service/reserveService";
import { useAccountStore } from "../../../account/stores/useAccountStore";
import { useAuthStore } from "../../../auth/store/authStore";
import { TransactionSuccess } from "../../../account/components/shared/TransactionSuccess";
import { TransactionResponse } from "../../../../types/account/response";

export const ReservationCreateAmountForm = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { name } = location.state || {};

  const { activeAccountId } = useAccountStore();
  const { user } = useAuthStore(); // usuario logueado

  const [amount, setAmount] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [successData, setSuccessData] = useState<TransactionResponse | null>(null);

  if (!name) return <div className="p-4 text-red-600">Error: no se seleccionó un nombre de reserva.</div>;
  if (!activeAccountId) return <div className="p-4 text-red-600">Error: no hay cuenta seleccionada.</div>;

  const suggestedAmounts = [10000, 50000, 100000];

  const handleSubmit = () => {
    if (!amount) {
      setError("Por favor, seleccioná o ingresá un monto.");
      return;
    }
    setShowConfirmModal(true);
  };

  const handleConfirmReservation = async () => {
    setShowConfirmModal(false);
    setLoading(true);
    setError(null);

    try {
      const response = await createReservation(activeAccountId, {
        reservedAmount: amount ?? 0,
        reason: name,
      });

      // Aseguramos que los campos tengan valor por defecto
      setSuccessData({
        transactionId: response.transactionId ?? "0",
        transactionDate: response.transactionDate ?? new Date().toISOString(),
        sourceAccount: user?.fullName ?? "",
        destinationAccount: name,
        amount: response.amount ?? amount ?? 0, 
        reason: response.reason ?? "",
        transactionType: "RESERVE",
      });
    } catch (err: any) {
      setError(err.message || "Error al crear la reserva.");
    } finally {
      setLoading(false);
    }
  };

  if (successData) {
    return (
      <TransactionSuccess
        userName={user?.fullName ?? "Tú"}
        currency="ARS"
        amount={(successData?.amount ?? 0).toString()}
        reason={successData?.reason ?? ""}
        transactionDetails={{
          transactionId: Number(successData?.transactionId ?? 0),
          transactionDate: (successData?.transactionDate ?? new Date()),
          sourceAccount: successData?.sourceAccount ?? "",
          destinationAccount: successData?.destinationAccount ?? "",
        }}
        destinationAccountName={successData?.destinationAccount ?? ""}
        onReturn={() => navigate("/dashboard")}
        isOwnCard
      />
    );
  }

  return (
    <div className="max-w-lg mx-auto p-6 bg-white rounded-lg shadow-md mt-8">
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4">
          {error}
        </div>
      )}

      <h2 className="text-2xl font-semibold text-gray-800 mb-6">
        ¿Cuánto querés reservar para <span className="text-blue-600">{name}</span>?
      </h2>

      <div className="flex space-x-4 mb-6">
        {suggestedAmounts.map((val) => (
          <button
            key={val}
            onClick={() => setAmount(val)}
            className={`px-4 py-2 rounded-lg border ${
              amount === val
                ? "bg-blue-500 text-white border-blue-500"
                : "bg-white text-gray-800 border-gray-300"
            }`}
          >
            ${val}
          </button>
        ))}
      </div>

      <input
        type="number"
        placeholder="Ingresá otro monto"
        value={amount ?? ""}
        onChange={(e) => setAmount(Number(e.target.value))}
        className="shadow-sm appearance-none border-b-2 border-gray-300 w-full py-3 px-1 text-gray-700 leading-tight focus:outline-none focus:border-blue-500 text-xl mb-6"
      />

      <button
        onClick={handleSubmit}
        disabled={loading || !amount}
        className={`w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg focus:outline-none focus:shadow-outline transition-colors ${
          !amount ? "opacity-50 cursor-not-allowed" : ""
        }`}
      >
        {loading ? "Guardando..." : "Confirmar reserva"}
      </button>

      {/* Modal de confirmación */}
      {showConfirmModal && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-white rounded-lg p-6 max-w-sm w-full text-center space-y-4">
            <h3 className="text-lg font-bold">Confirmar reserva</h3>
            <p>¿Deseás reservar ${amount?.toLocaleString("es-AR")} para "{name}"?</p>
            <div className="flex justify-between space-x-4 mt-4">
              <button
                onClick={() => setShowConfirmModal(false)}
                className="w-1/2 py-2 px-4 border rounded-lg hover:bg-gray-100"
              >
                Cancelar
              </button>
              <button
                onClick={handleConfirmReservation}
                className="w-1/2 py-2 px-4 bg-blue-500 text-white rounded-lg hover:bg-blue-700"
              >
                Confirmar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
