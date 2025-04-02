// src/components/UserOnline.tsx
import { useUserOnline } from '../hooks/useUserOnline';

const UserOnline = () => {
  const { user, loading, error, token } = useUserOnline();

  if (loading) return <div className="p-4">Cargando información del usuario...</div>;

  if (error) return (
    <div className="p-4 bg-red-100 text-red-700 rounded">
      <p>Error: {error}</p>
      {!token && (
        <p className="mt-2">Por favor inicia sesión para ver esta información</p>
      )}
    </div>
  );

  if (!user) return <div className="p-4">No se encontraron datos de usuario</div>;

  return (
    <div className="p-6 max-w-md mx-auto bg-white rounded-lg shadow-md">
      <h2 className="text-xl font-bold mb-4">Mi Perfil</h2>
      <div className="space-y-3">
        <p><span className="font-semibold">Nombre:</span> {user.fullName}</p>
        <p><span className="font-semibold">Email:</span> {user.email}</p>
        <p><span className="font-semibold">Teléfono:</span> {user.phoneNumber}</p>
        <p><span className="font-semibold">Estado:</span> 
          <span className={`ml-2 px-2 py-1 text-xs rounded-full ${
            user.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
          }`}>
            {user.active ? 'Activo' : 'Inactivo'}
          </span>
        </p>
      </div>
    </div>
  );
};

export default UserOnline;