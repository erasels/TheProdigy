package theProdigy.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

import java.util.ArrayList;
import java.util.Arrays;

import static theProdigy.util.UC.doPow;
import static theProdigy.util.UC.p;

public class WheelOfMisfortunePower extends AbstractSacredPower implements CloneablePowerInterface, OnReceivePowerPower {
    public static final String POWER_ID = TheProdigy.makeID("WheelOfMisfortune");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static ArrayList<String> nonApplicablePowers = new ArrayList<>(Arrays.asList(DrawReductionPower.POWER_ID, ConfusionPower.POWER_ID, HexPower.POWER_ID, EntanglePower.POWER_ID));
    private static final int POISON_AMT = 8;

    public WheelOfMisfortunePower(int amount, AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        isTurnBased = false;
        loadRegion("artifact");
        priority = 3;
    }

    public WheelOfMisfortunePower(int amount) {
        this(amount, p());
    }

    @Override
    public boolean onReceivePower(AbstractPower p, AbstractCreature t, AbstractCreature s) {
        if(p.type == PowerType.DEBUFF && t == owner && s != owner) {
            UC.generalPowerLogic(this, true);
            if(s instanceof AbstractMonster) {
                if (nonApplicablePowers.contains(p.ID)) {
                    p = new PoisonPower(s, owner, POISON_AMT);
                } else {
                    p.owner = s;
                }
                doPow(owner, s, p, false);
            }
            return false;
        }
        return true;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + POISON_AMT + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new WheelOfMisfortunePower(amount, owner);
    }
}