package theProdigy.stances;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
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

    //TODO: Add dark particles as stance effect


    @Override
    public void onApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower p) {
        if(source == UC.p() && target != UC.p() && p.type == AbstractPower.PowerType.DEBUFF) {
            UC.doDmg(target, DEBUFF_DAMAGE, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.POISON, true);
        }
    }
}
