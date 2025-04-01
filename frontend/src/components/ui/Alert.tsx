// src/components/ui/Alert.tsx
import { twMerge } from 'tailwind-merge';

type AlertVariant = 'error' | 'success' | 'warning' | 'info';

interface AlertProps {
  message: string;
  variant?: AlertVariant;
  className?: string;
}

const variantClasses = {
  error: 'bg-red-100 text-red-700',
  success: 'bg-green-100 text-green-700',
  warning: 'bg-yellow-100 text-yellow-700',
  info: 'bg-blue-100 text-blue-700',
};

export const Alert = ({
  message,
  variant = 'error',
  className = '',
}: AlertProps) => {
  const mergedClasses = twMerge(
    'p-3 rounded-md text-sm',
    variantClasses[variant],
    className
  );

  return <div className={mergedClasses}>{message}</div>;
};