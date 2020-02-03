package theProdigy.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.DarkSmokePuffEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;
import theProdigy.TheProdigy;
import theProdigy.util.UC;

public class OccultStance extends ProdigyStance {
    public static final String ID = TheProdigy.makeID("Occult");
    private static final int DEBUFF_DAMAGE = 3;

    public OccultStance() {
        super(ID, Color.PURPLE.cpy());
    }

    @Override
    public void updateDescription() {
        description = stanceString.DESCRIPTION[0] + DEBUFF_DAMAGE + stanceString.DESCRIPTION[1];
    }

    private static final float EFFECT_INTERVAL = 0.7f;
    private static final float PUFF_INTERVAL = 2f;
    private float darkTimer;
    @Override
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            if (particleTimer <= 0) {
                float x1 = UC.p().hb.x;
                float x2 = UC.p().hb.x + UC.p().hb.width;
                float y1 = UC.p().hb.y;
                float y2 = UC.p().hb.y + UC.p().hb.height;

                if(MathUtils.randomBoolean(0.8f)) {
                    for (int i = 0; i < 2+(MathUtils.randomBoolean()?1:0); i++) {
                        AbstractDungeon.effectsQueue.add(new DarkOrbPassiveEffect(MathUtils.random(x1, x2), MathUtils.random(y1, y2)));
                    }
                } else {
                    if(darkTimer <= 0) {
                        AbstractDungeon.effectsQueue.add(new DarkSmokePuffEffect(MathUtils.random(x1, x2), MathUtils.random(y1, y2)));
                        darkTimer = PUFF_INTERVAL;
                    }
                }
                particleTimer = EFFECT_INTERVAL;
            }
            particleTimer -= UC.gt();
            darkTimer -= UC.gt();
        }

        super.updateAnimation(0.25f, 0.05f, 0.5f);
    }

    @Override
    public void onApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower p) {
        if(source == UC.p() && target != UC.p() && p.type == AbstractPower.PowerType.DEBUFF) {
            UC.doDmg(target, DEBUFF_DAMAGE, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.POISON, true);
        }
    }
}
