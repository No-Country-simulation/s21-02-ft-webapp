import { twMerge } from 'tailwind-merge';
import { ReactNode } from 'react';

interface CardProps {
  children: ReactNode;
  className?: string;
  withShadow?: boolean;
}

export const Card = ({ children, className = '', withShadow = true }: CardProps) => {
  const mergedClasses = twMerge(
    'bg-white p-6 rounded-lg border border-gray-200 mb-4 lg:mb-0 shadow-md lg:w-[100%]',
    withShadow ? 'shadow-md' : '',
    className
  );

  return <div className={mergedClasses}>{children}</div>;
};