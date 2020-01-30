package theProdigy.powers.turn.barriers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnMyBlockBrokenPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import theProdigy.TheProdigy;
import theProdigy.powers.abstracts.AbstractSacredPower;
import theProdigy.util.UC;

import static theProdigy.util.UC.*;

public class UnstableBarrierPower extends AbstractSacredPower implements CloneablePowerInterface, OnMyBlockBrokenPower {
    public static final String POWER_ID = TheProdigy.makeID("UnstableBarrier");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public UnstableBarrierPower(int amount2, AbstractCreature owner) {
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

    public UnstableBarrierPower(int amount2) {
        this(amount2, p());
    }

    @Override
    public void atStartOfTurn() {
        UC.generalPowerLogic(this);
    }

    @Override
    public void onMyBlockBroken() {
        retaliate(null);
    }

    @Override
    public void retaliate(AbstractCreature target) {
        flash();
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDeadOrEscaped())
                doVfx(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
        }
        doAllDmg(amount2, AbstractGameAction.AttackEffect.NONE, DamageInfo.DamageType.THORNS, false);
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
        UnstableBarrierPower tmp = new UnstableBarrierPower(amount2, owner);
        tmp.amount = this.amount;
        return tmp;
    }
}