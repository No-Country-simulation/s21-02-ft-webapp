import { useEffect, useState } from "react";
import { getMovementsByAccount } from "../services/movementService";
import { MovementResponseDTO } from "../../../types/account/response";

export const useMovements = (accountId: number) => {
    const [movements, setMovements] = useState<MovementResponseDTO[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!accountId) return;

        const fetchMovements = async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await getMovementsByAccount(accountId);
                setMovements(data);
            } catch (err: any) {
                setError(err.message || 'Error cargando movimientos');
            } finally {
                setLoading(false);
            }
        };

        fetchMovements();
    }, [accountId]);

    return { movements, loading, error };
};