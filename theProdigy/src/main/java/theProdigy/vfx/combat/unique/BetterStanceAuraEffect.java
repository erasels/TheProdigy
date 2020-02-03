package theProdigy.vfx.combat.unique;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public class BetterStanceAuraEffect extends StanceAuraEffect {
    public BetterStanceAuraEffect(float c1a, float c1b, float c2a, float c2b, float c3a, float c3b) {
        this(MathUtils.random(c1a, c1b), MathUtils.random(c2a, c2b), MathUtils.random(c3a, c3b));
    }

    public BetterStanceAuraEffect(float c1, float c2, float c3) {
        super("");
        color = new Color(c1, c2, c3, 0);
    }
}
