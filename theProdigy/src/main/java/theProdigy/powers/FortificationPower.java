package theProdigy.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;

import static theProdigy.util.UC.p;

public class FortificationPower extends AbstractSacredPower implements CloneablePowerInterface, OnReceivePowerPower {
    public static final String POWER_ID = TheProdigy.makeID("Fortification");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FortificationPower(int amount, AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = AbstractPower.PowerType.BUFF;
        updateDescription();
        isTurnBased = false;
        loadRegion("barricade");
    }

    public FortificationPower(int amount) {
        this(amount, p());
    }

    @Override
    public void onInitialApplication() {
        p().powers.stream()
                .filter(p -> p instanceof AbstractSacredPower && ((AbstractSacredPower)p).isBarrierPower)
                .forEach(p->((AbstractSacredPower) p).extendBarrier(this.amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        p().powers.stream()
                .filter(p -> p instanceof AbstractSacredPower && ((AbstractSacredPower)p).isBarrierPower)
                .forEach(p->((AbstractSacredPower) p).extendBarrier(stackAmount));
    }

    @Override
    public boolean onReceivePower(AbstractPower p, AbstractCreature target, AbstractCreature source) {
        if(target == p()) {
            if(p instanceof AbstractSacredPower && ((AbstractSacredPower) p).isBarrierPower) {
                ((AbstractSacredPower) p).extendBarrier(amount);
            }
        }
        return true;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FortificationPower(amount);
    }
}


