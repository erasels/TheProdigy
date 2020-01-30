package theProdigy.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import theProdigy.actions.common.CallbackDrawAction;

import java.util.function.Consumer;

public class DrawnCardHook {
    public static Consumer<AbstractCard> callback;
    private static AbstractCard[] card;

    @SpirePatch(clz = AbstractPlayer.class, method = "draw", paramtypez = {int.class})
    public static class AddCallback {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void setCard(AbstractPlayer __instance, int numCards, @ByRef AbstractCard[] c) {
            if (callback != null && numCards == 1) {
                card = c;
            }
        }

        @SpirePostfixPatch
        public static void patch(AbstractPlayer __instance, int numCards) {
            if (callback != null && card != null) {
                callback.accept(card[0]);
                card = null;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "removeTopCard");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = DrawCardAction.class, method = "update")
    public static class NewInstanceCapture {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr m) throws CannotCompileException {
                    if (m.getClassName().equals(DrawCardAction.class.getName())) {
                        m.replace("{" +
                                "if(" + DrawnCardHook.class.getName() + ".callback != null) {" +
                                "$_ = new " + CallbackDrawAction.class.getName() + "($1, " + DrawnCardHook.class.getName() + ".callback);" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}" +
                                "}");
                    }
                }
            };
        }
    }
}
