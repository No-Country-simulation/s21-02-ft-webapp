// src/components/ui/Card.tsx
import { twMerge } from 'tailwind-merge';
import { ReactNode } from 'react';

interface CardProps {
  children: ReactNode;
  className?: string;
}

export const Card = ({ children, className = '' }: CardProps) => {
  const mergedClasses = twMerge(
    'bg-white rounded-lg shadow-md p-6',
    className
  );

  return <div className={mergedClasses}>{children}</div>;
};