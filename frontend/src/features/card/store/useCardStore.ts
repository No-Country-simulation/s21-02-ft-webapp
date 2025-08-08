import { create } from 'zustand';
import { api } from '../../../services/api';
import { useAuthStore } from '../../../features/auth/store/authStore';
import { CardResponse } from '../../../types/card/response';

interface CardStore {
  cards: CardResponse[];
  loading: boolean;
  error: string | null;
  setCards: (cards: CardResponse[]) => void;
  addCard: (card: CardResponse) => void;
  fetchCards: () => Promise<void>;
}

export const useCardStore = create<CardStore>((set) => ({
  cards: [],
  loading: false,
  error: null,

  setCards: (cards) => set({ cards }),

  addCard: (card) => set((state) => ({ cards: [...state.cards, card] })),

  fetchCards: async () => {
    set({ loading: true, error: null });
    try {
      const token = useAuthStore.getState().token;
      const response = await api.get<CardResponse[]>('/cards', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      set({ cards: response.data, loading: false });
    } catch (error) {
      set({
        error: error instanceof Error ? error.message : 'Error al cargar tarjetas',
        loading: false,
      });
    }
  },
}));