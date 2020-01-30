package theProdigy.relics.special;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theProdigy.TheProdigy;
import theProdigy.relics.abstracts.ProdigyRelic;
import theProdigy.util.UC;

import java.util.ArrayList;

public class PurificationRod extends ProdigyRelic {
    public static final String ID = TheProdigy.makeID("PurificationRod");

    private static final int STACK = 1;
    private ArrayList<AbstractMonster> hitList = new ArrayList<>();

    public PurificationRod() {
        super(ID, "PurificationRod.png", AbstractRelic.RelicTier.STARTER, LandingSound.SOLID);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(target instanceof AbstractMonster) {
            if (info.type == DamageInfo.DamageType.NORMAL && info.owner == UC.p()  && target != UC.p() && !hitList.contains(target)) {
                hitList.add((AbstractMonster) target);
                UC.doPow(target, new WeakPower(target, STACK, false));
                UC.doPow(target, new VulnerablePower(target, STACK, false));
            }
        }
    }

    @Override
    public void onVictory() {
        hitList.clear();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STACK + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new PurificationRod();
    }
}