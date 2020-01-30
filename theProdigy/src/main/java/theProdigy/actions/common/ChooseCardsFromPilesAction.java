package theProdigy.actions.common;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import theProdigy.patches.cards.RenderCurrentPilePatches;
import theProdigy.util.UC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static theProdigy.TheProdigy.makeID;

public class ChooseCardsFromPilesAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ChooseCardsFromPilesAction"));

    public static final String[] TEXT = uiStrings.TEXT;

    private ArrayList<CardGroup> piles;
    private Predicate<AbstractCard> cardFilter;
    private Map<AbstractCard, CardGroup> relationMap = new HashMap<>();

    public ChooseCardsFromPilesAction(ArrayList<CardGroup> piles, Predicate<AbstractCard> cardFilter, int num) {
        setValues(null, UC.p(), num);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FASTER;
        this.piles = piles;
        this.cardFilter = cardFilter;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            this.isDone = true;
            return;
        }
        if (this.duration == Settings.ACTION_DUR_FASTER) {
            for(CardGroup pile : piles) {
                for(AbstractCard c : pile.group) {
                    if(cardFilter.test(c)) {
                        relationMap.put(c, pile);
                        RenderCurrentPilePatches.CurrentPileField.set(c, pile);
                    }
                }
            }
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            tmp.group.addAll(relationMap.keySet());
            if (relationMap.isEmpty()) {
                this.isDone = true;
                return;
            } else if (relationMap.size() <= amount) {
                for(Map.Entry<AbstractCard, CardGroup> vals: relationMap.entrySet()) {
                    RenderCurrentPilePatches.CurrentPileField.pileEnum.set(vals.getKey(), RenderCurrentPilePatches.pileRender.UNSPECIFIED);
                    if(UC.p().hand.size() < BaseMod.MAX_HAND_SIZE) {
                        AbstractCard card = vals.getKey();
                        card.unhover();
                        card.lighten(true);
                        card.setAngle(0.0F);
                        card.drawScale = 0.12F;
                        card.targetDrawScale = 0.75F;
                        card.current_x = MathUtils.random(200f, Settings.WIDTH - 200f);
                        card.current_y = Settings.HEIGHT * 0.7f;
                        vals.getValue().removeCard(card);
                        AbstractDungeon.player.hand.addToTop(card);
                        AbstractDungeon.player.hand.refreshHandLayout();
                        AbstractDungeon.player.hand.applyPowers();
                    } else {
                        vals.getValue().moveToDiscardPile(vals.getKey());
                        UC.p().createHandIsFullDialog();
                    }
                }
                tickDuration();
                return;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, amount, TEXT[0] + amount + TEXT[1], false, false, false, false);
                tickDuration();
            }
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for(AbstractCard c : AbstractDungeon.gridSelectScreen.targetGroup.group) {
                RenderCurrentPilePatches.CurrentPileField.pileEnum.set(c, RenderCurrentPilePatches.pileRender.UNSPECIFIED);
            }

            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                if(!(UC.p().hand.size() < BaseMod.MAX_HAND_SIZE)) {
                    relationMap.get(c).moveToDiscardPile(c);
                    UC.p().createHandIsFullDialog();
                } else {
                    relationMap.get(c).removeCard(c);
                    AbstractDungeon.player.hand.addToTop(c);
                }
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            UC.p().hand.refreshHandLayout();
        }
        tickDuration();
    }
}
