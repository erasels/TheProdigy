package theProdigy.powers.turn;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

import static theProdigy.util.UC.p;

public class BlessingOfProtectionPower extends AbstractSacredPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheProdigy.makeID("BlessingOfProtection");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final float BLK_MOD = 1.25f;

    public BlessingOfProtectionPower(int amount, AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        isTurnBased = true;
        loadRegion("barricade");
    }

    public BlessingOfProtectionPower(int amount) {
        this(amount, p());
    }

    @Override
    public void atStartOfTurn() {
        UC.generalPowerLogic(this);
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount*BLK_MOD;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + UC.getPercentageInc(BLK_MOD) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlessingOfProtectionPower(amount, owner);
    }
}