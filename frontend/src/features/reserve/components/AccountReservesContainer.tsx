import { useEffect, useMemo } from "react";
import { useAccountStore } from "../../account/stores/useAccountStore";
import { AccountReservesHeader } from "./AccountReservesHeader";
import { ReserveList } from "./ReserveListForm";
import { useReserveStore } from "../../reserve/store/useReserveStore";
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

  const { reserves, loading: reservesLoading, error: reservesError, fetchReserves } = useReserveStore();

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
      accounts.find(a => a.currency?.trim().toUpperCase() === "ARS") || accounts[0];

    setActiveAccountId(arsAccount.accountId);
  }, [accounts, activeAccountId, setActiveAccountId]);

  useEffect(() => {
    if (activeAccountId) fetchReserves(activeAccountId);
  }, [activeAccountId, fetchReserves]);

  const totalReservedAmount = useMemo(() => {
    return reserves.reduce((sum, r) => sum + (r.reservedAmount || 0), 0);
  }, [reserves]);

  const yieldPercentage = useMemo(() => {
    const baseDolarPrice = 1200;
    if (!dolarData || dolarData.venta <= baseDolarPrice) return 0;
    return ((dolarData.venta - baseDolarPrice) / baseDolarPrice) * 100;
  }, [dolarData]);

  if (accountsLoading || reservesLoading || dolarLoading) return <div>Cargando...</div>;
  if (accountsError) return <div>Error cuentas: {accountsError}</div>;
  if (reservesError) return <div>Error reservas: {reservesError}</div>;
  if (dolarError) return <div>Error dólar: {dolarError}</div>;
  if (!accounts?.length) return <div>No hay cuentas</div>;

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
