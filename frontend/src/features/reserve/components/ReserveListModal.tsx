// src/features/reserve/components/ReserveListModal.tsx
import { useReserves } from "../../reserve/hook/useReserve";
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
    const { reserves, loading, error, releaseReserve } = useReserves(sourceAccountId ?? undefined);
    const [successData, setSuccessData] = useState<ReserveTransactionResponseDTO | null>(null);

    if (!isOpen) return null;

   const handleReserveClick = async (reserve: ReserveResponseDTO) => {
    if (actionType === "add") {
        navigate('/reservations/create/amount', { state: { name: reserve.reason } });
        onClose();
    } else if (actionType === "release") {
        const confirmed = window.confirm(
            `¿Estás seguro que deseas liberar los fondos de la reserva "${reserve.reason}"?`
        );
        if (confirmed) {
            const transaction = await releaseReserve(reserve.reservationId);
            if (transaction) {
                setSuccessData({
                    ...transaction,
                    transactionDate: new Date(transaction.transactionDate).toISOString(), // 🔹 asegurar formato ISO
                });
            }
        }
    }
};

    // Mostrar modal de éxito si successData tiene valor
    if (successData) {
        return (
            <TransactionSuccess
                userName= {successData.sourceAccount || ""}
                currency="ARS"
                amount={successData.amount.toString()}
                reason={successData.reason || ""}
                transactionDetails={successData}
                destinationAccountName={successData.destinationAccount || ""}
                onReturn={() => {
                    setSuccessData(null);
                    onClose();
                }}
                isOwnCard
            />
        );
    }

    return (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
            <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-lg">
                <h2 className="text-lg font-semibold mb-4">
                    {actionType === "add" ? "Añadir fondos a una reserva" : "Liberar fondos de una reserva"}
                </h2>

                {loading && <p className="text-gray-500">Cargando reservas...</p>}
                {error && <p className="text-red-500">{error}</p>}
                {!loading && reserves.length === 0 && <p className="text-gray-500">No tienes reservas disponibles.</p>}

                <ul className="divide-y divide-gray-200">
                    {reserves.map((reserve) => (
                        <li
                            key={reserve.reservationId}
                            className="p-3 hover:bg-gray-100 cursor-pointer flex justify-between items-center"
                            onClick={() => handleReserveClick(reserve)}
                        >
                            <div>
                                <p className="font-medium text-gray-800">{reserve.reason}</p>
                                <p className="text-sm text-gray-500">
                                    {new Intl.NumberFormat("es-AR", { style: "currency", currency: "ARS" }).format(reserve.reservedAmount)}
                                </p>
                            </div>
                            <span className="text-blue-500 text-sm font-semibold">
                                {actionType === "add" ? "➕ Añadir" : "➖ Liberar"}
                            </span>
                        </li>
                    ))}
                </ul>

                <div className="mt-6 flex justify-end">
                    <button className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400" onClick={onClose}>
                        Cerrar
                    </button>
                </div>
            </div>
        </div>
    );
};
