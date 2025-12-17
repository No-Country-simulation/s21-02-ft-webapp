import { useState } from 'react';
import { CardList } from './CardList';
import { Button } from '../../../components/ui/Button';
import { Modal } from '../../../components/ui/Modal';
import { CardForm } from '../../../features/card/components/CardForm';

export const CardFeature = () => {
  const [showForm, setShowForm] = useState(false);

  return (
    <div className="space-y-6 p-3">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Mis Tarjetas</h2>
        <Button onClick={() => setShowForm(true)}>
          Asociar Tarjeta de debito
        </Button>
      </div>

      <CardList />

      <Modal isOpen={showForm} onClose={() => setShowForm(false)}>
        <CardForm onSuccess={() => setShowForm(false)} />
      </Modal>
    </div>
  );
};