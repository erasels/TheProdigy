package theProdigy.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.Settings;
import theProdigy.util.UC;

public class ShuffleIntoDrawAndDrawAction extends AbstractGameAction {
    private float startingDuration;

    public ShuffleIntoDrawAndDrawAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        UC.att(new DrawCardAction(UC.p(), UC.p().hand.size()));
        while(!UC.p().hand.isEmpty()) {
            UC.p().hand.moveToDeck(UC.p().hand.getTopCard(), true);
        }
        isDone = true;
    }
}