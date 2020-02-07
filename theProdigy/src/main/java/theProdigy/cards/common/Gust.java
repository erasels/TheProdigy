package theProdigy.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WindyParticleEffect;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.util.CardInfo;

import static theProdigy.TheProdigy.makeID;
import static theProdigy.util.UC.*;

public class Gust extends ProdigyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Gust",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 2;

    private static final int MAGIC = 2;
    private static final int MAGIC_2 = 1;

    public Gust() {
        super(cardInfo, true, AlignedStance.ELEMENTAL);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC);
        magicNumber2 = baseMagicNumber2 = MAGIC_2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < 13; i++) {
            doVfx(new WindyParticleEffect());
        }
        doDmg(m, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        doDraw(magicNumber);
        atb(new DiscardAction(p, p, magicNumber2, !upgraded));
    }
}