package theProdigy.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;

import static theProdigy.util.UC.p;

public class LimitPower extends AbstractSacredPower implements CloneablePowerInterface, OnReceivePowerPower {
    public static final String POWER_ID = TheProdigy.makeID("Limit");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LimitPower(int amount, AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        isTurnBased = false;
        loadRegion("barricade");
        priority = -99;
    }

    public LimitPower() {
        this(0, p());
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new LimitPower(amount, owner);
    }

    @Override
    public boolean onReceivePower(AbstractPower p, AbstractCreature t, AbstractCreature s) {
        if(t == p() && (p instanceof IntangiblePlayerPower || p instanceof IntangiblePower)) {
            return false;
        }
        return true;
    }
}