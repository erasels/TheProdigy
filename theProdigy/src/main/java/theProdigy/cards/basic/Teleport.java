package theProdigy.cards.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.util.CardInfo;

import static theProdigy.TheProdigy.makeID;
import static theProdigy.util.UC.*;

public class Teleport extends ProdigyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Teleport",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 7;

    public Teleport() {
        super(cardInfo, false);

        setBlock(BLOCK);
        setMPCost(5, -2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doDef(block);
    }

    @Override
    public void empoweredUse(AbstractPlayer p, AbstractMonster m) {
        doDef(block);
    }
}