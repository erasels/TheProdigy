package theProdigy.patches.stances;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;
import theProdigy.stances.ProdigyStance;
import theProdigy.util.UC;

@SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
public class OnUseCardStancePatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(UseCardAction __instance, AbstractCard c, AbstractCreature target) {
        if(UC.p().stance instanceof ProdigyStance && !c.dontTriggerOnUseCard) {
            ((ProdigyStance) UC.p().stance).onUseCard(c, __instance);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardGroup.class, "group");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
