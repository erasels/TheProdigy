package theProdigy.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import theProdigy.util.UC;

public class BetterSweepingBeamEffect extends SweepingBeamEffect {
    public BetterSweepingBeamEffect(float sX, float sY, Color col) {
        super(sX, sY, UC.p().flipHorizontal);
        this.color = col;
    }
}
