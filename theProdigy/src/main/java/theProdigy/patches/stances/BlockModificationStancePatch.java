package theProdigy.patches.stances;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;
import theProdigy.stances.ProdigyStance;
import theProdigy.util.UC;

@SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
public class BlockModificationStancePatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
    public static void patch(AbstractCard __instance, @ByRef float[] tmp) {
        if(UC.p().stance instanceof ProdigyStance) {
            tmp[0] = ((ProdigyStance) UC.p().stance).modifyBlock(tmp[0]);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
