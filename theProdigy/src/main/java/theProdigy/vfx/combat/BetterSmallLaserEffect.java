package theProdigy.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class BetterSmallLaserEffect extends SmallLaserEffect {
    public BetterSmallLaserEffect(AbstractCreature c, float dX, float dY, Color col) {
        this(c.hb.cX + (c.hb.width/4f), c.hb.cY, dX, dY, col);
    }

    public BetterSmallLaserEffect(float sX, float sY, float dX, float dY, Color col) {
        super(sX, sY, dX, dY);
        this.color = col;
    }
}
