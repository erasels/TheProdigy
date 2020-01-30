package theProdigy.actions.utility;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theProdigy.util.UC;

import java.util.function.Consumer;

public class ClearAllDebuffsAction extends AbstractGameAction {
    private Consumer<AbstractPower> callback;

    public ClearAllDebuffsAction(AbstractCreature target) {
        this(target, null);
    }

    public ClearAllDebuffsAction(AbstractCreature target, Consumer<AbstractPower> callback) {
        this.target = target;
        this.callback = callback;
    }

    public void update() {
        for (AbstractPower power : target.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF && !(power instanceof InvisiblePower)) {
                UC.att(new RemoveSpecificPowerAction(target, target, power));
                if(callback != null) {
                    callback.accept(power);
                }
            }
        }
        isDone = true;
    }
}
