package theProdigy.powers.turn;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

import static theProdigy.util.UC.p;

public class DuplexBoundaryPower extends AbstractSacredPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheProdigy.makeID("DuplexBoundary");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final float DMG_MULTI = 0.5f;

    public DuplexBoundaryPower(int amount, AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        isTurnBased = true;
        loadRegion("barricade");
    }

    public DuplexBoundaryPower(int amount) {
        this(amount, p());
    }

    @Override
    public void atStartOfTurn() {
        UC.generalPowerLogic(this);
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
            if (type == DamageInfo.DamageType.NORMAL) {
                return damage * DMG_MULTI;
            }
            return damage;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + MathUtils.round(DMG_MULTI*100f) + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DuplexBoundaryPower(amount, owner);
    }
}