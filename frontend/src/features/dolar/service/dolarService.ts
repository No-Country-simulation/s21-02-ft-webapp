import { DolarQuoteDTO } from "../../../types/dollar/response";

export const getDolarQuotes = async (): Promise<DolarQuoteDTO[]> => {
  const res = await fetch("https://dolarapi.com/v1/ambito/dolares");
  if (!res.ok) throw new Error("Error al obtener las cotizaciones");
  const data: DolarQuoteDTO[] = await res.json();
  return data;
};
