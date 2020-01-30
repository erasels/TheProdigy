package theProdigy.powers.turn.barriers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

import static theProdigy.util.UC.doDmg;
import static theProdigy.util.UC.p;

public class RepulseBarrierPower extends AbstractSacredPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheProdigy.makeID("RepulseBarrier");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RepulseBarrierPower(int amount2, AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.amount2 = amount2;
        type = PowerType.BUFF;
        updateDescription();
        isTurnBased = true;
        loadRegion("barricade");
        isBarrierPower = true;
    }

    public RepulseBarrierPower(int amount2) {
        this(amount2, p());
    }

    @Override
    public void atStartOfTurn() {
        UC.generalPowerLogic(this);
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner) {
            retaliate(info.owner);
        }
        return damageAmount;
    }

    @Override
    public void retaliate(AbstractCreature target) {
        flash();
        doDmg(target, amount2, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true, true);
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount2 += stackAmount;
    }

    //TODO: Add generate particle vfx that renders a barrier?

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        RepulseBarrierPower tmp = new RepulseBarrierPower(amount2, owner);
        tmp.amount = this.amount;
        return tmp;
    }
}