package theProdigy.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.util.UC;

public class DoActionIfMonsterDeadAction extends AbstractGameAction {
    AbstractGameAction action;
    public DoActionIfMonsterDeadAction(AbstractMonster m, AbstractGameAction action) {
        target = m;
        this.action = action;
    }

    @Override
    public void update() {
        if(target != null && target.isDeadOrEscaped()) {
            UC.att(action);
        }

        isDone = true;
    }
}
