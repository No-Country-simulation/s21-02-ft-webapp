// src/features/account/components/RegisterAccountForm.tsx
import { Button } from "../../../components/ui/Button";
import { Alert } from "../../../components/ui/Alert";
import { Card } from "../../../components/ui/Card";
import { Link } from "react-router-dom";
import { useAuthStore } from "../../auth/store/authStore";
import { useRegisterAccount } from "../hooks/useRegisterAccount";
import { Select } from "../../../components/ui/Select";

export const RegisterAccountForm = () => {
    const { isAuthenticated } = useAuthStore();
    const {
        currency,
        setCurrency,
        isLoading,
        error,
        handleSubmit
    } = useRegisterAccount();

    if (!isAuthenticated()) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <Card className="w-full max-w-lg p-6">
                    <div className="text-center space-y-4">
                        <p className="text-gray-600">No has iniciado sesión</p>
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
                        <p className="text-gray-600 mt-2">Seleccione el tipo de cuenta</p>
                    </div>

                    {error && (
                        <Alert message={error} variant="error" className="mb-4" />
                    )}

                    <form onSubmit={handleSubmit} className="space-y-6">
                        <Select
                            label="Tipo de cuenta"
                            value={currency}
                            onChange={(e) => setCurrency(e.target.value)}
                            options={[
                                { value: 'ARS', label: 'Pesos Argentinos ($)' },
                                { value: 'USD', label: 'Dólares Estadounidenses (US$)' }
                            ]}
                            required
                            disabled={isLoading}
                        />

                        <div className="flex gap-4">
                            <Link to="/dashboard" className="flex-1">
                                <Button variant="outline" fullWidth disabled={isLoading}>
                                    Cancelar
                                </Button>
                            </Link>
                            <Button type="submit" fullWidth isLoading={isLoading}>
                                Crear cuenta
                            </Button>
                        </div>
                    </form>
                </div>
            </Card>
        </div>
    );
};