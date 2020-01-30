package theProdigy.actions.unique;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.patches.combat.OverkillHookPatches;
import theProdigy.util.UC;

import java.util.ArrayList;

public class TargetingTalismanAction extends DamageAction {
    protected DamageInfo info;

    public TargetingTalismanAction(AbstractCreature target, DamageInfo info, AttackEffect effect, boolean superFast) {
        super(target, info, effect, superFast);
        OverkillHookPatches.OverkillDamage.callbackAction.set(info, this);
    }

    @Override
    public void update() {
        super.update();
        if(isDone && (target == null || target.isDeadOrEscaped()) && amount > 0) {
            ArrayList<AbstractMonster> aM = UC.getAliveMonsters();
            AbstractCreature t = null;
            for(AbstractMonster m : aM) {
                if(t == null || t.currentHealth > m.currentHealth) {
                    t = m;
                }
            }
            if(t != null) {
                UC.att(new TargetingTalismanAction(t, new DamageInfo(UC.p(), amount * 2, DamageInfo.DamageType.NORMAL), AttackEffect.FIRE, false));
                UC.att(new WaitAction(Settings.FAST_MODE?Settings.ACTION_DUR_FAST:Settings.ACTION_DUR_MED));
            }
        }
    }
}
