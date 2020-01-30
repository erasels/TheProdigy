package theProdigy.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

import static theProdigy.util.UC.p;

public class SlaveSealsPower extends AbstractSacredPower implements CloneablePowerInterface {
    public static final String POWER_ID = TheProdigy.makeID("SlaveSeals");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SlaveSealsPower(int amount, AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        updateDescription();
        isTurnBased = false;
        loadRegion("barricade");
    }

    public SlaveSealsPower(int amount) {
        this(amount, p());
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        //TODO: Some cool Vfx, idk
        if(info.type == DamageInfo.DamageType.NORMAL && info.owner == UC.p() && target != UC.p() && damageAmount > 0) {
            AbstractMonster t = (AbstractMonster) target;
            for (int i = 0; i < amount ; i++) {
                t = AbstractDungeon.getRandomMonster(t);
                UC.doDmg(t, damageAmount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true);
            }
        }
    }

    //TODO: In generateParticles, StunStar effect in a circle around the player for power amount?

    @Override
    public void updateDescription() {
        if(amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[2] + DESCRIPTIONS[4];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3] + DESCRIPTIONS[4];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SlaveSealsPower(amount, owner);
    }
}