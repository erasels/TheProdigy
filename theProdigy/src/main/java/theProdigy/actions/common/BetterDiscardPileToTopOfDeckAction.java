package theProdigy.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import theProdigy.TheProdigy;

public class BetterDiscardPileToTopOfDeckAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheProdigy.makeID("ChooseCardsFromPilesAction"));

    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;

    public BetterDiscardPileToTopOfDeckAction(int amount) {
        this.p = AbstractDungeon.player;
        setValues(null, p, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FASTER;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            isDone = true;
            return;
        }
        if (duration == Settings.ACTION_DUR_FASTER) {
            if (p.discardPile.isEmpty()) {
                isDone = true;
                return;
            }
            if (p.discardPile.size() <= amount) {
                for(AbstractCard c : p.discardPile.group) {
                    p.discardPile.removeCard(c);
                    p.discardPile.moveToDeck(c, false);
                }
                isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(p.discardPile, amount, TEXT[0] + amount + TEXT[1], false, false, false, false);
                tickDuration();
            }
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                p.discardPile.removeCard(c);
                p.hand.moveToDeck(c, false);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
