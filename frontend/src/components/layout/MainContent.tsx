// src/components/layout/MainContent.tsx
export const MainContent = ({ children }: { children: React.ReactNode }) => {
    return (
      <div className="mx-4 lg:mx-6 pt-20 pb-20 lg:ml-72"> {/* Añadido pt-20 y lg:ml-72 */}
        {children}
      </div>
    );
  };