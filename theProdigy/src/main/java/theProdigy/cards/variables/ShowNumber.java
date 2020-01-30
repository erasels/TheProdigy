package theProdigy.cards.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theProdigy.cards.abstracts.ProdigyCard;

public class ShowNumber extends DynamicVariable {
    @Override
    public String key() {
        return "theProdigy:SN";
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof ProdigyCard) {
            return ((ProdigyCard) card).baseShowNumber;
        }
        return -1;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof ProdigyCard) {
            return ((ProdigyCard) card).showNumber;
        }
        return -1;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof ProdigyCard) {
            return ((ProdigyCard) card).isShowNumberModified;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof ProdigyCard) {
            ((ProdigyCard) card).isShowNumberModified = true;
        }
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
