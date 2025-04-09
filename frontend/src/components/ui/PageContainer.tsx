// src/components/ui/PageContainer.tsx
import { ReactNode } from 'react';

interface PageContainerProps {
  children: ReactNode;
  className?: string;
}

export const PageContainer = ({ children, className = '' }: PageContainerProps) => {
  return (
    <div className={`min-h-screen bg-gray-100 rounded-lg border border-gray-200 ${className}`}>
      {children}
    </div>
  );
};