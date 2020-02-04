package theProdigy.cards.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.actions.utility.AlwaysDamageRandomEnemyAction;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.util.CardInfo;

import static theProdigy.TheProdigy.makeID;
import static theProdigy.util.UC.*;

public class UnrulySpark extends ProdigyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "UnrulySpark",
            2,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 4;
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;
    private static final int DMG_TIMES = 3;

    private static final int MPCOST = 2;

    public UnrulySpark() {
        super(cardInfo, true, AlignedStance.ELEMENTAL);

        setDamage(DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        setMPCost(MPCOST);
        baseMagicNumber2 = magicNumber2 = DMG_TIMES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber2 + (isEmpowered?magicNumber:0); i++) {
            atb(new AlwaysDamageRandomEnemyAction(new DamageInfo(p, damage), AbstractGameAction.AttackEffect.LIGHTNING));
        }
    }
}