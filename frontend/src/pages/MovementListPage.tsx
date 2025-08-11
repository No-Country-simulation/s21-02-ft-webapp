import { PageContainer } from "../components/ui/PageContainer";
import {AccountMovementsContainer} from "../features/account/components/AccountMovementsContainer";

export const MovementListPage = () => {
    return (
        <PageContainer>
            <AccountMovementsContainer />
        </PageContainer>
    );
};