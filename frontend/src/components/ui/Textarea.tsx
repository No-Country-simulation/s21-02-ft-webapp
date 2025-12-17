import { twMerge } from 'tailwind-merge';

interface TextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  containerClass?: string;
}

export const Textarea = ({
  label,
  containerClass = '',
  className = '',
  ...props
}: TextareaProps) => {
  const textareaClasses = twMerge(
    'mt-1 block w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500 focus:border-cyan-500 transition-colors duration-200 resize-none',
    className
  );

  return (
    <div className={twMerge('space-y-1 mb-4', containerClass)}>
      {label && (
        <label className="block text-sm font-medium text-gray-700">
          {label}
        </label>
      )}
      <textarea className={textareaClasses} {...props} />
    </div>
  );
};
