// src/components/ui/Input.tsx
import { twMerge } from 'tailwind-merge';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  containerClass?: string;
}

export const Input = ({
  label,
  containerClass = '',
  className = '',
  ...props
}: InputProps) => {
  const inputClasses = twMerge(
    'mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 transition-colors duration-200',
    className
  );

  return (
    <div className={twMerge('space-y-1', containerClass)}>
      {label && (
        <label className="block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <input className={inputClasses} {...props} />
    </div>
  );
};