import { RegisterAccountForm } from '../features/account/components/RegisterAccountForm';
import { useAuthStore } from '../features/auth/store/authStore';

export const RegisterAccountPage = () => {
    const { user } = useAuthStore((state) => state);

    if (user) {
        return (
            <div className="min-h-screen bg-gray-100">
                  <RegisterAccountForm />
                </div>
        );
    }
    return null;
};
    