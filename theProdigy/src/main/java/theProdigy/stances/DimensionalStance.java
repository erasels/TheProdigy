package theProdigy.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import theProdigy.TheProdigy;
import theProdigy.actions.common.ChangeManaAction;
import theProdigy.util.UC;

public class DimensionalStance extends ProdigyStance {
    public static final String ID = TheProdigy.makeID("Dimensional");
    private static final int MP_LOSS = 4;
    private static final float NUMBER_MOD = 0.5f;

    public DimensionalStance() {
        super(ID, Color.PINK.cpy());
    }

    @Override
    public void updateDescription() {
        description = stanceString.DESCRIPTION[0] + MP_LOSS + stanceString.DESCRIPTION[1] + MathUtils.round(NUMBER_MOD*100f) + stanceString.DESCRIPTION[2];
    }

    private static final float EFFECT_INTERVAL = 0.85f;
    private static final float TRANSITION_TIME = 2f;
    private float transition;
    private float c1 = Color.PINK.r, c2 = Color.PINK.g, c3 = Color.PINK.b;
    private float g1 = Color.ROYAL.r, g2 = Color.ROYAL.g, g3 = Color.ROYAL.b;

    @Override
    public void updateAnimation() {
        if(particleTimer <= 0) {
            float x1 = UC.p().hb.x;
            float x2 = UC.p().hb.x + UC.p().hb.width;
            float y1 = UC.p().hb.y;
            float y2 = UC.p().hb.y + UC.p().hb.height;
            for (int i = 0; i < MathUtils.random(4); i++) {
                if(MathUtils.randomBoolean(0.35f)) {
                    AbstractDungeon.effectsQueue.add(new StarBounceEffect(MathUtils.random(x1, x2), MathUtils.random(y1, y2)));
                } else {
                    AbstractDungeon.effectsQueue.add(new StunStarEffect(MathUtils.random(x1, x2), MathUtils.random(y1, y2)));
                }
            }
            particleTimer = EFFECT_INTERVAL;
        }
        particleTimer -= UC.gt();

        transition += UC.gt();
        float val = transition / TRANSITION_TIME;
        float tmp1 = c1 + (g1 - c1) * val;
        float tmp2 = c2 + (g2 - c2) * val;
        float tmp3 = c3 + (g3 - c3) * val;
        if(transition >= TRANSITION_TIME)
            transition = 0;

        updateAnimation(tmp1, tmp2, tmp3);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if(type == DamageInfo.DamageType.NORMAL) {
            return damage * (1f + NUMBER_MOD);
        }
        return damage;
    }

    @Override
    public float modifyBlock(float block) {
        return block * (1f + NUMBER_MOD);
    }

    //Maybe change to mana gain on card played
    @Override
    public void atStartOfTurn() {
        UC.atb(new ChangeManaAction(-MP_LOSS));
    }
}
