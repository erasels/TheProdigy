package theProdigy.universal;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface CardMPCostHooks {
    default int modifyMPCost(AbstractCard c, int mpcost) {return mpcost;}
    default int preMPLoss(AbstractCard c, int initialCost) {return initialCost;}
    default void postMPLoss(AbstractCard c, int loss) {}
}
