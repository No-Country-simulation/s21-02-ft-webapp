import { Card} from '../../../../components/ui/Card';
import {  Alert } from '../../../../components/ui/Alert';
import { Button } from '../../../../components/ui/Button';

interface TransactionErrorProps {
  message: string;
  onAction: () => void;
  actionLabel: string;
}

export const TransactionError = ({ message, onAction, actionLabel }: TransactionErrorProps) => (
  <Card className="w-full max-w-md p-6">
    <div className="text-center space-y-4">
      <Alert message={message} variant="error" />
      <Button onClick={onAction} fullWidth className="mt-4">
        {actionLabel}
      </Button>
    </div>
  </Card>
);