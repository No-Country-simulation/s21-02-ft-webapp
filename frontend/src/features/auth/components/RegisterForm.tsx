import { useRegisterForm } from '../hooks/useRegisterForm';
import { Button } from '../../../components/ui/Button';
import { Input } from '../../../components/ui/Input';
import { Alert } from '../../../components/ui/Alert';
import { Card } from '../../../components/ui/Card';
import { Link } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

export const RegisterForm = () => {
    const {
        formData,
        errors,
        isLoading,
        handleChange,
        handleSubmit
    } = useRegisterForm();

    const { isAuthenticated, logout } = useAuthStore();

    if (isAuthenticated()) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <Card className="w-full max-w-lg p-6">
                    <div className="text-center space-y-4">
                        <p className="text-gray-600">Ya has iniciado sesión</p>
                        <Button variant="danger" onClick={logout} fullWidth>
                            Cerrar sesión
                        </Button>
                        <Link to="/" className="block">
                            <Button variant="outline" fullWidth>
                                Volver al inicio
                            </Button>
                        </Link>
                    </div>
                </Card>
            </div>
        );
    }

    return (
        <div className="flex items-center justify-center min-h-screen p-4">
            <Card className="w-full max-w-lg">
                <div className="p-6">
                    <div className="text-center mb-6">
                        <h2 className="text-2xl font-bold text-gray-800">Crear una nueva cuenta</h2>
                        <p className="text-gray-600 mt-2">Ingresa tus datos para continuar</p>
                    </div>

                    {errors.submit && (
                        <Alert message={errors.submit} variant="error" className="mb-4" />
                    )}

                    <form onSubmit={handleSubmit} className="space-y-4">
                        <Input
                            id="fullName"
                            name="fullName"
                            label="Nombre completo"
                            type="text"
                            value={formData.fullName}
                            onChange={handleChange}
                            placeholder="Ej: María González"
                            required
                            disabled={isLoading}
                            minLength={10}
                            maxLength={50}
                        />

                        <Input
                            id="dni"
                            name="dni"
                            label="DNI"
                            type="text"
                            inputMode="numeric"
                            pattern="\d*"
                            value={formData.dni}
                            onChange={handleChange}
                            placeholder="Ej: 87654321"
                            required
                            disabled={isLoading}
                            minLength={7}
                            maxLength={8}
                        />

                        <Input
                            id="email"
                            name="email"
                            label="Email"
                            type="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="Ej: usuario@ejemplo.com"
                            required
                            disabled={isLoading}
                        />

                        <Input
                            id="phoneNumber"
                            name="phoneNumber"
                            label="Teléfono (Argentina)"
                            type="tel"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                            placeholder="Ej: +54 9 XXX XXX XXXX"
                            required
                            disabled={isLoading}
                            pattern="(?=.*[0-9]).{10,15}" 
                        />

                        <Input
                            id="password"
                            name="password"
                            label="Contraseña"
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                            placeholder="••••••••"
                            required
                            disabled={isLoading}
                            minLength={8}
                            maxLength={30}
                        />

                        <Button
                            type="submit"
                            variant="gradient"
                            isLoading={isLoading}
                            fullWidth
                            className="mt-2"
                        >
                            {isLoading ? 'Registrando...' : 'Registrarse'}
                        </Button>
                    </form>

                    <div className="text-center text-sm text-gray-600 mt-4">
                        ¿Ya tienes una cuenta?{' '}
                        <Link
                            to="/login"
                            className="text-cyan-600 font-medium hover:underline"
                        >
                            Inicia sesión
                        </Link>
                    </div>
                </div>
            </Card>
        </div>
    );
};