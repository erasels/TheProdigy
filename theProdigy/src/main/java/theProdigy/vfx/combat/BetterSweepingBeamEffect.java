package theProdigy.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import theProdigy.util.UC;

public class BetterSweepingBeamEffect extends SweepingBeamEffect {
    public BetterSweepingBeamEffect(AbstractCreature c, Color col) {
        this(c.hb.cX + (c.hb.width/4f), c.hb.cY, col);
    }

    public BetterSweepingBeamEffect(float sX, float sY, Color col) {
        super(sX, sY, UC.p().flipHorizontal);
        this.color = col;
    }
}
