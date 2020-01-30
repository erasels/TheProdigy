package theProdigy.actions.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import theProdigy.util.UC;
import theProdigy.vfx.combat.FlameBurstEffect;
import theProdigy.vfx.combat.MoveImageWithSparkleEffect;

import java.util.ArrayList;

public class SwitchPilesAction extends AbstractGameAction {

    public SwitchPilesAction() {
        super();
    }

    public void update() {
        if (!UC.p().drawPile.isEmpty()) {
            AbstractDungeon.effectsQueue.add(new MoveImageWithSparkleEffect(CardGroup.DRAW_PILE_X,
                    CardGroup.DRAW_PILE_Y,
                    CardGroup.DISCARD_PILE_X,
                    CardGroup.DISCARD_PILE_Y,
                    ImageMaster.DECK_BTN_BASE,
                    Color.ORANGE.cpy(),
                    "POWER_TIME_WARP",
                    new FlameBurstEffect(CardGroup.DISCARD_PILE_X, CardGroup.DISCARD_PILE_Y, 20)
            ));
        }
        if (!UC.p().discardPile.isEmpty()) {
            AbstractDungeon.effectsQueue.add(new MoveImageWithSparkleEffect(CardGroup.DISCARD_PILE_X,
                    CardGroup.DISCARD_PILE_Y,
                    CardGroup.DRAW_PILE_X,
                    CardGroup.DRAW_PILE_Y,
                    ImageMaster.DECK_BTN_BASE,
                    Color.BLUE.cpy(),
                    "POWER_TIME_WARP",
                    new FlameBurstEffect(CardGroup.DRAW_PILE_X, CardGroup.DRAW_PILE_Y, 20)
            ));
        }
        ArrayList<AbstractCard> tmp = UC.p().discardPile.group;
        UC.p().discardPile.group = UC.p().drawPile.group;
        UC.p().drawPile.group = tmp;
        isDone = true;
    }
}
