import { RegisterAccountForm } from '../features/account/components/RegisterAccountForm';
import { useAuthStore } from '../features/auth/store/authStore';
import { PageContainer } from '../components/ui/PageContainer';

export const RegisterAccountPage = () => {
    const { user } = useAuthStore((state) => state);

    if (user) {
        return (
            <PageContainer>
                <RegisterAccountForm />
            </PageContainer>
        );
    }
    return null;
};
