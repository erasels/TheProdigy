package theProdigy.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import theProdigy.util.UC;

public class MoveCardFromHandToDeckAction extends AbstractGameAction {
    private AbstractCard c;
    public MoveCardFromHandToDeckAction(AbstractCard c) {
        this.c = c;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(duration == startDuration) {
            UC.p().hand.moveToDeck(c, true);
        }
        tickDuration();
    }
}
