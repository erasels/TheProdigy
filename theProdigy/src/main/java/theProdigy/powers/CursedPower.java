package theProdigy.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

public class CursedPower extends AbstractSacredPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheProdigy.makeID("Cursed");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CursedPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        type = PowerType.DEBUFF;
        updateDescription();
        isTurnBased = false;
        loadRegion("cExplosion");
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void onDeath() {
        owner.powers.stream()
                .filter(p -> p.type == PowerType.DEBUFF)
                .forEach(d -> UC.getAliveMonsters()
                        .forEach(m -> {
                            if (d instanceof CloneablePowerInterface && !(d instanceof CursedPower)) {
                                UC.doPow(owner, m, ((CloneablePowerInterface) d).makeCopy(), false);
                            }
                        }));
    }

    @Override
    public AbstractPower makeCopy() {
        return new CursedPower(owner);
    }
}

