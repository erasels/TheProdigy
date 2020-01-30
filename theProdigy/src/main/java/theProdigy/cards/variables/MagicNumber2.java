package theProdigy.cards.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theProdigy.cards.abstracts.ProdigyCard;

public class MagicNumber2 extends DynamicVariable {
    @Override
    public String key() {
        return "theProdigy:M2";
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof ProdigyCard) {
            return ((ProdigyCard) card).baseMagicNumber2;
        }
        return -1;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof ProdigyCard) {
            return ((ProdigyCard) card).magicNumber2;
        }
        return -1;
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof ProdigyCard) {
            return ((ProdigyCard) card).isMagicNumber2Modified;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof ProdigyCard) {
            ((ProdigyCard) card).isMagicNumber2Modified = true;
        }
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
