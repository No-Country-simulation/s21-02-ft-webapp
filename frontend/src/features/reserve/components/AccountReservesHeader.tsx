import { useState } from 'react';
import { ReserveListModal } from '../../reserve/components/ReserveListModal';
import { useAccountStore } from '../../account/stores/useAccountStore';
import { useNavigate } from 'react-router-dom';

type Props = {
    totalReserved: number;
    yieldPercentage: number;
};

export const AccountReservesHeader = ({ totalReserved, yieldPercentage }: Props) => {
    const [modalOpen, setModalOpen] = useState(false);
    const [actionType, setActionType] = useState<'add' | 'release' | null>(null);
    const navigate = useNavigate();

    // obtener cuenta activa
    const sourceAccountId = useAccountStore((state) => state.activeAccountId);

     const handleCreateReserveClick = () => {
        navigate('/reservations/create/name');
    };

    const handleAddFundsClick = () => {
        setActionType('add');
        setModalOpen(true);
    };

    const handleReleaseFundsClick = () => {
        setActionType('release');
        setModalOpen(true);
    };

    return (
        <div className="max-w-md mx-auto p-4 bg-white rounded-lg">
            <h2 className="text-xl font-light text-gray-800">Total reservado</h2>
            <div className="flex items-center mt-2">
                <span className="text-4xl font-bold text-gray-600">
                    {new Intl.NumberFormat("es-AR", {
                        style: "currency",
                        currency: "ARS",
                        minimumFractionDigits: 2,
                    }).format(totalReserved)}
                </span>
            </div>

            <div className="flex items-center mt-2 text-green-600">
                <span className="inline-block bg-green-100 rounded-full p-1 mr-2">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7 7 7" />
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 19V3" />
                    </svg>
                </span>
                <span className="text-sm font-semibold">Rinde {yieldPercentage.toFixed(2)}%</span>
            </div>

            <div className="flex justify-around items-center mt-6 space-x-2">
                <button className="flex flex-col items-center" onClick={handleCreateReserveClick}>
                    <div className="bg-blue-500 rounded-full w-12 h-12 flex items-center justify-center text-white">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                        </svg>
                    </div>
                    <span className="mt-2 text-sm text-gray-600">Crear</span>
                </button>

                <button className="flex flex-col items-center" onClick={handleAddFundsClick}>
                    <div className="bg-blue-100 rounded-full w-12 h-12 flex items-center justify-center text-blue-500">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                        </svg>
                    </div>
                    <span className="mt-2 text-sm text-gray-600">Reservar</span>
                </button>

                <button className="flex flex-col items-center" onClick={handleReleaseFundsClick}>
                    <div className="bg-blue-100 rounded-full w-12 h-12 flex items-center justify-center text-blue-500">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 15l7-7 7 7" />
                        </svg>
                    </div>
                    <span className="mt-2 text-sm text-gray-600">Retirar</span>
                </button>
            </div>

            {/* Modal para seleccionar reservas */}
            {modalOpen && actionType && sourceAccountId && (
                <ReserveListModal
                    isOpen={modalOpen}
                    actionType={actionType}
                    sourceAccountId={sourceAccountId}
                    onClose={() => setModalOpen(false)}
                />
            )}
        </div>
    );
};
