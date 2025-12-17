import { DolarSummary } from "./DolarSummary";
import MovementListForm from "../../movement/components/MovementListForm";
import { useAccountStore } from "../../account/stores/useAccountStore";

export const HomeContent = () => {
    const { activeAccountId } = useAccountStore();
  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-10">
      <section>
        <h2 className="text-3xl font-bold text-gray-700 mb-6 text-center">
          Cotizaciones destacadas
        </h2>
        <DolarSummary />
      </section>

      {/* Movimientos */}
      <section>
        <h2 className="text-3xl font-bold text-gray-700 mb-6 text-center">
          Últimos movimientos
        </h2>
        <MovementListForm sourceAccountId={activeAccountId || 0} />
      </section>
    </div>
  );
};
