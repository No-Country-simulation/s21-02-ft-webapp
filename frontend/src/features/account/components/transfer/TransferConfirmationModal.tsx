import { useEffect, useState } from 'react';
import { Modal } from '../../../../components/ui/Modal';
import { Button } from '../../../../components/ui/Button';
import { validateDestination } from '../../services/transferServices';

interface TransferConfirmationModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  currency: string;
  amount: string;
  destinationIdentifier: string;
}

export const TransferConfirmationModal = ({
  isOpen,
  onClose,
  onConfirm,
  currency,
  amount,
  destinationIdentifier,
}: TransferConfirmationModalProps) => {
  const [destinationName, setDestinationName] = useState<string>('');

  useEffect(() => {
    const fetchName = async () => {
      if (isOpen && destinationIdentifier) {
        try {
          const result = await validateDestination(destinationIdentifier);
          if (result.isValid) {
            setDestinationName(result.accountName ?? 'Desconocido');
          } else {
            setDestinationName('CBU/Alias no válido');
          }
        } catch {
          setDestinationName('Error al obtener nombre');
        }
      }
    };

    fetchName();
  }, [isOpen, destinationIdentifier]);

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <div className="p-4 space-y-4">
        <h3 className="text-lg font-bold text-gray-800">¿Estás seguro?</h3>
        <p className="text-gray-600">
          Vas a transferir {currency} {amount} a {destinationName}
        </p>
        <div className="flex gap-2">
          <Button onClick={onConfirm} fullWidth>
            Sí, transferir
          </Button>
          <Button onClick={onClose} fullWidth variant="secondary">
            Cancelar
          </Button>
        </div>
      </div>
    </Modal>
  );
};
