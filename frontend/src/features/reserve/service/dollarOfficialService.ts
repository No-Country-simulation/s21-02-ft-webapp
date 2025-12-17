import { DolarResponse } from "../../../types/dollar/response"; 

export const getDolarOfficial = async (): Promise<DolarResponse> => {
    const response = await fetch('https://dolarapi.com/v1/dolares/oficial');
      if (!response.ok) {
    throw new Error('Error al obtener el precio del dólar');
  }
    const data = await response.json();
    return data;
};