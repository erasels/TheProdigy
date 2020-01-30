package theProdigy.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HealForPowerAmountAction extends AbstractGameAction {
    private String PID;

    public HealForPowerAmountAction(AbstractCreature target, String PowerID) {
        this.target = target;
        this.PID = PowerID;
    }

    public void update() {
        if (target != null && !target.isDeadOrEscaped()) {
            AbstractPower p = target.getPower(PID);
            if (p != null) {
                target.heal(p.amount, true);
            }
        }
        isDone = true;
    }
}
