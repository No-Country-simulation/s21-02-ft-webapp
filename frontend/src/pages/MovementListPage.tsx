import { PageContainer } from "../components/ui/PageContainer";
import {AccountMovementsContainer} from "../features/movement/components/AccountMovementsContainer";

export const MovementListPage = () => {
    return (
        <PageContainer>
            <AccountMovementsContainer />
        </PageContainer>
    );
};