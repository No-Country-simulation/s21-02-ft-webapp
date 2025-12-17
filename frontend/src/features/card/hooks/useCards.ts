import { createCard } from '../services/cardServices';
import { CardRequest } from '../../../types/card/request';
import { useCardStore } from '../../card/store/useCardStore';
import { useAuthStore } from '../../../features/auth/store/authStore';

export const useCard = () => {
  const { cards, loading: isLoading, error, fetchCards, addCard: addCardToStore } = useCardStore();
  const { user } = useAuthStore();

  const addCard = async (cardData: CardRequest) => {
    try {
      const newCard = await createCard(cardData);
      addCardToStore(newCard);
      return newCard;
    } catch (err) {
      throw err;
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