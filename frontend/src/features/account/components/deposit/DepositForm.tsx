import { useState, useEffect } from 'react';
import { useDeposit } from '../../hooks/useDeposit';
import { useAccountStore } from '../../stores/useAccountStore';
import { useCardStore } from '../../../card/store/useCardStore';
import { Input } from '../../../../components/ui/Input';
import { Select } from '../../../../components/ui/Select';
import { Button } from '../../../../components/ui/Button';
import { AccountResponse } from '../../../../types/account/response';

interface DepositFormProps {
  sourceAccountId: number | null;
  selectedAccount: AccountResponse | null;
}

export const DepositForm = ({ sourceAccountId, selectedAccount }: DepositFormProps) => {
  const { accounts } = useAccountStore();
  const { cards } = useCardStore();
  const { deposit, isLoading, error } = useDeposit();

  // Obtenemos las tarjetas de débito disponibles
  const debitCards = cards.filter(card => card.type === 'DEBIT');

  const [formData, setFormData] = useState({
    accountId: sourceAccountId ? String(sourceAccountId) : '',
    cardNumber: '',  // Inicialmente vacío
    amount: ''
  });

  // ⚠️ SOLUCIÓN: Efecto para setear automáticamente el cardNumber cuando cargan las tarjetas
  useEffect(() => {
    if (debitCards.length > 0 && !formData.cardNumber) {
      setFormData(prev => ({ ...prev, cardNumber: debitCards[0].encryptedNumber }));
    }
  }, [debitCards, formData.cardNumber]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      console.log('Deposit Request Payload:', JSON.stringify({
        amount: Number(formData.amount),
        cardNumber: formData.cardNumber
      }));
      await deposit(Number(formData.accountId), {
        amount: Number(formData.amount),
        cardNumber: formData.cardNumber
      });
      // Resetea el formulario pero mantiene el cardNumber default
      setFormData({ accountId: '', cardNumber: debitCards[0]?.encryptedNumber || '', amount: '' });
    } catch (err) {
      console.error('Deposit failed:', err);
    }
  };

  const handleChange = (field: string, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-md mx-auto">
      <Select
        label="Tarjeta de débito"
        value={formData.cardNumber}
        onChange={(e) => handleChange('cardNumber', e.target.value)}
        options={debitCards.map(card => ({
            value: card.encryptedNumber,
            label: `${card.issuingBank} - ****${card.encryptedNumber.slice(-4)} - $${card.balance.toFixed(2)}`
        }))}
        required
      />

      <Input
        label="Monto a depositar"
        type="number"
        min="0.01"
        step="0.01"
        value={formData.amount}
        onChange={(e) => handleChange('amount', e.target.value)}
        placeholder="Ej: 500.50"
        required
      />

      <Button type="submit" isLoading={isLoading} fullWidth>
        Realizar Depósito
      </Button>
    </form>
  );
};
