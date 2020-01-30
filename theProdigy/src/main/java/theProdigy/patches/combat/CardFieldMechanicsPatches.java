package theProdigy.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class CardFieldMechanicsPatches {

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class PlayerFields {
        public static SpireField<Boolean> isBurst = new SpireField<>(() -> false);
        public static SpireField<Integer> turnBurstAmount = new SpireField<>(() -> 0);
    }

    //Should take care of clearing burst from last combat as well
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
    public static class TurnEndBurstReset {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __instance) {
            PlayerFields.isBurst.set(AbstractDungeon.player, false);
            PlayerFields.turnBurstAmount.set(AbstractDungeon.player, 0);
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class CaptureBurstActivation {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(UseCardAction __instance, AbstractCard c, AbstractCreature target) {
            if(c.type == AbstractCard.CardType.ATTACK) {
                PlayerFields.isBurst.set(AbstractDungeon.player, true);
            } else {
                PlayerFields.isBurst.set(AbstractDungeon.player, false);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
