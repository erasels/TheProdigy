package theProdigy.cards.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.util.CardInfo;
import theProdigy.vfx.combat.BubbleEffect;

import static theProdigy.TheProdigy.makeID;
import static theProdigy.util.UC.*;

public class Reinforce extends ProdigyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Reinforce",
            1,
            CardType.SKILL,
            CardTarget.SELF);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 1;

    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    private static final int MPCOST = 3;

    public Reinforce() {
        super(cardInfo, true, AlignedStance.NONE);

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);
        setMPCost(MPCOST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doVfx(new BubbleEffect(Color.ROYAL.cpy(), "GUARDIAN_ROLL_UP"));
        doDef(block);
    }

    @Override
    public void empoweredUse(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(magicNumber));
    }
}