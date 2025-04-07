// src/components/ui/Spinner.tsx
import { ReactNode } from 'react';

interface SpinnerProps {
  size?: 'sm' | 'md' | 'lg'; // Define los tamaños permitidos
  className?: string;
  children?: ReactNode;
}

export const Spinner = ({ size = 'md', className }: SpinnerProps) => {
  const sizeClasses = {
    sm: 'h-4 w-4 border-2',
    md: 'h-8 w-8 border-4',
    lg: 'h-12 w-12 border-4',
  };

  return (
    <div
      className={`inline-block animate-spin rounded-full border-solid border-current border-r-transparent ${
        sizeClasses[size]
      } ${className || ''}`}
      role="status"
    >
      <span className="sr-only">Cargando...</span>
    </div>
  );
};