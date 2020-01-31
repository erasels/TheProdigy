package theProdigy.stances;

import com.badlogic.gdx.graphics.Color;
import theProdigy.TheProdigy;
import theProdigy.actions.common.ChangeManaAction;
import theProdigy.util.UC;

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

    //TODO: Add frost/Lightning/fire particles as stance effect

    @Override
    public void atStartOfTurn() {
        UC.atb(new ChangeManaAction(MP_GAIN));
    }
}
