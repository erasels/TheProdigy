package theProdigy.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import theProdigy.TheProdigy;
import theProdigy.actions.common.ChangeManaAction;
import theProdigy.util.UC;
import theProdigy.vfx.combat.unique.FlameSlingEffect;

public class ElementalStance extends ProdigyStance {
    public static final String ID = TheProdigy.makeID("Elemental");
    private static final int MP_GAIN = 3;

    public ElementalStance() {
        super(ID, Color.ROYAL.cpy());
    }

    @Override
    public void updateDescription() {
        description = stanceString.DESCRIPTION[0] + MP_GAIN + stanceString.DESCRIPTION[1];
    }

    private static final float EFFECT_INTERVAL = 0.75f;
    private float effectCooldown = 0;
    private boolean gracePeriod = false;
    private static final int FIRE_SKIPS = 2;
    private int fireSkips = 0;

    @Override
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            if (effectCooldown <= 0) {
                float x1 = UC.p().hb.x;
                float x2 = UC.p().hb.x + UC.p().hb.width;
                float y1 = UC.p().hb.y;
                float y2 = UC.p().hb.y + UC.p().hb.height;

                switch (MathUtils.random(4)) {
                    case 1:
                        AbstractDungeon.effectsQueue.add(new LightningOrbActivateEffect(MathUtils.random(x1, x2), MathUtils.random(y1, y2)));
                        break;
                    case 2:
                        AbstractDungeon.effectsQueue.add(new FrostOrbActivateEffect(MathUtils.random(x1, x2), MathUtils.random(y1, y2)));
                        break;
                    case 3:
                        for (int i = 0; i < 11; i++) {
                            AbstractDungeon.effectsQueue.add(new CalmParticleEffect());
                        }
                        break;
                    default:
                        if (fireSkips == 0) {
                            AbstractDungeon.effectsQueue.add(new FlameSlingEffect(MathUtils.random(x1, x1 + ((x2 - x1) * 0.3f)), MathUtils.random(y1, y2), MathUtils.random(x2 - ((x2 - x1) * 0.3f), x2), MathUtils.random(y1, y2)));
                            gracePeriod = true;
                            fireSkips = FIRE_SKIPS;
                        } else {
                            fireSkips--;
                        }
                }
                effectCooldown = EFFECT_INTERVAL;
                if (gracePeriod) {
                    gracePeriod = false;
                    effectCooldown += 0.3f;
                }
            }
            effectCooldown -= UC.gt();
        }

        float tmp = MathUtils.random(0.75f, 1f);
        super.updateAnimation(tmp, tmp, tmp);
    }

    //Maybe change to mana gain on card played
    @Override
    public void atStartOfTurn() {
        UC.atb(new ChangeManaAction(MP_GAIN));
    }
}
