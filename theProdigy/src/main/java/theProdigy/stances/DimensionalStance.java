package theProdigy.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
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

    //TODO: Add star (wallop) particles as stance effect


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
