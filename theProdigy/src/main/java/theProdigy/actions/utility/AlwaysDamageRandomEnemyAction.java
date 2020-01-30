package theProdigy.actions.utility;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class AlwaysDamageRandomEnemyAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;

    public AlwaysDamageRandomEnemyAction(DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.setValues(null, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    public void update() {
        target = AbstractDungeon.getMonsters().getRandomMonster( null, true, AbstractDungeon.cardRandomRng);
        if (shouldCancelAction()) {
            isDone = true;
        } else if (target != null && !target.isDeadOrEscaped()) {
            if (duration == DURATION) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            }

            tickDuration();
            if (isDone) {
                if (attackEffect == AttackEffect.POISON) {
                    target.tint.color = Color.CHARTREUSE.cpy();
                    target.tint.changeColor(Color.WHITE.cpy());
                } else if (attackEffect == AttackEffect.FIRE) {
                    target.tint.color = Color.RED.cpy();
                    target.tint.changeColor(Color.WHITE.cpy());
                }

                target.damage(info);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));
            }

        } else {
            isDone = true;
        }
    }
}