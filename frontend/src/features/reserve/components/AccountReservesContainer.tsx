import { useEffect, useMemo } from "react";
import { useAccountStore } from "../../account/stores/useAccountStore";
import { ReserveList } from "./ReserveListForm";
import { AccountReservesHeader } from "./AccountReservesHeader";
import { useReserves } from "../hook/useReserve";
import { useDolarOfficial } from "../hook/useDolarOfficial";

export const AccountReservesContainer = () => {
  const {
    accounts,
    loading: accountsLoading,
    error: accountsError,
    activeAccountId,
    setActiveAccountId,
    fetchAccounts,
  } = useAccountStore();

  // 🔹 Solo pasar accountId si es válido
  const { reserves, loading: reservesLoading, error: reservesError } = useReserves(
    activeAccountId ?? undefined
  );

  const { dolarData, loading: dolarLoading, error: dolarError } = useDolarOfficial();

  useEffect(() => {
    if (!accounts || accounts.length === 0) fetchAccounts();
  }, [accounts, fetchAccounts]);

  useEffect(() => {
    if (!accounts || accounts.length === 0) return;

    const existsActive =
      typeof activeAccountId === "number" && accounts.some(a => a.accountId === activeAccountId);
    if (existsActive) return;

    const arsAccount =
      accounts.find(a => typeof a.currency === "string" && a.currency.trim().toUpperCase() === "ARS") ||
      accounts[0];

    setActiveAccountId(arsAccount.accountId);
  }, [accounts, activeAccountId, setActiveAccountId]);

  const totalReservedAmount = useMemo(() => {
    if (!reserves) return 0;
    return reserves.reduce((sum, reserve) => sum + (reserve.reservedAmount || 0), 0);
  }, [reserves]);

  const yieldPercentage = useMemo(() => {
    const baseDolarPrice = 1200;
    if (!dolarData || dolarData.venta <= baseDolarPrice) return 0;

    const difference = dolarData.venta - baseDolarPrice;
    return (difference / baseDolarPrice) * 100;
  }, [dolarData]);

  if (accountsLoading || reservesLoading || dolarLoading) {
    return <div className="p-4 text-center text-gray-700">Cargando datos...</div>;
  }
  if (accountsError) return <div className="p-4 text-center text-red-600">Error de cuentas: {accountsError}</div>;
  if (reservesError) return <div className="p-4 text-center text-red-600">Error de reservas: {reservesError}</div>;
  if (dolarError) return <div className="p-4 text-center text-red-600">Error de dólar: {dolarError}</div>;
  if (!accounts || accounts.length === 0) return <div className="p-4 text-center text-gray-700">No hay cuentas disponibles</div>;

  return (
    <div className="w-full px-4 mt-10 sm:px-6 lg:px-8">
      <AccountReservesHeader totalReserved={totalReservedAmount} yieldPercentage={yieldPercentage} />

      {activeAccountId !== null && (
        <div className="max-w-md mx-auto mt-8">
          <ReserveList sourceAccountId={activeAccountId} />
        </div>
      )}
    </div>
  );
};
