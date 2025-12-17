import { twMerge } from 'tailwind-merge';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  containerClass?: string;
  leftAddon?: string;
}

export const Input = ({
  label,
  containerClass = '',
  className = '',
  leftAddon,
  ...props
}: InputProps) => {
  const inputClasses = twMerge(
    'block w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500 focus:border-cyan-500 transition-colors duration-200',
    leftAddon ? 'pl-12' : '', // espacio adicional si hay leftAddon
    className
  );

  return (
    <div className={twMerge('space-y-1 mb-4', containerClass)}>
      {label && (
        <label className="block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}

      <div className="relative">
        {leftAddon && (
          <span className="absolute inset-y-0 left-0 pl-4 flex items-center text-gray-500">
            {leftAddon}
          </span>
        )}
        <input className={inputClasses} {...props} />
      </div>
    </div>
  );
};
