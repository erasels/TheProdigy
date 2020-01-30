package theProdigy.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EndTurnDiscardAction extends AbstractGameAction {
    public EndTurnDiscardAction() {
        this.actionType = ActionType.DISCARD;
        this.duration = 0.1f;
    }

    public void update() {
        if (this.duration == 0.1f) {
            for (int i = AbstractDungeon.player.hand.size() - 1; i >= 0; --i)
            {
                AbstractDungeon.player.hand.moveToDiscardPile(AbstractDungeon.player.hand.group.get(i));
                GameActionManager.incrementDiscard(true);
            }

            AbstractDungeon.player.hand.applyPowers();
        }

        this.tickDuration();
    }
}