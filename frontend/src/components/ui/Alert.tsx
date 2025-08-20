import { twMerge } from 'tailwind-merge';

type AlertVariant = 'error' | 'success' | 'warning' | 'info';

interface AlertProps {
  message: string;
  variant?: AlertVariant;
  className?: string;
}

const variantClasses = {
  error: 'bg-red-100 border-l-4 border-red-500 text-red-700',
  success: 'bg-green-100 border-l-4 border-green-500 text-green-700',
  warning: 'bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700',
  info: 'bg-blue-100 border-l-4 border-blue-500 text-blue-700',
};

export const Alert = ({
  message,
  variant = 'error',
  className = '',
}: AlertProps) => {
  const mergedClasses = twMerge(
    'p-3 rounded-md text-sm mb-4',
    variantClasses[variant],
    className
  );

  return <div className={mergedClasses}>{message}</div>;
};