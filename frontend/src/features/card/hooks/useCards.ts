import { useState } from 'react';
import { createCard, getCards } from '../services/cardServices';
import { CardRequest } from '../../../types/card/request';
import { useCardStore } from '../../card/store/useCardStore';
import { useAuthStore } from '../../../features/auth/store/authStore';
import { useCallback } from 'react';

export const useCard = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { cards, setCards, addCard: addCardToStore } = useCardStore(); // Usamos el store
  const { user } = useAuthStore();

  const fetchCards = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const cardsData = await getCards();
      setCards(cardsData); // Actualiza el store global
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al obtener tarjetas');
    } finally {
      setIsLoading(false);
    }
  }, [setCards]);

  const addCard = async (cardData: CardRequest) => {
    setIsLoading(true);
    setError(null);
    try {
      const newCard = await createCard(cardData);
      addCardToStore(newCard); // Añade la tarjeta al store global
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