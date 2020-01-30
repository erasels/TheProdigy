package theProdigy.cards.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.util.CardInfo;
import theProdigy.util.UC;
import theProdigy.vfx.general.RunAnimationEffect;

import static theProdigy.TheProdigy.makeID;
import static theProdigy.util.UC.doAnim;

public class Defend extends ProdigyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Defend",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.BASIC
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 3;

    public Defend() {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        doAnim(RunAnimationEffect.ANIS.GUARDA);
        UC.doDef(block);
    }
}