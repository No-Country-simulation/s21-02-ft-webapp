import { useEffect, useState } from 'react';
import { getDolarOfficial } from "../service/dollarOfficialService";

export const useDolarOfficial = () => {
    const [dolarData, setDolarData] = useState<{ compra: number, venta: number } | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchDolarPrice = async () => {
            setLoading(true);
            setError(null);
            try {
                // Llamamos a la función correctamente con paréntesis ()
                const data = await getDolarOfficial();
                setDolarData({
                    compra: data.compra,
                    venta: data.venta,
                });
            } catch (err: any) {
                setError(err.message || 'Error cargando reservas');
            } finally {
                setLoading(false);
            }
        };

        fetchDolarPrice();
    }, []);

    return { dolarData, loading, error };
};