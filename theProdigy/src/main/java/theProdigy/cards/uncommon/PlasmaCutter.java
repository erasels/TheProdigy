package theProdigy.cards.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.util.CardInfo;
import theProdigy.vfx.combat.BetterSweepingBeamEffect;

import static theProdigy.TheProdigy.makeID;
import static theProdigy.util.UC.*;

public class PlasmaCutter extends ProdigyCard {
    private final static CardInfo cardInfo = new CardInfo(
            "PlasmaCutter",
            2,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 1;

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    private static final int MAGIC_2 = 3;

    private static final int MPCOST = 4;

    public PlasmaCutter() {
        super(cardInfo, false, AlignedStance.OCCULT);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
        setMPCost(MPCOST);
        baseMagicNumber2 = magicNumber2 = MAGIC_2;
        setMultiDamage(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber2; i++) {
            doVfx(new BetterSweepingBeamEffect(p, Color.FIREBRICK.cpy()));
            doAllDmg(damage, AbstractGameAction.AttackEffect.FIRE, damageTypeForTurn, false);
            atb(new WaitAction(Settings.ACTION_DUR_XFAST));
        }
    }

    @Override
    public void empoweredUse(AbstractPlayer p, AbstractMonster m) {
        for(AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if(!mon.isDeadOrEscaped()) {
                doPow(mon, new VulnerablePower(mon, magicNumber, false));
            }
        }
    }
}