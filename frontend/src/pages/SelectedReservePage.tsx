// src/features/reserve/pages/SelectReservePage.tsx
import { ReserveList } from "../features/reserve/components/ReserveListForm";
import { useAccountStore } from "../features/account/stores/useAccountStore";

export const SelectReservePage = () => {
  // Podés obtener el ID de la cuenta actual desde un store global (Zustand) o contexto
  const sourceAccountId = useAccountStore((state) => state.activeAccountId); // TODO: reemplazar con el account activo

  return (
    <div className="p-4">
      <ReserveList sourceAccountId={sourceAccountId!} selectable={true} />
    </div>
  );
};
