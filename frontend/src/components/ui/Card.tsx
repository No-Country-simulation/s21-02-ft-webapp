import { twMerge } from 'tailwind-merge';
import { ReactNode } from 'react';

interface CardProps {
  children: ReactNode;
  className?: string;
  withShadow?: boolean;
}

export const Card = ({ children, className = '', withShadow = true }: CardProps) => {
  const mergedClasses = twMerge(
    'bg-white rounded-lg p-6 border border-gray-200',
    withShadow ? 'shadow-md' : '',
    className
  );

  return <div className={mergedClasses}>{children}</div>;
};