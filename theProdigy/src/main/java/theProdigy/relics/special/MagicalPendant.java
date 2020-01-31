package theProdigy.relics.special;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import theProdigy.TheProdigy;
import theProdigy.actions.common.ChangeManaAction;
import theProdigy.relics.abstracts.ProdigyRelic;
import theProdigy.util.UC;

public class MagicalPendant extends ProdigyRelic {
    public static final String ID = TheProdigy.makeID("MagicalPendant");

    private static final int MP_GAIN = 2;

    public MagicalPendant() {
        super(ID, "MagicalPendant.png", AbstractRelic.RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public void onChangeStance(AbstractStance prevStance, AbstractStance newStance) {
        flash();
        UC.atb(new ChangeManaAction(MP_GAIN));
    }
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MP_GAIN + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new MagicalPendant();
    }
}