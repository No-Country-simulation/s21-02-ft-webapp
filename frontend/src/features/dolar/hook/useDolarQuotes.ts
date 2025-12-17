// src/features/dolar/hook/useDolarQuotes.ts
import useSWR from "swr";
import { DolarQuoteDTO } from "../../../types/dollar/response";

const fetcher = (url: string) => fetch(url).then(res => res.json());

export const useDolarQuotes = () => {
  const { data, error, mutate } = useSWR<DolarQuoteDTO[]>(
    "https://dolarapi.com/v1/ambito/dolares",
    fetcher,
    {
      refreshInterval: 30000, // consulta cada 30 segundos automáticamente
      revalidateOnFocus: true, // revalida al volver a la pestaña
    }
  );

  return {
    quotes: data ?? [],
    loading: !data && !error,
    error: error ? (error as Error).message : null,
    refresh: mutate, // permite refrescar manualmente
  };
};
