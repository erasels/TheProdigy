package theProdigy.powers.turn.barriers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

import static theProdigy.util.UC.p;

public class AnachronicBarrierPower extends AbstractSacredPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheProdigy.makeID("AnachronicBarrier");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean justApplied;

    public AnachronicBarrierPower(int amount2, AbstractCreature owner) {
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
        this.priority = 1;
        justApplied = false;
    }

    @Override
    public void onInitialApplication() {
        justApplied = true;
    }

    public AnachronicBarrierPower(int amount2) {
        this(amount2, p());
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer) {
            if(!justApplied) {
                UC.reducePower(this);
            } else {
                justApplied = false;
            }
        }
    }

    @Override
    public void onRemove() {
        if(amount2 > 0) {
            retaliate(owner);
        }
    }

    @Override
    public void retaliate(AbstractCreature target) {
        UC.atb(new SFXAction("POWER_TIME_WARP"));
        UC.doDmg(target, amount2, DamageInfo.DamageType.THORNS);
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            amount2+=damageAmount;
            return 0;
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }

    //TODO: Add generate particle vfx that renders a barrier?

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        AnachronicBarrierPower tmp = new AnachronicBarrierPower(amount2, owner);
        tmp.amount = this.amount;
        return tmp;
    }
}