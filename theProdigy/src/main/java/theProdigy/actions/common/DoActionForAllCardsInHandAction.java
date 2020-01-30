package theProdigy.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import theProdigy.util.UC;

import java.util.ArrayList;
import java.util.function.Consumer;

public class DoActionForAllCardsInHandAction extends AbstractGameAction {
    private Consumer<ArrayList<AbstractCard>> callback1;
    private Consumer<AbstractCard> callback2;

    public DoActionForAllCardsInHandAction(Consumer<ArrayList<AbstractCard>> callback) {
        this.callback1 = callback;
    }

    public DoActionForAllCardsInHandAction(int x, Consumer<AbstractCard> callback) {
        this.callback2 = callback;
    }

    @Override
    public void update() {
        if(callback1 != null) {
            callback1.accept(UC.p().hand.group);
        } else {
            for(AbstractCard c : UC.p().hand.group) {
                callback2.accept(c);
            }
        }
        isDone = true;
    }
}
