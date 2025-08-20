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
    'mt-1 block w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500 focus:border-cyan-500 transition-colors duration-200',
    className
  );

  return (
    <div className={twMerge('space-y-1 mb-4', containerClass)}>
      {label && (
        <label className="block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <input className={inputClasses} {...props} />
    </div>
  );
};