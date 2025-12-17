import { useState } from 'react';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { Select } from '../../../components/ui/Select';
import { CardFormValues } from '../../../types/card/cardFormValues';
import { useCard } from '../hooks/useCards';

const banks = [
  'Banco Santander',
  'Banco Nación',
  'BBVA',
  'Galicia',
  'HSBC',
  'ICBC',
  'Macro',
  'Patagonia'
];

export const CardForm = ({ onSuccess }: { onSuccess?: () => void }) => {
  const { addCard, isLoading, error, fetchCards } = useCard();

  const [formData, setFormData] = useState<CardFormValues>({
    number: '',
    type: 'DEBIT',
    issuingBank: banks[0],
    expirationDate: '',
    cvv: '',
    balance: 0
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await addCard({
        encryptedNumber: formData.number,
        type: formData.type,
        issuingBank: formData.issuingBank,
        expirationDate: formData.expirationDate,
        encryptedCvv: formData.cvv,
        balance: formData.balance || 0
      });
      await fetchCards();
      onSuccess?.();
    } catch (err) {
      console.error('Error al agregar tarjeta:', err);
    }
  };

  const handleChange = (field: keyof CardFormValues, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && (
        <div className="p-4 bg-red-100 text-red-700 rounded-md mb-4">
          {error}
        </div>
      )}
      <Input
        label="Número de tarjeta"
        value={formData.number}
        onChange={(e) => handleChange('number', e.target.value)}
        placeholder="1234 5678 9012 3456"
        required
        pattern="\d{16}"
        title="Ingrese 16 dígitos sin espacios"
      />
      <Select
        label="Tipo de tarjeta"
        value={formData.type}
        onChange={(e) => handleChange('type', e.target.value as string)}
        options={[
          { value: 'DEBIT', label: 'Débito' },
          { value: 'CREDIT', label: 'Crédito' }
        ]}
        required
      />
      <Select
        label="Banco emisor"
        value={formData.issuingBank}
        onChange={(e) => handleChange('issuingBank', e.target.value)}
        options={banks.map(bank => ({ value: bank, label: bank }))}
        required
      />
      <Input
        label="Fecha de expiración (MM/AA)"
        value={formData.expirationDate}
        onChange={(e) => handleChange('expirationDate', e.target.value)}
        placeholder="MM/AA"
        required
        pattern="\d{2}/\d{2}"
      />
      <Input
        label="CVV"
        value={formData.cvv}
        onChange={(e) => handleChange('cvv', e.target.value)}
        placeholder="123"
        required
        pattern="\d{3}"
        type="password"
      />
      <Input
        label="Saldo inicial"
        value={formData.balance?.toString() || ''}
        onChange={(e) => handleChange('balance', e.target.value)}
        type="number"
        min="0"
        step="0.01"
      />
      <Button type="submit" isLoading={isLoading} fullWidth>
        Asociar Tarjeta
      </Button>
    </form>
  );
};