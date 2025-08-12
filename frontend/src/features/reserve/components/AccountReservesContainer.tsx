import { useEffect, useMemo } from "react";
import { useAccountStore } from "../../account/stores/useAccountStore";
import { ReserveList } from "./ReserveListForm";
import { AccountReservesHeader } from "./AccountReservesHeader";
import { useReserves } from "../hook/useReserve"; 
import { useDolarOfficial } from "../hook/useDolarOfficial"; // Nuevo hook importado
import { Card } from "../../../components/ui/Card";

export const AccountReservesContainer = () => {
    const {
        accounts,
        loading: accountsLoading,
        error: accountsError,
        activeAccountId,
        setActiveAccountId,
        fetchAccounts,
    } = useAccountStore();

    const {
        reserves,
        loading: reservesLoading,
        error: reservesError,
    } = useReserves(activeAccountId || 0);

    const { dolarData, loading: dolarLoading, error: dolarError } = useDolarOfficial();

    useEffect(() => {
        if (!accounts || accounts.length === 0) {
            fetchAccounts();
        }
    }, [accounts, fetchAccounts]);

    useEffect(() => {
        if (!accounts || accounts.length === 0) return;

        const existsActive = typeof activeAccountId === 'number' && accounts.some(a => a.accountId === activeAccountId);
        if (existsActive) return;

        const arsAccount = accounts.find(a =>
            typeof a.currency === 'string' && a.currency.trim().toUpperCase() === 'ARS'
        ) || accounts[0];

        setActiveAccountId(arsAccount.accountId);
    }, [accounts, activeAccountId, setActiveAccountId]);

    const totalReservedAmount = useMemo(() => {
        if (!reserves) return 0;
        return reserves.reduce((sum, reserve) => sum + (reserve.reservedAmount || 0), 0);
    }, [reserves]);
    
    // Calcula el rendimiento en base a la variación del dólar
    const yieldPercentage = useMemo(() => {
        // Asume un valor base del dólar para calcular la diferencia
        const baseDolarPrice = 1200; // Valor de ejemplo, podrías obtenerlo de otro lugar
        if (!dolarData || dolarData.venta <= baseDolarPrice) return 0;

        const difference = dolarData.venta - baseDolarPrice;
        return (difference / baseDolarPrice) * 100;
    }, [dolarData]);

    if (accountsLoading || reservesLoading || dolarLoading) {
        return <div className="p-4 text-center text-gray-700">Cargando datos...</div>;
    }
    if (accountsError) {
        return <div className="p-4 text-center text-red-600">Error de cuentas: {accountsError}</div>;
    }
    if (reservesError) {
        return <div className="p-4 text-center text-red-600">Error de reservas: {reservesError}</div>;
    }
    if (dolarError) {
        return <div className="p-4 text-center text-red-600">Error de dólar: {dolarError}</div>;
    }
    if (!accounts || accounts.length === 0) {
        return <div className="p-4 text-center text-gray-700">No hay cuentas disponibles</div>;
    }

    return (
        
       <div className="flex flex-col items-center">
            <Card className="max-w-md mx-auto mt-8">
            <AccountReservesHeader
                totalReserved={totalReservedAmount}
                yieldPercentage={yieldPercentage}
            />
            
            {activeAccountId !== null && (
                <div className="max-w-md mx-auto mt-8">
                    <ReserveList sourceAccountId={activeAccountId} />
                </div>
            )}
            </Card>
        </div>
    );
};