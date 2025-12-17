import { Modal } from '../../../../components/ui/Modal';
import { useEffect, useState } from 'react';
import { validateDestination } from '../../services/transferServices';
import { Button } from '../../../../components/ui/Button';

interface TransactionConfirmationModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  currency: string;
  amount: string | number;
  destinationIdentifier: string;
  destinationName?: string;
  isOwnCard?: boolean;
}

export const TransactionConfirmationModal = ({
  isOpen,
  onClose,
  onConfirm,
  currency,
  amount,
  destinationIdentifier,
  destinationName,
  isOwnCard = false,
}: TransactionConfirmationModalProps) => {
  const [resolvedDestinationName, setResolvedDestinationName] = useState<string>('');

  useEffect(() => {
    const fetchName = async () => {
      if (isOpen && destinationIdentifier && !destinationName) {
        try {
          const result = await validateDestination(destinationIdentifier);
          if (result.isValid) {
            setResolvedDestinationName(result.accountName ?? 'Desconocido');
          } else {
            setResolvedDestinationName('CBU/Alias no válido');
          }
        } catch {
          setResolvedDestinationName('Error al obtener nombre');
        }
      }
    };

    fetchName();

    if (!isOpen) {
      setResolvedDestinationName('');
    }
  }, [isOpen, destinationIdentifier, destinationName]);

  const displayName = destinationName ?? resolvedDestinationName ?? 'Desconocido';

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <div className="p-4 space-y-4">
        <h3 className="text-lg font-bold text-gray-800">¿Estás seguro?</h3>
        <p className="text-gray-600">
          {isOwnCard
            ? `Te depositaste desde una tarjeta propia ${currency} ${amount}`
            : `Vas a transferir ${currency} ${amount} a ${displayName}`}
        </p>
        <div className="flex gap-2">
          <Button onClick={onConfirm} fullWidth>
            Sí, confirmar
          </Button>
          <Button onClick={onClose} fullWidth variant="secondary">
            Cancelar
          </Button>
        </div>
      </div>
    </Modal>
  );
};
