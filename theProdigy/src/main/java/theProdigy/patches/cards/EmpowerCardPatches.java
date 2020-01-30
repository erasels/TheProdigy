package theProdigy.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.util.UC;

public class EmpowerCardPatches {
    //Reset Empowered status if it leaves the hand
    @SpirePatch(clz = CardGroup.class, method = "moveToDiscardPile")
    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    @SpirePatch(clz = CardGroup.class, method = "moveToBottomOfDeck")
    @SpirePatch(clz = CardGroup.class, method = "resetCardBeforeMoving")
    public static class StaysInHand {
        @SpirePostfixPatch
        public static void patch(CardGroup __instance, AbstractCard card) {
            if (UC.isEmpowered(card)) {
                ((ProdigyCard) card).isEmpowered = false;
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToDeck")
    public static class StaysInHand2 {
        @SpirePostfixPatch
        public static void patch(CardGroup __instance, AbstractCard card, boolean randomSpot) {
            if (UC.isEmpowered(card) && __instance == AbstractDungeon.player.hand && AbstractDungeon.player.hand.contains(card)) {
                ((ProdigyCard) card).isEmpowered = false;
            }
        }
    }
}
