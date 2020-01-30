package theProdigy.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import javassist.CtBehavior;

import java.util.function.Consumer;

public class MakeTempCardInDrawCallbackPatches {
    public static MakeTempCardInDrawPileAction getAction(MakeTempCardInDrawPileAction action, Consumer<AbstractCard> callback) {
        CallbackVar.callback.set(action, callback);
        return action;
    }

    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = SpirePatch.CLASS)
    public static class CallbackVar {
        public static SpireField<Consumer<AbstractCard>> callback = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = "update")
    public static class WhyAreDevsLikeThis {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void patch(MakeTempCardInDrawPileAction __instance, @ByRef AbstractCard[] c) {
            if(CallbackVar.callback.get(__instance) != null) {
                CallbackVar.callback.get(__instance).accept(c[0]);
                UnfuckCardVar.shouldUnfuck.set(c[0], true);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(ShowCardAndAddToDrawPileEffect.class);
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class UnfuckCardVar {
        public static SpireField<Boolean> shouldUnfuck = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class})
    public static class ResetCard1 {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom, @ByRef AbstractCard[] ___card) {
            if(UnfuckCardVar.shouldUnfuck.get(srcCard)) {
                ___card[0] = srcCard;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(ShowCardAndAddToDrawPileEffect.class, "duration");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class ResetCard2 {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, boolean randomSpot, boolean toBottom, @ByRef AbstractCard[] ___card) {
            if(UnfuckCardVar.shouldUnfuck.get(srcCard)) {
                ___card[0] = srcCard;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(ShowCardAndAddToDrawPileEffect.class, "duration");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
