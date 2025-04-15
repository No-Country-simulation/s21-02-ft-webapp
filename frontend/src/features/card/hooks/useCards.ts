import { useState } from 'react';
import { createCard, getCards } from '../services/cardServices';
import { CardRequest } from '../../../types/card/request';
import { CardResponse } from '../../../types/card/response';
import { useAuthStore } from '../../../features/auth/store/authStore';
import { useCallback } from 'react';

export const useCard = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [cards, setCards] = useState<CardResponse[]>([]);
  const { user } = useAuthStore();

  const fetchCards = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const cardsData = await getCards();
      setCards(cardsData);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al obtener tarjetas');
    } finally {
      setIsLoading(false);
    }
  }, []);

  const addCard = async (cardData: CardRequest) => {
    setIsLoading(true);
    setError(null);
    try {
      const newCard = await createCard(cardData);
      setCards(prev => [...prev, newCard]);
      return newCard;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al crear tarjeta');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  return {
    cards,
    isLoading,
    error,
    fetchCards,
    addCard,
    currentUser: user
  };
};